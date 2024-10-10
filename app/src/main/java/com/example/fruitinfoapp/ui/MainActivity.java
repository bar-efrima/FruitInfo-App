package com.example.fruitinfoapp.ui;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.attribution.AppsFlyerRequestListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fruitinfoapp.R;
import com.example.fruitinfoapp.adapter.FruitsAdapter;
import com.example.fruitinfoapp.models.Fruit;
import com.example.fruitinfoapp.network.FruityViceService;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.util.Log;
import android.provider.Settings;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import com.appsflyer.AppsFlyerConversionListener;



/**
 *Main activity of the app, displaying and managing the list of fruits.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView FruitsRecyclerView;
    private FruitsAdapter fruitsAdapter;
    private Button resetButton;
    private List<Fruit> fruitsList;
    private TextView sortingNutritionText;

    // initialize Retrofit for FruityViceService calls
    private final Retrofit apiClient = new Retrofit.Builder()
            .baseUrl("https://www.fruityvice.com/api/")
            .addConverterFactory(GsonConverterFactory.create()).build();// Converter for JSON parsing

    private final FruityViceService fruityService = apiClient.create(FruityViceService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Setting the layout for this activity

        AppsFlyerLib.getInstance().setDebugLog(true); // Enable SDK debug logs
        AppsFlyerLib.getInstance().setCollectAndroidID(true);

        // AppsFlyer Conversion Listener, A conversion can be either Organic or Non-organic
        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionDataMap) {
                for (String attrName : conversionDataMap.keySet()) {
                    Log.d("MainActivity", "Conversion attribute: " + attrName + " = " + conversionDataMap.get(attrName));
                }
                String status = conversionDataMap.get("af_status").toString();
                if (status.equals("Non-organic")) {
                    if (conversionDataMap.get("is_first_launch").toString().equals("true")) {
                        Log.d("MainActivity", "Conversion: First Launch");
                    } else {
                        Log.d("MainActivity", "Conversion: Not First Launch");
                    }
                } else {
                    Log.d("MainActivity", "Conversion: This is an organic install.");
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.d("MainActivity", "Error getting conversion data: " + errorMessage);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> attributionData) {
                Log.d("MainActivity", "Attribution data: " + attributionData.toString());
            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d("MainActivity", "Error on attribution: " + errorMessage);
            }
        };

        // Initialize the SDK
        AppsFlyerLib.getInstance().init("fb8Q2zo9PAxeMYGSwXo6Bo", conversionListener, this);
        AppsFlyerLib.getInstance().start(this); // Starting the Android SDK
        sortingNutritionText = findViewById(R.id.sorting_nutrition_text); // Initialize the sorting nutrition TextView
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("AndroidID", "Android ID: " + androidId); // Log the device id
        // Initialize RecyclerView and its adapter
        FruitsRecyclerView = findViewById(R.id.recycler_view);
        FruitsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        fruitsAdapter = new FruitsAdapter();
        FruitsRecyclerView.setAdapter(fruitsAdapter);
        // Set up the filtering and Reset buttons
        setupButtons();
        // Fetch the initial fruits data
        fetchFruitsData();
    }

    // Log the button event clicks
    private void logButtonEvent(String buttonType) {
        Map<String, Object> eventValues = new HashMap<>();
        eventValues.put("Button_Type", buttonType);  // "Button_Type" is the key, and buttonType is the value ("Sugar", "Protein", "Calories", or "Reset")

        AppsFlyerLib.getInstance().logEvent(this, "Button Clicked", eventValues, new AppsFlyerRequestListener() {
            @Override
            public void onSuccess() {
                Log.d("MainActivity", "Event 'Button Clicked' with type '" + buttonType + "' sent successfully");
            }

            @Override
            public void onError(int errorCode, @NonNull String errorDescription) {
                Log.d("MainActivity", "Event 'Button Clicked' with type '" + buttonType + "' failed to be sent: Error code: " + errorCode + ", Description: " + errorDescription);
            }
        });
    }

    private void setupButtons() {
        findViewById(R.id.button_sugar).setOnClickListener(v -> {
            logButtonEvent("Sugar");
            sortAndHighlight("sugar");

        });
        findViewById(R.id.button_protein).setOnClickListener(v -> {
                logButtonEvent("Protein");
                sortAndHighlight("protein");
        });
        findViewById(R.id.button_calories).setOnClickListener(v -> {
                logButtonEvent("Calories");
                sortAndHighlight("calories");
        });
        resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(v -> {
            fetchFruitsData();
            sortingNutritionText.setText("Sort by: None");
            fruitsAdapter.SetHighlightedNutrition("");
            logButtonEvent("Reset");
        });
    }

    // Set up click listeners for sorting and reset buttons
    private void sortAndHighlight(String sortBy) {
        if (fruitsList != null) {
            switch (sortBy) {
                case "sugar":
                    Collections.sort(fruitsList, (f1, f2) -> Double.compare(f1.getNutritions().getSugar(), f2.getNutritions().getSugar()));
                    break;
                case "protein":
                    Collections.sort(fruitsList, (f1, f2) -> Double.compare(f1.getNutritions().getProtein(), f2.getNutritions().getProtein()));
                    break;
                case "calories":
                    Collections.sort(fruitsList, (f1, f2) -> Double.compare(f1.getNutritions().getCalories(), f2.getNutritions().getCalories()));
                    break;
            }
            fruitsAdapter.UpdateFruitsList(fruitsList);
            fruitsAdapter.SetHighlightedNutrition(sortBy);
            sortingNutritionText.setText("Sort by: " + capitalize(sortBy)); // Update the sorting Nutrition chosen
            FruitsRecyclerView.scrollToPosition(0); // Scroll to the start of the list
        }
    }

    // Make the first letter of a string capitalized
    private String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1); // Capitalizes the first character
    }

    // Fetches fruits data from the FruityVice API
    private void fetchFruitsData() {
        Call<List<Fruit>> call = fruityService.getFruits();
        call.enqueue(new Callback<List<Fruit>>() {
            @Override
            public void onResponse(Call<List<Fruit>> call, Response<List<Fruit>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fruitsList = response.body(); // Storing the list we got
                    fruitsAdapter.UpdateFruitsList(fruitsList);
                } else {
                    // Handling HTTP error responses
                    Log.e("MainActivity", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Fruit>> call, Throwable t) {
                // Handling network errors
                Log.e("MainActivity", "Network error: " + t.getMessage());
            }
        });
    }
}

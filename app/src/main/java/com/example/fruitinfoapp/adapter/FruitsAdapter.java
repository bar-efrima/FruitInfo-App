package com.example.fruitinfoapp.adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fruitinfoapp.R;
import com.example.fruitinfoapp.models.Fruit;
import java.util.ArrayList;
import java.util.List;

public class FruitsAdapter extends RecyclerView.Adapter<FruitsAdapter.FruitViewHolder> {

    private List<Fruit> fruitsList = new ArrayList<>(); // List to hold Fruit objects
    private String highlightedNutrition = "";  // To keep track of which nutrition is highlighted

    // Method to create new views, called by the RecyclerView
    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View fruitItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
        return new FruitViewHolder(fruitItemView);
    }

    // MMethod that binds the data to the view holder for each item in the RecyclerView.
    // It's called by the RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder holder, int position) {
        Fruit fruit = fruitsList.get(position);
        holder.displayFruitDetails(fruit, highlightedNutrition);
    }

    // Method to return the size of the dataset (called by the RecyclerView)
    @Override
    public int getItemCount() {
        return fruitsList.size();
    }

    //Method to update the fruitsList and notify the adapter about the data change
    public void UpdateFruitsList(List<Fruit> fruitsList) {
        this.fruitsList = fruitsList;
        notifyDataSetChanged();
    }

    // Method to set the highlighted nutrition and notify the adapter about the data change
    public void SetHighlightedNutrition(String nutrition) {
        this.highlightedNutrition = nutrition;
        notifyDataSetChanged();
    }

    // ViewHolder class to provide a reference to the views for each data item
    static class FruitViewHolder extends RecyclerView.ViewHolder {
        TextView fruitName, fruitSugar, fruitProtein, fruitCalories;

        FruitViewHolder(View itemView) {
            super(itemView); // passing the itemView to the RecyclerView.ViewHolder class for initialization
            fruitName = itemView.findViewById(R.id.fruit_name);
            fruitSugar = itemView.findViewById(R.id.fruit_sugar);
            fruitProtein = itemView.findViewById(R.id.fruit_protein);
            fruitCalories = itemView.findViewById(R.id.fruit_calories);
        }

        // Display the fruit details and highlight the nutrition based on the selected sorting type
        void displayFruitDetails(Fruit fruit, String highlightedNutrition){
            fruitName.setText(fruit.getName());
            fruitSugar.setText("Sugar: " + fruit.getNutritions().getSugar());
            fruitProtein.setText("Protein:" + fruit.getNutritions().getProtein());
            fruitCalories.setText("Calories: " + fruit.getNutritions().getCalories());
            fruitSugar.setTypeface(null, highlightedNutrition.equals("sugar") ? Typeface.BOLD : Typeface.NORMAL);
            fruitProtein.setTypeface(null, highlightedNutrition.equals("protein") ? Typeface.BOLD : Typeface.NORMAL);
            fruitCalories.setTypeface(null, highlightedNutrition.equals("calories") ? Typeface.BOLD : Typeface.NORMAL);
        }
    }
}

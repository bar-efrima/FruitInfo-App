package com.example.fruitinfoapp.network;
import com.example.fruitinfoapp.models.Fruit;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

///Retrofit interface for handling API calls to the FruityVice service///
public interface FruityViceService {

    @GET("fruit/all") // The endpoint to FruityVice for fetching all fruits data
    Call<List<Fruit>> getFruits();
}

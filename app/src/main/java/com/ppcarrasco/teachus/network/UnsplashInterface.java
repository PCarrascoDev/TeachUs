package com.ppcarrasco.teachus.network;


import com.ppcarrasco.teachus.models.Unsplash;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by pedro on 30-11-2017.
 */

public interface UnsplashInterface {
    @GET("random?count=1&query=books&orientation=landscape")
    Call<List<Unsplash>> get();
}

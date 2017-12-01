package com.ppcarrasco.teachus.background;

import android.os.AsyncTask;
import android.util.Log;

import com.ppcarrasco.teachus.models.Unsplash;
import com.ppcarrasco.teachus.network.UnsplashInterceptor;
import com.ppcarrasco.teachus.network.UnsplashInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by pedro on 30-11-2017.
 */

public class GetSplash extends AsyncTask<Void, Void, List<Unsplash>> {


    @Override
    protected List<Unsplash> doInBackground(Void... params) {

        UnsplashInterface request =  new UnsplashInterceptor().get();
        Call<List<Unsplash>> call = request.get();

        List<Unsplash> list = new ArrayList<>();

        try {
            Response<List<Unsplash>> response = call.execute();

            if (200 == response.code() && response.isSuccessful()) {

                if (response.body().size() > 0) {
                    list = response.body();

                    Log.d("LIST", list.toString());

                    if (list.size() > 0) {

                        for (Unsplash unsplash : list) {
                            Log.d("unsplash", unsplash.getUrls().getFull());
                        }

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        Log.d("LIST", String.valueOf(list));
        return list;


    }
}
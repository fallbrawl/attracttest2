package com.group.attract.attracttest2.Utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by paul on 11.10.17.
 */

public class RetrofitManager {
    private Retrofit retrofit;

    public SuperheroService getSuperheroService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(SuperheroService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(SuperheroService.class);
    }
}

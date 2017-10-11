package com.group.attract.attracttest2.Utils;

import com.group.attract.attracttest2.SuperheroProfile;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by paul on 11.10.17.
 */

public interface SuperheroService {
    String BASE_URL = "http://test.php-cd.attractgroup.com/";

    @GET("test.json")
    Call<ArrayList<SuperheroProfile>> getListSuperheroes();
}

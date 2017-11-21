package com.example.abyandafa.uploadimage.Service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Abyan Dafa on 13/11/2017.
 */

public class BaseRestService {

    public static Retrofit retrofit;
    public final static String BASE_URL = "http://10.151.253.151/rescue/public/";
    final static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build();
    final static OkHttpClient okHttpClient1 = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();

    public static Retrofit initializeRetrofit(int cek) {

        if(retrofit==null)
        {
            if(cek==1)
            {
                retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();
            }
            else
            {
                retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient1)
                        .build();
            }
        }

        return retrofit;
    }
}

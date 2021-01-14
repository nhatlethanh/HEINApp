package com.src.Network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.src.Model.StoreKey;

public class APIFsBs {
    private static Retrofit retrofit = null;

    public static Retrofit getAPIProduct() {
        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + StoreKey.getToken())
                        .build();
                return chain.proceed(newRequest);
            })
                    .connectTimeout(60, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();


            retrofit = new Retrofit.Builder()
                    .baseUrl(EndPoint.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

        }
        return retrofit;
    }

}

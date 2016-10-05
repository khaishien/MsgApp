package com.example.shen_mini_itx.msgapp.WebCall;

import com.example.shen_mini_itx.msgapp.Common.Constant;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shen-mini-itx on 20-Jul-16.
 */
public class  RestClient {

    private static RestApi REST_CLIENT;


    static {
        setupRestClient();
    }

    public static RestApi get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOTURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        REST_CLIENT = retrofit.create(RestApi.class);
    }
}

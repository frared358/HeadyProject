package francis.headyproject.remote;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import static okhttp3.logging.HttpLoggingInterceptor.Level.BODY;

public class ApiClient {

    public static Retrofit getClient(){

        Retrofit retrofit;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://stark-spire-93433.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

}

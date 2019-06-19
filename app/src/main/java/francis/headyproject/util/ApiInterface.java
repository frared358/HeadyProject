package francis.headyproject.util;

import francis.headyproject.pojo.ResponseData;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("json")
    Call<ResponseData> getData();


}

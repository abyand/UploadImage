package com.example.abyandafa.uploadimage.Service;

import com.example.abyandafa.uploadimage.Model.APIResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Abyan Dafa on 13/11/2017.
 */

public interface APIService {

    @Multipart
    @POST("upload")
    Call<APIResponse> postImage(@Part MultipartBody.Part image, @Part("email") RequestBody email,
                                @Part("longitude") RequestBody longitude, @Part("latitude") RequestBody latitude,
                                @Part("lokasi") RequestBody lokasi, @Part("foto") RequestBody foto,
                                @Part("ambulan") RequestBody ambulan);
}

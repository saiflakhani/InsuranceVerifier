package com.quicsolv.insurance.apiCalls;

import com.quicsolv.insurance.pojo.ApplicantDataVO;
import com.quicsolv.insurance.pojo.PhotoUploadResponse;
import com.quicsolv.insurance.pojo.QuestionDataVO;
import com.quicsolv.insurance.pojo.Verifier;

import java.util.List;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("clientlist")
    Call<List<ApplicantDataVO>> getClientList();

    @GET("questionslist")
    Call<List<QuestionDataVO>> getQuestionsList();

    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadPhoto(@Part MultipartBody.Part photo);

    @POST("addanswer")
    Call<ResponseBody> addAnswer(@Body ApplicantDataVO body);

    @GET("verifierlist")
    Call<List<Verifier>> getVerifierList();

    @GET("verifierclientlist")
    Call<List<ApplicantDataVO>> getVendorClientList(@Query("mobileNum") String number);

    @Multipart
    @POST("verification_image")
    Call<PhotoUploadResponse> postPhoto(@Query("uniqueId") String uniqueId, @Query("description") String description, @Part MultipartBody.Part file);
}
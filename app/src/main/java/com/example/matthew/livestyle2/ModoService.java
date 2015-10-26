package com.example.matthew.livestyle2;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.POST;

/**
 * Created by kevinyang on 10/24/15.
 */
public interface ModoService {

    //    @POST("/people/register")
//    void registerUser(@Header("X-Modo-Date") long date, @Header("Authorization") String authorization,
//                      @Body String user, Callback<Response> cb);
    @FormUrlEncoded
    @POST("/people/register")
    void registerUser(@Header("X-Modo-Date") long date, @Header("Authorization") String authorization,
                      @Field("is_modo_terms_agree") int agree, @Field("phone") String phone,
                      @Field("should_send_modo_descript") int descript, Callback<Response> cb);

    @FormUrlEncoded
    @POST("/card/add")
    void addCard(@Header("X-Modo-Date") long date, @Header("Authorization") String authorization, @Field("account_id") String accountID,
                 @Field("card_number") String card, @Field("card_security") String security, @Field("expiry") String exp,
                 @Field("zip_code") String zip,
                 Callback<Response> cb);


    @FormUrlEncoded
    @POST("/gift/send")
    void sendGift(@Header("X-Modo-Date") long date, @Header("Authorization") String authorization, @Field("account_id") String accountID,
                  @Field("gift_amount") Integer amount, @Field("should_notify_sender") Integer sender, @Field("should_notify_receiver") Integer notify, @Field("receiver_phone") String number,
                  Callback<Response> cb);
}

class User {
    String phone;
    String verify_password_url;
    boolean should_send_modo_descript;
    boolean is_modo_terms_agree;
    boolean should_send_password;

    public User(String newPhone, String newURL, boolean newDescript, boolean newTerms, boolean password
    ) {
        this.phone = newPhone;
        this.should_send_modo_descript = newDescript;
        this.is_modo_terms_agree = newTerms;
        this.verify_password_url = newURL;
        this.should_send_password = password;
    }
}

class UserResponse {
    String account_id;
    String first_name;
    String middle_name;
    String last_name;
    String suffix;
    String username;
    String member_since_date;
    String regCode;

    public String getAccount_id() {
        return account_id;
    }

    public String getRegCode() {
        return regCode;
    }
}
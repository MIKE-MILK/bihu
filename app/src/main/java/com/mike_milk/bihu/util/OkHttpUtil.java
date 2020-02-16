package com.mike_milk.bihu.util;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttpUtil {
/*
弃用的工具类，请忽视
 */
    public static void loginWithOkHttp(String account,String password,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("loginAccount",account)
                .add("loginPassword",password)
                .build();
        Request request=new Request.Builder()
                .url("http://bihu.jay86.com/login.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void registerWithOkHttp(String account,String password,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        RequestBody body=new FormBody.Builder()
                .add("registerAccount",account)
                .add("registerPassword",password)
                .build();
        Request request=new Request.Builder()
                .url("http://bihu.jay86.com/register.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}

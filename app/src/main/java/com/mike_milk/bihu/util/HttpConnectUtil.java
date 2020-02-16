package com.mike_milk.bihu.util;

import android.accounts.NetworkErrorException;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpConnectUtil {

    private static final int TIMEOUT_IN_MILLIONS = 5 * 1000;
    private static String Data;
    private static Handler handler = new Handler();

    public interface CallBack {
        void onResponse(String response);
    }


    public static void doAsyncPost(final String urlString, final String param, final CallBack callBack) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String response = doSyncPost(urlString, param);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onResponse(response);
                    }
                });
            }
        }).start();
    }



    public static String doSyncPost(final String urlString, final String param) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            connection.setReadTimeout(TIMEOUT_IN_MILLIONS);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.setUseCaches(false);
            connection.setRequestMethod("POST");

            connection.setInstanceFollowRedirects(true);

            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=utf-8");

            connection.setRequestProperty("Accept-Charset", "UTF-8");

            connection.connect();
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            dos.write(param.getBytes());
            dos.flush();
            dos.close();

            if (connection.getResponseCode() == 200) {

                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                Data = response.toString();
            } else {

                throw new NetworkErrorException("response status is " + connection.getResponseCode());
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return Data;
    }
}

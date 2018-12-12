package com.example.ksy.webapi;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
interface HttpGetCallbackListener{
    void onFinish(String data);
}
public class HttpUtil {
    public static void sendGetRequest(final Context context, final String address, final HttpGetCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);

                    Log.d("HttpUtil",connection.getResponseCode()+"\t" + connection.getResponseMessage());

                    if(connection.getResponseCode() == 200){
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while((line = reader.readLine()) != null){
                            response.append(line);
                        }
                        //Log.d("HttpUtil",response.toString());

                        JSONObject jsonObject = new JSONObject(response.toString());
                        boolean status = jsonObject.getBoolean("status");
                        if(listener!=null && status == true){
                            listener.onFinish(jsonObject.getString("data"));
                        }else {
                            new Thread(){
                                public void run(){
                                    Looper.prepare();
                                    Toast.makeText(context,"数据库不存在该条记录",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }.start();
                        }
                    }else{
                        Log.d("HttpUtil","Connect error!");
                    }
                }catch (Exception e){
                    Log.d("HttpUtil","Exception");
                    new Thread(){
                        public void run(){
                            Looper.prepare();
                            Toast.makeText(context,"网络连接失败",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }.start();
                    e.printStackTrace();
                }finally {
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}

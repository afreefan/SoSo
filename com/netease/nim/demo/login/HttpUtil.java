package com.netease.nim.demo.login;

/**
 * Created by dell on 2016/10/29.
 */
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.cache.memcached.SHA256KeyHashingScheme;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by dell on 2016/10/14.
 */
public class HttpUtil  {
    public static final String BASE_URL="http://42.96.129.1:8080/CTS/";


    public static HttpGet getHttpGet(String url){
        HttpGet request=new HttpGet(url);
        return request;
    }

    public static HttpPost getHttpPost(String url)
    {
        HttpPost request=new HttpPost(url);
        return request;

    }

    public static HttpResponse getHttpResponse(HttpGet request)throws ClientProtocolException,
            IOException{
        HttpResponse response=new DefaultHttpClient().execute(request);
        return response;
    }

    public  static HttpResponse getHttpResponse(HttpPost request)throws ClientProtocolException,
            IOException{
        HttpResponse response=new DefaultHttpClient().execute(request);
        return response;
    }

    public static  String queryStringForPost(String url){
        HttpPost request=HttpUtil.getHttpPost(url);
        String result=null;

        try{

            HttpResponse response=HttpUtil.getHttpResponse(request);

            if(response.getStatusLine().getStatusCode()==200) {

                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        }catch (ClientProtocolException e){
            e.printStackTrace();
            result="网络异常";
            return result;
        }
        catch (IOException e){
            e.printStackTrace();
            result="网络异常";
            return  result;
        }
        return null;
    }

    public static  String queryStringForget(String url){
        HttpGet request=HttpUtil.getHttpGet(url);
        String result =null;
        try{
            HttpResponse response=HttpUtil.getHttpResponse(request);

            if(response.getStatusLine().getStatusCode()==200){
                result=EntityUtils.toString(response.getEntity());
                return result;
            }
        }catch (ClientProtocolException e) {
            e.printStackTrace();
            result = "网络异常";
        }catch (IOException e){
            e.printStackTrace();
            result="网络异常";
            return result;
        }
        return null;
    }

    private static String queryStringForPost(HttpPost request){

        String result = null;
        try{
            HttpResponse response=HttpUtil.getHttpResponse(request);
            if(response.getStatusLine().getStatusCode()==200){
                result=EntityUtils.toString(response.getEntity());
            }
        }catch (ClientProtocolException e){
            e.printStackTrace();
            result="网络异常";
            return result;
        }catch (IOException e){
            e.printStackTrace();
            result="网络异常";
            return result;

        }
        return null;
    }

    public static String getJsonContent(String path){

        try {
            URL url=new URL(path);
            HttpURLConnection connection=(HttpURLConnection) url.openConnection();
            //connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            int code=connection.getResponseCode();
            Log.i("code",""+code);
            if(code==200){
                return changeInputString(connection.getInputStream());
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return "";
    }

    private static String changeInputString(InputStream inputStream) {

        String jsonString="";
        ByteArrayOutputStream outPutStream=new ByteArrayOutputStream();
        byte[] data=new byte[1024];
        int len=0;
        try {
            while((len=inputStream.read(data))!=-1){
                outPutStream.write(data, 0, len);
            }
            jsonString=new String(outPutStream.toByteArray());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonString;
    }





}
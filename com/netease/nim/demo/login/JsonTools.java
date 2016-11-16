package com.netease.nim.demo.login;

import com.netease.nim.demo.main.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/11/5.
 */
public class JsonTools {
    public static List<User> getUsers(String key, String jsonString) {
        List<User> list = new ArrayList<User>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(key);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                User user = new User();
                user.setUsername(jsonObject2.getString("username"));
                user.setPhotopath(jsonObject2.getString("photopath"));
                user.setLatitude(jsonObject2.getDouble("latitude"));
                user.setLongitude(jsonObject2.getDouble("longitude"));
                list.add(user);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }
}

package com.pufei.gxdt.utils;


import com.pufei.gxdt.module.user.bean.UserBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */

public class UserUtils {
    public static String getUser(UserBean user) {
        if (user != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", user.getName());
                jsonObject.put("gender", user.getGender());
                jsonObject.put("head", user.getHead());
                jsonObject.put("mind", user.getMind());
                jsonObject.put("address", user.getAddress());
                jsonObject.put("auth", user.getAuth());
                jsonObject.put("phone", user.getPhone());
                jsonObject.put("uid",user.getUid());
                jsonObject.put("wechat",user.getWechat());
                jsonObject.put("qq",user.getQq());
                jsonObject.put("state",user.getState());
                jsonObject.put("openid",user.getOpenid());
                jsonObject.put("orgin",user.getOrgin());
                return jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

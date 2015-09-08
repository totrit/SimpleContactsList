package com.example.alvin.simplecontactlist.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alvin.simplecontactlist.model.IDataLoadCallback;
import com.example.alvin.simplecontactlist.model.PerContactInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by maruilin on 15/9/7.
 */
public class NetUtils {
    public static void asyncFetchContacts(Context context, final IDataLoadCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        String url = "http://jsonplaceholder.typicode.com/users";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        List<PerContactInfo> ret = gson.fromJson(response, new TypeToken<List<PerContactInfo>>(){}.getType());

                        callback.onSuccess(ret);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFail(error.getLocalizedMessage());
            }
        });
        queue.add(stringRequest);
    }
}

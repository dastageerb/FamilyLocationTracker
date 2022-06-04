package com.example.familyLocationTracker.fcm;

import android.app.Activity;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationsSender
{


//    private RequestQueue requestQueue;
//    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
//
//    public void sendNotifications(String userFcmToken,String title,String body,String driverPhone,Activity activity,OnNotificationResponse callback,boolean feedback)
//    {
//        requestQueue = Volley.newRequestQueue(activity);
//        JSONObject mainObj = new JSONObject();
//        try {
//            mainObj.put("to", userFcmToken);
//            JSONObject notiObject = new JSONObject();
//            notiObject.put("title", title);
//            notiObject.put("body", body);
//            notiObject.put("driverPhoneNumber",driverPhone);
//            notiObject.put("feedback",feedback);
//           // notiObject.put("icon", "icon"); // enter icon that exists in drawable only
//            mainObj.put("data", notiObject);
//
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//
//                    callback.onNotificationSent();
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    // code run is got error
//
//                }
//            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//
//                    Map<String, String> header = new HashMap<>();
//                    header.put("content-type", "application/json");
//                    header.put("authorization", "key=" + Constants.FIREBASE_SERVER_KEY);
//                    return header;
//
//                }
//            };
//            requestQueue.add(request);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
}

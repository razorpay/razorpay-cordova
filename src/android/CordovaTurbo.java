package com.razorpay.cordova;


import static com.razorpay.cordova.Main.MAP_KEY_ERROR_CODE;
import static com.razorpay.cordova.Main.MAP_KEY_ERROR_CODE_UPI;
import static com.razorpay.cordova.Main.MAP_KEY_ERROR_DESC;
import static com.razorpay.cordova.Main.MAP_KEY_ERROR_DESC_UPI;
import static com.razorpay.cordova.Main.MAP_KEY_ERROR_OBJ;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.razorpay.Checkout;
import com.razorpay.GenericPluginCallback;
import com.razorpay.upi.UpiAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CordovaTurbo{

    private final Checkout checkout;

    public interface TurboResponseListener{
        void onSuccess(JSONObject data);
        void onError(JSONObject error);
    }

    public CordovaTurbo(Activity activity, String key){
        checkout = new Checkout().upiTurbo(activity);
        setKeyID(key);
    }

    public void setKeyID(String key){
        checkout.setKeyID(key);
        Log.i("CORTURLOGS", "setKeyID is called and key is : "+key);
    }

    public void linkNewUpiAccount(String customerMobile, String color, TurboResponseListener responseListener){
        try{
            checkout.upiTurbo.linkNewUpiAccount(customerMobile, color, new GenericPluginCallback() {
                @Override
                public void onSuccess(@NonNull Object o) {
                    try {
                        JSONObject data = new JSONObject().put("data","linked acc executed successfully");
                        responseListener.onSuccess(data);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onError(@NonNull JSONObject jsonObject) {
                    JSONObject error = new JSONObject();
                    try {
                        if(jsonObject.has(MAP_KEY_ERROR_CODE)){
                            error.put(MAP_KEY_ERROR_CODE_UPI, jsonObject.getString(MAP_KEY_ERROR_CODE));
                            error.put(MAP_KEY_ERROR_DESC_UPI, jsonObject.getString(MAP_KEY_ERROR_DESC));
                        }else if(jsonObject.has(MAP_KEY_ERROR_OBJ)){
                            JSONObject errObj = jsonObject.getJSONObject(MAP_KEY_ERROR_OBJ);
                            error.put(MAP_KEY_ERROR_CODE_UPI, errObj.getString(MAP_KEY_ERROR_CODE));
                            error.put(MAP_KEY_ERROR_DESC_UPI, errObj.getString(MAP_KEY_ERROR_DESC));
                        }else {
                            error.put(MAP_KEY_ERROR_CODE_UPI, jsonObject.getString(MAP_KEY_ERROR_CODE_UPI));
                            error.put(MAP_KEY_ERROR_DESC_UPI, jsonObject.getString(MAP_KEY_ERROR_DESC_UPI));
                        }
                        responseListener.onError(error);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void manageUpiAccounts(String customerMobile, String color, TurboResponseListener responseListener){
        try{
            checkout.upiTurbo.manageUpiAccounts(customerMobile, color, new GenericPluginCallback() {
                @Override
                public void onSuccess(@NonNull Object o) {
                    /*This callback is never used*/
                }

                @Override
                public void onError(@NonNull JSONObject jsonObject) {
                    JSONObject error = new JSONObject();
                    try {
                        if(jsonObject.has(MAP_KEY_ERROR_CODE)){
                            error.put(MAP_KEY_ERROR_CODE_UPI, jsonObject.getString(MAP_KEY_ERROR_CODE));
                            error.put(MAP_KEY_ERROR_DESC_UPI, jsonObject.getString(MAP_KEY_ERROR_DESC));
                        }else if(jsonObject.has(MAP_KEY_ERROR_OBJ)){
                            JSONObject errObj = jsonObject.getJSONObject(MAP_KEY_ERROR_OBJ);
                            error.put(MAP_KEY_ERROR_CODE_UPI, errObj.getString(MAP_KEY_ERROR_CODE));
                            error.put(MAP_KEY_ERROR_DESC_UPI, errObj.getString(MAP_KEY_ERROR_DESC));
                        }else {
                            error.put(MAP_KEY_ERROR_CODE_UPI, jsonObject.getString(MAP_KEY_ERROR_CODE_UPI));
                            error.put(MAP_KEY_ERROR_DESC_UPI, jsonObject.getString(MAP_KEY_ERROR_DESC_UPI));
                        }
                        responseListener.onError(error);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void open(Activity activity, JSONObject payload){
        checkout.open(activity, payload);
    }
}
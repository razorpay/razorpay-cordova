package com.razorpay.cordova;


import static com.razorpay.cordova.Main.MAP_KEY_ERROR_CODE;
import static com.razorpay.cordova.Main.MAP_KEY_ERROR_DESC;

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

    public static final String MAP_KEY_ERROR_CODE_UPI = "errorCode";
    public static final String MAP_KEY_ERROR_DESC_UPI = "errorDescription";

    private final Checkout checkout;

    public interface TurboResponseListener{
        void onSuccess(JSONObject data);
        void onError(JSONObject error);
    }

    public CordovaTurbo(Activity activity, String key){
        Log.i("CORTURLOGS", "CordovaTurbo is called");
        checkout = new Checkout().upiTurbo(activity);
        setKeyID(key);
    }

    public void setKeyID(String key){
        checkout.setKeyID(key);
        Log.i("CORTURLOGS", "setKeyID is called");
    }

    public void linkNewUpiAccount(String customerMobile, String color, TurboResponseListener responseListener){
        try{
            checkout.upiTurbo.linkNewUpiAccount(customerMobile, color, new GenericPluginCallback() {
                @Override
                public void onSuccess(@NonNull Object o) {
                    Log.d("CORTURLOGS","link Success Obj is : "+o);
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
                        error.put(MAP_KEY_ERROR_CODE, error.getString(MAP_KEY_ERROR_CODE_UPI));
                        error.put(MAP_KEY_ERROR_DESC, error.getString(MAP_KEY_ERROR_DESC_UPI));
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
            JSONObject data = new JSONObject().put("data",customerMobile);
            responseListener.onSuccess(data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void open(JSONObject options){

    }

}

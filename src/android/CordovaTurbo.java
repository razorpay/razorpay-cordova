package com.razorpay.cordova;


import android.app.Activity;
import com.razorpay.Checkout;
import org.json.JSONObject;

public class CordovaTurbo{

    private final Checkout checkout;

    public interface TurboResponseListener{
        void onSuccess(JSONObject data);
        void onError(JSONObject error);
    }

    public CordovaTurbo(Activity activity){
        checkout = new Checkout().upiTurbo(activity);
    }

    public void setKeyID(String key){
        checkout.setKeyID(key);
    }

    public void linkNewUpiAccount(String customerMobile, String color, TurboResponseListener responseListener){
        try{
            JSONObject data = new JSONObject().put("data",customerMobile);
            responseListener.onSuccess(data);
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

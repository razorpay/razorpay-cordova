package com.razorpay.cordova;



import static com.razorpay.cordova.Main.MAP_KEY_ERROR_CODE_UPI;
import static com.razorpay.cordova.Main.MAP_KEY_ERROR_DESC_UPI;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.GenericPluginCallback;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CordovaTurbo{

    private final Checkout checkout;
    private boolean isKeyIDSet;
    private final String defErrorCode = "AXIS_SDK_ERROR";
    private final String defErrorDescription = "Something went wrong.Please try again.";
    private final String error = "error";
    private final String code = "code";
    private final String description = "description";
    private Gson gson;

    public interface TurboResponseListener{
        void onSuccess(String data);
        void onError(JSONObject error);
    }

    public CordovaTurbo(Activity activity){
        checkout = new Checkout().upiTurbo(activity);
        gson = new Gson();
    }

    public void setKeyID(String key){
        checkout.setKeyID(key);
        isKeyIDSet=true;
    }

    public void onMerchantActivityResult(Activity activity, int requestCode, int resultCode, Intent data, PaymentResultWithDataListener listener, ExternalWalletListener externalWalletListener){
        checkout.merchantActivityResult(activity, requestCode, resultCode, data, listener, externalWalletListener);
    }

    public void destroyCheckout(){
        if(checkout!=null){
            checkout.upiTurbo.destroy();
        }
    }

    public void linkNewUpiAccount(String customerMobile, String color, TurboResponseListener responseListener){
        try{
            checkout.upiTurbo.linkNewUpiAccount(customerMobile, color, new GenericPluginCallback() {
                @Override
                public void onSuccess(@NonNull Object o) {
                    if (o instanceof List<?> && !((List<?>) o).isEmpty()) {
                        responseListener.onSuccess(toJsonString(o));
                    } else {
                        responseListener.onSuccess(new JSONArray().toString());
                    }
                }

                @Override
                public void onError(@NonNull JSONObject jsonObject) {
                    String errorCode = defErrorCode;
                    String errorDescription = defErrorDescription;
                    try {
                        if (jsonObject.has(error)) {
                            errorCode = jsonObject.getJSONObject(error).getString(code);
                            errorDescription = jsonObject.getJSONObject(error).getString(description);
                        }
                    } catch (Exception e) {
                        Log.d("Exception", "linkNewUpiAccount Exception :"+e.getMessage());
                    }
                    responseListener.onError(createErrorJsonObj(errorCode,errorDescription));
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
                    String errorCode = defErrorCode;
                    String errorDescription = defErrorDescription;
                    try {
                        if (jsonObject.has(error)) {
                            errorCode = jsonObject.getJSONObject(error).getString(code);
                            errorDescription = jsonObject.getJSONObject(error).getString(description);
                        }
                    } catch (Exception e) {
                        Log.d("Exception", "manageUpiAccounts Exception :"+e.getMessage());
                    }
                    responseListener.onError(createErrorJsonObj(errorCode,errorDescription));
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private JSONObject createErrorJsonObj(String errorCode, String errorDescription) {
        JSONObject error = new JSONObject();
        try {
            error.put(MAP_KEY_ERROR_CODE_UPI, errorCode);
            error.put(MAP_KEY_ERROR_DESC_UPI, errorDescription);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return error;
    }

    private String toJsonString(Object object){
        return this.gson.toJson(object);
    }

}
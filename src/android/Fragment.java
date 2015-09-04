package com.razorpay.cordova;

import com.razorpay.Checkout;
import org.apache.cordova.*;
import org.json.JSONObject;

public class Fragment extends Checkout {
  CallbackContext callbackContext;  

  public void setCallbackContext(CallbackContext c){
    callbackContext = c;
  }
  
  public void onSuccess(String razorpay_payment_id){
    callbackContext.success(razorpay_payment_id);
  }

  public void onError(int code, String response){
    JSONObject error = new JSONObject();
    try{
      error.put("code", code);
      error.put("description", response);
    } catch(Exception e){}
    callbackContext.error(error);
  }
};
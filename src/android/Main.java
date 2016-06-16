package com.razorpay.cordova;

import com.razorpay.Checkout;
import org.json.JSONObject;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.widget.Toast;

public class Main extends CordovaPlugin {

  CallbackContext c;

  @Override
  public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
    Checkout co = new Checkout();
    c = callbackContext;
    try{
      JSONObject options = new JSONObject(data.getString(0));
      co.setPublicKey(options.getString("key"));
      co.open(this.cordova.getActivity(), options);
    } catch (Exception e){
      Toast.makeText(this.cordova.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }
    return true;
  }

  public void onPaymentSuccess(String razorpay_payment_id) {
    c.success(razorpay_payment_id);
  }

  public void onPaymentError(int code, String response) {
    JSONObject error = new JSONObject();
    try{
      error.put("code", code);
      error.put("description", response);
    } catch(Exception e){
    }
    c.error(error);
  }
}

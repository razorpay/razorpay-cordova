package com.razorpay.cordova;

import com.razorpay.CheckoutActivity;
import org.json.JSONObject;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.widget.Toast;
import android.os.Bundle;
import android.content.Intent;
import android.app.Activity;


public class Main extends CordovaPlugin {
  public static final int RZP_REQUEST_CODE = 72967729;
  public static final String MAP_KEY_RZP_PAYMENT_ID = "razorpay_payment_id";
  public CallbackContext cc;

  @Override
  public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
    this.cc = callbackContext;
    try{
      Intent intent = new Intent(this.cordova.getActivity(), CheckoutActivity.class);
      intent.putExtra("OPTIONS", data.getString(0));
      this.cordova.startActivityForResult((CordovaPlugin)this, intent, RZP_REQUEST_CODE);
    } catch (Exception e){
      Toast.makeText(this.cordova.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }
    return true;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
      if (requestCode != RZP_REQUEST_CODE) return;
      String result = null;
      if(intent != null){
        Bundle extras = intent.getExtras();
        if(extras != null){
          result = extras.getString("RESULT");
        }
      }
      if(resultCode == 1){
        try {
          JSONObject successJson = new JSONObject(result);
          cc.success(successJson.getString(MAP_KEY_RZP_PAYMENT_ID));
        } catch (Exception e) {}
      }
      else{
        if(resultCode == Activity.RESULT_CANCELED){
          result = "Payment Cancelled";
        }
        JSONObject error = new JSONObject();
        try{
          error.put("code", resultCode);
          error.put("description", result);
        } catch(Exception e){}
        cc.error(error);
      }
    }
}
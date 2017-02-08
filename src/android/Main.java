package com.razorpay.cordova;

import com.razorpay.CheckoutActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultWithDataListener;
import com.razorpay.PaymentData;
import org.json.JSONObject;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.widget.Toast;
import android.os.Bundle;
import android.content.Intent;
import android.app.Activity;


public class Main extends CordovaPlugin implements PaymentResultWithDataListener {
  public static final String MAP_KEY_ERROR_CODE = "code";
  public static final String MAP_KEY_ERROR_DESC = "description";
  public static final String MAP_KEY_ERROR_DATA = "error_data";
  public static final String MAP_KEY_PAYMENT_ID = "payment_id";
  public static final String MAP_KEY_ORDER_ID = "order_id";
  public static final String MAP_KEY_SIGNATURE = "signature";
  private String userAction;
  public CallbackContext cc;

  @Override
  public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
    this.cc = callbackContext;
    this.userAction = action;
    try{
      Intent intent = new Intent(this.cordova.getActivity(), CheckoutActivity.class);
      intent.putExtra("OPTIONS", data.getString(0));
      intent.putExtra("FRAMEWORK", "cordova");
      this.cordova.startActivityForResult((CordovaPlugin)this, intent, Checkout.RZP_REQUEST_CODE);
    } catch (Exception e){
      Toast.makeText(this.cordova.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }
    return true;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    Checkout.handleActivityResult(this.cordova.getActivity(), requestCode, resultCode, intent, this);
  }

  @Override
  public void onPaymentSuccess(String razorpayPaymentId, PaymentData paymentData) {
    if (this.userAction.equalsIgnoreCase("open")) {
      cc.success(razorpayPaymentId);
    } else if (this.userAction.equalsIgnoreCase("openWithAdditionalData")) {
      try {
        JSONObject data = new JSONObject();
        data.put(MAP_KEY_PAYMENT_ID, razorpayPaymentId);
        if(paymentData.getOrderId() != null){
          data.put(MAP_KEY_ORDER_ID, paymentData.getOrderId());
        }
        if(paymentData.getSignature() != null){
          data.put(MAP_KEY_SIGNATURE, paymentData.getSignature());
        }
        cc.success(data);
      } catch(Exception e){}
    }
  }

  @Override
  public void onPaymentError(int code, String description, PaymentData paymentData) {
    if (this.userAction.equalsIgnoreCase("open")) {
      try {
          JSONObject error = new JSONObject();
          error.put(MAP_KEY_ERROR_CODE, code);
          error.put(MAP_KEY_ERROR_DESC, description);
          cc.error(error);
      } catch(Exception e){}
    } else if (this.userAction.equalsIgnoreCase("openWithAdditionalData")) {
      try {
          JSONObject error = new JSONObject();
          error.put(MAP_KEY_ERROR_CODE, code);
          error.put(MAP_KEY_ERROR_DESC, description);
          if (paymentData.getData() != null) {
            error.put(MAP_KEY_ERROR_DATA, paymentData.getData());
          }
          cc.error(error);
      } catch(Exception e){}
    }
  }
}

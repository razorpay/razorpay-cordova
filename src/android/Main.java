package com.razorpay.cordova;

import com.razorpay.cordova.Fragment;
import org.json.JSONObject;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.widget.Toast;

public class Main extends CordovaPlugin {
  @Override
  public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
    Fragment co = new Fragment();
    co.setCallbackContext(callbackContext);
    try{
      JSONObject options = new JSONObject(data.getString(0));
      co.setPublicKey(options.getString("key"));
      co.open(this.cordova.getActivity(), options);
    } catch (Exception e){
      Toast.makeText(this.cordova.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }
    return true;
  }
}
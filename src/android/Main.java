package com.razorpay.cordova;

import com.razorpay.cordova.Fragment;
import org.json.JSONObject;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
// import android.widget.Toast;

public class Main extends CordovaPlugin {
  @Override
  public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
    Fragment co = new Fragment();
    co.setCallbackContext(callbackContext);
    try{
      co.open(this.cordova.getActivity(), new JSONObject(data.getString(0)));
    } catch (Exception e){

    }
    return true;
  }
}
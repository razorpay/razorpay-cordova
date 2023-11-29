package com.razorpay.cordova;

import com.razorpay.CheckoutActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultWithDataListener;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import org.json.JSONObject;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import android.widget.Toast;
import android.os.Bundle;
import android.content.Intent;


public class Main extends CordovaPlugin implements PaymentResultWithDataListener, ExternalWalletListener {
    public static final String MAP_KEY_ERROR_CODE = "code";
    public static final String MAP_KEY_ERROR_DESC = "description";
    public static final String MAP_KEY_ERROR_CODE_UPI = "errorCode";
    public static final String MAP_KEY_ERROR_DESC_UPI = "errorDescription";
    public static final String MAP_KEY_ERROR_OBJ = "error";
    public static final String MAP_KEY_CONTACT = "contact";
    public static final String MAP_KEY_EMAIL = "email";
    public static final String MAP_KEY_EXTERNAL_WALLET_NAME = "external_wallet_name";

    private String userAction;
    public CallbackContext cc;
    private Checkout checkout;
    private CordovaTurbo cordovaTurbo;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        this.cc = callbackContext;
        this.userAction = action;
        switch (action) {
            case "setKeyID":{
                if (cordovaTurbo != null){
                    cordovaTurbo.setKeyID(data.getString(0));
                }else{
                    JSONObject tele = new JSONObject();
                    tele.put(MAP_KEY_ERROR_CODE_UPI, "BAD_REQUEST_ERROR");
                    tele.put(MAP_KEY_ERROR_DESC_UPI, "Please initialize UPI Turbo by triggering initTurbo function");
                    cc.error(tele.toString());
                }
                break;
            }
            case "initUpiTurbo":{
                cordovaTurbo = new CordovaTurbo(this.cordova.getActivity(), data.getString(0));
                break;
            }
            case "linkNewUpiAccount": {
                if (cordovaTurbo != null){
                    cordovaTurbo.linkNewUpiAccount(data.getString(0), data.getString(1), new CordovaTurbo.TurboResponseListener() {
                        @Override
                        public void onSuccess(JSONObject data) {
                            cc.success(data);
                        }

                        @Override
                        public void onError(JSONObject error) {
                            cc.error(error);
                        }
                    });
                } else {
                    JSONObject tele = new JSONObject();
                    tele.put(MAP_KEY_ERROR_CODE_UPI, "BAD_REQUEST_ERROR");
                    tele.put(MAP_KEY_ERROR_DESC_UPI, "Please initialize UPI Turbo by triggering initTurbo function");
                    cc.error(tele.toString());
                }
                break;
            }
            case "open": {
                if (cordovaTurbo!=null){
                    JSONObject payload = new JSONObject(data.getString(0));
                    payload.put("FRAMEWORK", "cordova");
                    cordovaTurbo.open(this.cordova.getActivity(), payload);
                }else{
                    try {
                        Intent intent = new Intent(this.cordova.getActivity(), CheckoutActivity.class);
                        intent.putExtra("OPTIONS", data.getString(0));
                        intent.putExtra("FRAMEWORK", "cordova");
                        this.cordova.startActivityForResult((CordovaPlugin) this, intent, Checkout.RZP_REQUEST_CODE);
                    } catch (Exception e) {
                        Toast.makeText(this.cordova.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            }
            case "manageUpiAccount": {
                if (cordovaTurbo != null){
                    cordovaTurbo.manageUpiAccounts(data.getString(0), data.getString(1), new CordovaTurbo.TurboResponseListener() {
                        @Override
                        public void onSuccess(JSONObject data) {
                            /*This callback is never used*/
                        }

                        @Override
                        public void onError(JSONObject error) {
                            cc.error(error);
                        }
                    });
                }else{
                    JSONObject tele = new JSONObject();
                    tele.put(MAP_KEY_ERROR_CODE_UPI, "BAD_REQUEST_ERROR");
                    tele.put(MAP_KEY_ERROR_DESC_UPI, "Please initialize UPI Turbo by triggering initTurbo function");
                    cc.error(tele.toString());
                }
            }
        }
        return true;
    }

    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putString("action", this.userAction);
        return bundle;
    }

    @Override
    public void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext) {
        if (state != null) {
            this.userAction = state.getString("action");
        }
        this.cc = callbackContext;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Checkout.handleActivityResult(this.cordova.getActivity(), requestCode, resultCode, intent, this, this);
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentId, PaymentData paymentData) {
        if (this.userAction.equalsIgnoreCase("open")) {
            cc.success(paymentData.getData());
        }
    }

    @Override
    public void onPaymentError(int code, String description, PaymentData paymentData) {
        if (this.userAction.equalsIgnoreCase("open")) {
            try {
                JSONObject error = new JSONObject();
                error.put(MAP_KEY_ERROR_CODE_UPI, code);
                error.put(MAP_KEY_ERROR_DESC_UPI, description);
                error.put(MAP_KEY_CONTACT, paymentData.getUserContact());
                error.put(MAP_KEY_EMAIL, paymentData.getUserEmail());
                cc.error(error);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onExternalWalletSelected(String name, PaymentData paymentData) {
        if (this.userAction.equalsIgnoreCase("open")) {
            try {
                JSONObject response = new JSONObject();
                response.put(MAP_KEY_EXTERNAL_WALLET_NAME, name);
                response.put(MAP_KEY_EMAIL, paymentData.getUserEmail());
                response.put(MAP_KEY_CONTACT, paymentData.getUserContact());
                cc.error(response);
            } catch (Exception e) {
            }
        }
    }
}
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

import android.util.Log;
import android.widget.Toast;
import android.os.Bundle;
import android.content.Intent;


public class Main extends CordovaPlugin implements PaymentResultWithDataListener, ExternalWalletListener {
    public static final String MAP_KEY_ERROR_CODE = "code";
    public static final String MAP_KEY_ERROR_DESC = "description";
    public static final String MAP_KEY_ERROR_CODE_UPI = "errorCode";
    public static final String MAP_KEY_ERROR_DESC_UPI = "errorDescription";
    public static final String MAP_KEY_CONTACT = "contact";
    public static final String MAP_KEY_EMAIL = "email";
    public static final String MAP_KEY_EXTERNAL_WALLET_NAME = "external_wallet_name";
    public static final String MSG_BAD_REQUEST_ERROR = "BAD_REQUEST_ERROR";
    public static final String MSG_INIT_UPI_TURBO = "Please initialize UPI Turbo by triggering initTurbo function";
    public static final String MSG_FEATURE_NOT_FOUND_ERROR = "FEATURE_NOT_FOUND";
    public static final String MSG_UPI_TURBO_PLUGIN_NOT_FOUND = "There is no UPI Turbo plugin available. Please contact Razorpay support.";

    private String userAction;
    public CallbackContext cc;
    private CordovaTurbo cordovaTurbo;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        this.cc = callbackContext;
        this.userAction = action;
        switch (action) {
            case "open": {
                if(isTurboPluginAvailable() && cordovaTurbo==null){
                    initializeCordovaTurbo();
                }
                try {
                    Intent intent = new Intent(this.cordova.getActivity(), CheckoutActivity.class);
                    intent.putExtra("OPTIONS", data.getString(0));
                    intent.putExtra("FRAMEWORK", "cordova");
                    this.cordova.startActivityForResult((CordovaPlugin) this, intent, Checkout.RZP_REQUEST_CODE);
                } catch (Exception e) {
                    Toast.makeText(this.cordova.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
            }

            /**
             * Need to discuss weather we need to keep this setKeyID case or not
             * */
            case "setKeyID":{
                if (cordovaTurbo != null){
                    cordovaTurbo.setKeyID(data.getString(0));
                }else{
                    JSONObject init_error_obj = new JSONObject();
                    init_error_obj.put(MAP_KEY_ERROR_CODE_UPI, MSG_BAD_REQUEST_ERROR);
                    init_error_obj.put(MAP_KEY_ERROR_DESC_UPI, MSG_INIT_UPI_TURBO);
                    cc.error(init_error_obj.toString());
                }
                break;
            }

            case "initUpiTurbo":{
                if(!isTurboPluginAvailable()){
                    triggerUPITurboNotAvailableError();
                    break;
                }

                if(cordovaTurbo==null){
                    initializeCordovaTurbo();
                }
                cordovaTurbo.setKeyID(data.getString(0));
                break;
            }

            case "linkNewUpiAccount": {
                if(!isTurboPluginAvailable()){
                    triggerUPITurboNotAvailableError();
                    break;
                }

                if (cordovaTurbo != null && cordovaTurbo.isKeyIDSet()){
                    cordovaTurbo.linkNewUpiAccount(data.getString(0), data.getString(1), new CordovaTurbo.TurboResponseListener() {
                        @Override
                        public void onSuccess(String data) {
                            cc.success(data);
                        }

                        @Override
                        public void onError(JSONObject error) {
                            cc.error(error);
                        }
                    });
                } else {
                    JSONObject init_error_obj = new JSONObject();
                    init_error_obj.put(MAP_KEY_ERROR_CODE_UPI, MSG_BAD_REQUEST_ERROR);
                    init_error_obj.put(MAP_KEY_ERROR_DESC_UPI, MSG_INIT_UPI_TURBO);
                    cc.error(init_error_obj.toString());
                }
                break;
            }

            case "manageUpiAccount": {
                if(!isTurboPluginAvailable()){
                    triggerUPITurboNotAvailableError();
                    break;
                }

                if (cordovaTurbo != null && cordovaTurbo.isKeyIDSet()){
                    cordovaTurbo.manageUpiAccounts(data.getString(0), data.getString(1), new CordovaTurbo.TurboResponseListener() {
                        @Override
                        public void onSuccess(String data) {
                            /*This callback is never used*/
                        }

                        @Override
                        public void onError(JSONObject error) {
                            cc.error(error);
                        }
                    });
                }else{
                    JSONObject init_error_obj = new JSONObject();
                    init_error_obj.put(MAP_KEY_ERROR_CODE_UPI, MSG_BAD_REQUEST_ERROR);
                    init_error_obj.put(MAP_KEY_ERROR_DESC_UPI, MSG_INIT_UPI_TURBO);
                    cc.error(init_error_obj.toString());
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
        if(isTurboPluginAvailable()){
            cordovaTurbo.onMerchantActivityResult(this.cordova.getActivity(), requestCode, resultCode, intent, this, this);
        }else {
            /**
             * Deprecated but used for Non-Turbo Checkout SDK
             * */
            Checkout.handleActivityResult(this.cordova.getActivity(), requestCode, resultCode, intent, this, this);
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentId, PaymentData paymentData) {
        if (this.userAction.equalsIgnoreCase("open")) {
            JSONObject response = new JSONObject();
            try {
                response.put("razorpay_payment_id", paymentData.getPaymentId());
                response.put("razorpay_user_contact", paymentData.getOrderId());
                response.put("razorpay_user_email", paymentData.getOrderId());

                /**
                 * These will come in production mode
                 * */
                //response.putOpt("razorpay_order_id", paymentData.getOrderId());
                //response.putOpt("razorpay_signature", paymentData.getSignature());
            } catch (JSONException e) {
                /**
                 * Intentionally kept empty to allow the triggering of success callback
                 * even in cases of getting other keys in response
                 * */
            }
            cc.success(response);
        }
    }

    @Override
    public void onPaymentError(int code, String description, PaymentData paymentData) {
        if (this.userAction.equalsIgnoreCase("open")) {
            try {
                JSONObject error = new JSONObject();
                if(isTurboPluginAvailable()){
                    error.put(MAP_KEY_ERROR_CODE_UPI, code);
                    error.put(MAP_KEY_ERROR_DESC_UPI, description);
                }else {
                    error.put(MAP_KEY_ERROR_CODE, code);
                    error.put(MAP_KEY_ERROR_DESC, description);
                }
                error.put(MAP_KEY_CONTACT, paymentData.getUserContact());
                error.put(MAP_KEY_EMAIL, paymentData.getUserEmail());
                cc.error(error);
            } catch (Exception e) {
                Log.d("Exception", "onPaymentError Exception: "+e.getMessage());
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
                Log.d("Exception", "onExternalWalletSelected Exception: "+e.getMessage());
            }
        }
    }

    private void initializeCordovaTurbo(){
        if(cordovaTurbo==null){
            cordovaTurbo = new CordovaTurbo(this.cordova.getActivity());
        }
    }

    private boolean isTurboPluginAvailable() {
        try {
            /**
             * Class found, which indicates turbo plugin exists
             */
            Class.forName("com.razorpay.UpiTurboLinkAccountResultListener");
            return true;
        } catch (ClassNotFoundException e) {
            /**
             * Class not found, so it doesn't exist
             */
            return false;
        }
    }

    private void triggerUPITurboNotAvailableError(){
        JSONObject init_error_obj = new JSONObject();
        try {
            init_error_obj.put(MAP_KEY_ERROR_CODE_UPI, MSG_FEATURE_NOT_FOUND_ERROR);
            init_error_obj.put(MAP_KEY_ERROR_DESC_UPI, MSG_UPI_TURBO_PLUGIN_NOT_FOUND);
            cc.error(init_error_obj.toString());
        } catch (JSONException e) {
            /**
             * This block will only execute when CallbackContext is null
             * */
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cordovaTurbo!=null){
            cordovaTurbo.destroyCheckout();
        }
    }
}
/*global cordova, module*/

module.exports = {
    open: function (options, successCallback, errorCallback) {
      if (successCallback) {
        this.callbacks['payment.success'] = function(response) {
          successCallback(response.razorpay_payment_id);
        }
      }

      if (errorCallback) {
        this.callbacks['payment.cancel'] = errorCallback;
      }

      cordova.exec(
        this.pluginCallback,
        this.pluginCallback,
        'Checkout',
        'open',
        [
          JSON.stringify(options)
        ]
      );
    },

    pluginCallback: function(response){
      if(response.razorpay_payment_id){
        callbacks['payment.success'](response);
      }
      else if(response.external_wallet_name){
        callbacks['payment.external_wallet_name'](response);
      }
      else if(response.code){
        callbacks['payment.cancel'](response);
      }
    },

    callbacks: {},

    on: function(event, callback) {
      if (typeof event === 'string' && typeof callback === 'function') {
        this.callbacks[event] = callback;
      }
    },

    onResume: function(event) {
      if(event.pendingResult && event.pendingResult.pluginServiceName === 'Checkout'){
        this.pluginCallback(event.pendingResult.result);
      }
    }
};

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
        this.callbacks['payment.success'],
        this.callbacks['payment.cancel'],
        'Checkout',
        'open',
        [
          JSON.stringify(options)
        ]
      );
    },

    callbacks: {},

    on: function(event, callback) {
      if (typeof event === 'string' && typeof callback === 'function') {
        this.callbacks[event] = callback;
      }
    },

    onResume: function(event) {
      if(event.pendingResult){
        if(event.pendingResult.pluginStatus === "OK") {
            this.callbacks['payment.success'](event.pendingResult.result);
        } 
        else {
            this.callbacks['payment.cancel'](event.pendingResult.result);
        }
      }
    }
};

# Cordova/Phonegap bindings for Razorpay's Mobile SDKs

Offcial Cordova/Phonegap plugin for integrating Razorpay's checkout. Supported platforms are Android and iOS.

## Usage:
Install the plugin

    $ cd your-project-folder
    $ cordova platform add android # optional
    $ cordova platform add ios     # optional
    $ cordova plugin add https://github.com/razorpay/razorpay-cordova.git

(or, `phonegap plugin add https://github.com/razorpay/razorpay-cordova.git`)

Integration code

    var options = {
        description: 'Credits towards consultation',
        image: 'https://i.imgur.com/3g7nmJC.png',
        currency: 'INR',
        key: 'rzp_test_1DP5mmOlF5G5ag',
        amount: '5000',
        name: 'foo',
        prefill: {email: 'pranav@razorpay.com', contact: '8879524924', name: 'Pranav Gupta'},
        theme: {color: '#F37254'}
    }

    var successCallback = function(payment_id) {
        alert('payment_id: ' + payment_id);
    }

    var cancelCallback = function(error) {
        alert(error.description + ' (Error '+error.code+')');
    }

    RazorpayCheckout.open(options, successCallback, cancelCallback);


Change the options accordingly. Supported options can be found [here](https://docs.razorpay.com/docs/checkout-form#checkout-fields). This code snippet can be added anytime after `deviceready` event.

# Cordova/Phonegap bindings for Razorpay's Mobile SDKs

Cordova plugin for integrating razorpay's payment on mobile. Currently supports android only.

## Usage:
Install the plugin

    $ cd your-project-folder
    $ cordova plugin add https://github.com/pronav/razorpay-cordova.git

(or, `phonegap plugin add https://github.com/pronav/razorpay-cordova.git`)

Integration code

    var options = {
        description: 'Credits towards consultation',
        image: 'https://i.imgur.com/3g7nmJC.png',
        currency: 'INR',
        key: 'rzp_test_1DP5mmOlF5G5ag',
        amount: '5000',
        name: 'foo',
        prefill: {email: 'pranav@razorpay.com', contact: '8879524924', name: 'Pranav Gupta'}
    }

    var successCallback = function(payment_id) {
        alert('payment_id: ' + payment_id);
    }

    var cancalCallback = function(error) {
        alert(error.description + ' (Error '+error.code+')');
    }

    RazorpayCheckout.open(options, successCallback, cancalCallback);


Change the options accordingly. This code snipette can be added anytime after `deviceready` event.
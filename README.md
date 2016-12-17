# Cordova/Phonegap bindings for Razorpay's Mobile SDKs

Official Cordova/Phonegap plugin for integrating Razorpay's checkout.

## Supported platforms

- Android
- iOS
- Browser

You can check out the sample apps for cordova, ionic1 & ionic2 in https://github.com/razorpay/razorpay-cordova-sample-app

## Usage:

Install the plugin

```bash
cd your-project-folder
cordova platform add android      # optional
cordova platform add ios          # optional
cordova platform add browser      # optional
cordova plugin add https://github.com/razorpay/razorpay-cordova.git --save

```

(or, `phonegap plugin add https://github.com/razorpay/razorpay-cordova.git --save`)

## Integration code

```js
var options = {
  description: 'Credits towards consultation',
  image: 'https://i.imgur.com/3g7nmJC.png',
  currency: 'INR',
  key: 'rzp_test_1DP5mmOlF5G5ag',
  amount: '5000',
  name: 'foo',
  prefill: {
    email: 'pranav@razorpay.com',
    contact: '8879524924',
    name: 'Pranav Gupta'
  },
  theme: {
    color: '#F37254'
  }
}

var successCallback = function(payment_id) {
  alert('payment_id: ' + payment_id)
}

var cancelCallback = function(error) {
  alert(error.description + ' (Error '+error.code+')')
}

RazorpayCheckout.open(options, successCallback, cancelCallback)

```

Change the options accordingly. Supported options can be found [here](https://docs.razorpay.com/docs/checkout-form#checkout-fields).

### Things to be taken care:

- Add the integration code snippet after `deviceready` event.

- On browser platform, change the [Content Security Policy](https://content-security-policy.com/) to whitelist the `razorpay.com` domain.

```html
<meta http-equiv="Content-Security-Policy" content="default-src 'self' https://*.razorpay.com data: gap: https://ssl.gstatic.com 'unsafe-eval'; style-src 'self' 'unsafe-inline'; media-src *">

```


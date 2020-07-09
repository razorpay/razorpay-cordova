# Cordova/Phonegap bindings for Razorpay's Mobile SDKs

[![NPM Version](http://img.shields.io/npm/v/com.razorpay.cordova.svg?style=flat)](https://www.npmjs.com/package/com.razorpay.cordova)
[![NPM Downloads](https://img.shields.io/npm/dm/com.razorpay.cordova.svg?style=flat)](https://npmcharts.com/compare/com.razorpay.cordova?minimal=true)
[![Install Size](https://packagephobia.now.sh/badge?p=com.razorpay.cordova)](https://packagephobia.now.sh/result?p=com.razorpay.cordova)

Official Cordova/Phonegap plugin for integrating Razorpay's checkout.

* [Supported platforms](#supported-platforms)
* [Prerequisites](#prerequisites)
* [Usage](#usage)
* [Integration code](#integration-code)
* [External Wallets](#external-wallets)
* [Example App](https://github.com/razorpay/razorpay-cordova-sample-app)
* [Android Lifecycle Guide](#android-lifecycle-guide)
* [Notes](#things-to-be-taken-care)
* [FAQ's](#faq)

## Supported platforms

- Android
- iOS
- Browser

You can check out the sample apps for:- 
* [Cordova v9.0](https://github.com/razorpay/razorpay-cordova-sample-app/tree/master/rzp-cordova-example)
* [ionic1](https://github.com/razorpay/razorpay-cordova-sample-app/tree/master/rzp-ionic1-example)
* [ionic2](https://github.com/razorpay/razorpay-cordova-sample-app/tree/master/rzp-ionic2-example)
* [ionic3](https://github.com/razorpay/razorpay-cordova-sample-app/tree/master/rzp-ionic3-example)
* [ionic5](https://github.com/razorpay/razorpay-cordova-sample-app/tree/master/rzp-ionic-5-example)

## Prerequisites

 - Learn about the <a href="/docs/payment-gateway/payment-flow/" target="_blank">Razorpay Payment Flow</a>.
 - Sign up for a <a href="https://dashboard.razorpay.com/#/access/signin">Razorpay Account</a> and generate the <a href="https://razorpay.com/docs/payment-gateway/dashboard-guide/settings/#api-keys/" target="_blank">API Keys</a> from the Razorpay Dashboard. Using the Test keys helps simulate a sandbox environment. No actual monetary transaction happens when using the Test keys. Use Live keys once you have thoroughly tested the application and are ready to go live.
 
## Usage:

Install the plugin

**Note**: For Windows users, please run this on Git Bash instead of Command Prompt. You can download Git for Windows [here](https://github.com/git-for-windows/git/releases/latest).

```bash
cd your-project-folder
cordova platform add android      # optional
cordova platform add ios          # make sure your ios version is ios@5 or latest.
cordova platform add browser      # optional
cordova plugin add https://github.com/razorpay/razorpay-cordova.git --save

```
(or, `phonegap plugin add https://github.com/razorpay/razorpay-cordova.git --save`)

**Note**: Make sure that you set Always Embed Swift Standard Libraries of your main target to yes. 

**Note**: Remember to enable bitcode on your iOS project. We support Xcode 11+ versions. 

## Integration code

### Orders API Flow

With the advent of `auto-capture` using [Order API](https://docs.razorpay.com/v1/page/orders), the integration needs to change a little ([only if you are using this flow](https://docs.razorpay.com/v1/page/orders#auto-capturing-payment)). The only change is that the callbacks have to be added as events. Here is a code sample:

```js
var options = {
  description: 'Credits towards consultation',
  image: 'https://i.imgur.com/3g7nmJC.png',
  currency: 'INR',
  key: '', // your api key.
  order_id: 'order_7HtFNLS98dSj8x',
  amount: '5000',
  name: 'foo',
  theme: {
    color: '#F37254'
  }
}

var successCallback = function(success) {
  alert('payment_id: ' + success.razorpay_payment_id)
  var orderId = success.razorpay_order_id
  var signature = success.razorpay_signature
}

var cancelCallback = function(error) {
  alert(error.description + ' (Error '+error.code+')')
}

RazorpayCheckout.on('payment.success', successCallback)
RazorpayCheckout.on('payment.cancel', cancelCallback)
RazorpayCheckout.open(options)
```

Change the options accordingly. Supported options can be found [here](https://docs.razorpay.com/docs/checkout-form#checkout-fields).

### External Wallets
We also support **displaying** wallets like Citrus and Paytm, which are currently not a part of the standard Razorpay offering. After the user chooses which one of these they want, control is handed back to you with data like wallet name, contact and email of the user. This helps you take the next steps towards facilitating the payment and Razorpay's role in that payment cycle ends there.

To add a wallet, change the `options` JSON as follows:
```js
var options = {
  currency: 'INR',
  key: '',
  amount: '5000',
  external: {
    wallets: ['paytm']
  },
  ...
  ...
  ...
}
```

To get callback for this, add this before calling `open`:
```js
RazorpayCheckout.on('payment.external_wallet', externalWalletCallback)
```

### Legacy

This is legacy integration code and we will continue to support it till further notice. Your server needs to send capture request in this scenario, after the payment is completed.

```js
var options = {
  description: 'Credits towards consultation',
  image: 'https://i.imgur.com/3g7nmJC.png',
  currency: 'INR',
  key: '',
  amount: '5000',
  name: 'foo',
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

## Android Lifecycle Guide 
***It is recomended that you read [this](https://cordova.apache.org/docs/en/latest/guide/platforms/android/#lifecycle-guide) first before proceeding with this section***

Since our plugin launches a new activity on Android, the cordova activity goes in the background
and might get destroyed by the Android System. For this scenario, you need to add the following code to make sure the 
payment result is delivered after the cordova activity is recreated:
```
// You need to register an event listener for the `resume` event
document.addEventListener('resume', onResume, false);
var onResume = function(event) {
        // Re-register the payment success and cancel callbacks
        RazorpayCheckout.on('payment.success', successCallback)
        RazorpayCheckout.on('payment.cancel', cancelCallback)
        // Pass on the event to RazorpayCheckout
        RazorpayCheckout.onResume(event);
      };


```

## Things to be taken care:

- Add the integration code snippet after `deviceready` event.

- On browser platform, change the [Content Security Policy](https://content-security-policy.com/) to whitelist the `razorpay.com` domain.

```html
<meta http-equiv="Content-Security-Policy" content="default-src 'self' https://*.razorpay.com data: gap: https://ssl.gstatic.com 'unsafe-eval'; style-src 'self' 'unsafe-inline'; media-src *">

```

- Due to the way ionic works, we can't support `ionic serve` at the moment. Try using `ionic run browser` instead of `ionic serve`. `ionic serve` doesn't support cordova browser plugins at the moment. See [driftyco/ionic-cli#354](https://github.com/driftyco/ionic-cli/issues/354).

## FAQ

If you are still not able to integrate, then go to [FAQ](https://github.com/razorpay/razorpay-cordova/wiki/FAQ).

## Note

We don't support capacitor because of the app support dependency, for more details go through [this link](https://capacitor.ionicframework.com/docs/getting-started/dependencies#ios-development).

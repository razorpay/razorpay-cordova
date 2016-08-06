(function injectRZPScript(callback) {
  var rjs = document.getElementsByTagName('script')[0];
  var js = document.getElementById('rzp-jssdk');
  if (js) return;

  js = document.createElement('script');
  js.id = 'rzp-jssdk';
  js.onload = function() {
    if (typeof callback === 'function') callback();
  }
  js.src = 'https://checkout.razorpay.com/v1/checkout.js';
  rjs.parentNode.insertBefore(js, rjs);
}())

function open(successCallback, cancelCallback, args) {
  var options = normalizeOptions(successCallback, cancelCallback, args);
  if (window.Razorpay) {
    openRZP(options)
  } else {
    injectRZPScript(function() {
      openRZP(options)
    })
  }
}

function normalizeOptions(successCallback, cancelCallback, args) {
  var options = JSON.parse(args[0]);
  options.handler = function(response) {
    successCallback(response);
  }
  return options;
}

function openRZP(options) {
  var rzp = new Razorpay(options);
  rzp.open();
}

module.exports = {
  open: open
}

require('cordova/exec/proxy').add('Checkout', module.exports);

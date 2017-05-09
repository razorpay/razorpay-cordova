function injectRZPScript(callback) {
  var rjs = document.getElementsByTagName('script')[0]
  var js = document.getElementById('rzp-jssdk')
  if (js) return

  js = document.createElement('script')
  js.id = 'rzp-jssdk'
  js.onload = function() {
    if (typeof callback === 'function') callback()
  }

  js.onerror = function() {
    if (typeof callback === 'function') callback({code: 0, description: 'Network error'})
  }

  js.src = 'https://checkout.razorpay.com/v1/checkout.js'
  rjs.parentNode.insertBefore(js, rjs)
}
injectRZPScript()

function open(successCallback, cancelCallback, args) {
  var options = normalizeOptions(successCallback, cancelCallback, args)
  if (window.Razorpay) {
    openRZP(options)
  } else {
    injectRZPScript(function(error) {
      if (error.code === 0) {
        cancelCallback(error)
      } else {
        openRZP(options)
      }
    })
  }
}

function normalizeOptions(successCallback, cancelCallback, args) {
  var options = JSON.parse(args[0])
  options.modal = options.modal || {}

  options.modal.ondismiss = function() {
    cancelCallback({
      code: 2,
      description: 'Payment cancelled by user'
    })
  }

  options.handler = function(response) {
    successCallback(response)
  }

  if (options.external && options.external.wallets && options.external.wallets.length) {
    options.external.handler = function(response) {
      response.external_wallet_name = response.wallet
      cancelCallback(response)
    }
  }

  return options
}

function openRZP(options) {
  var rzp = new Razorpay(options)
  rzp.open()
}

module.exports = {
  open: open
}

require('cordova/exec/proxy').add('Checkout', module.exports)

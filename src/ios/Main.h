#import <Cordova/CDV.h>

// razorpay-pod 1.5.0+ is modularised: the checkout delegate protocols
// (RazorpayPaymentCompletionProtocolWithData, ExternalWalletSelectionProtocol)
// now live in the RazorpayCore module, while RazorpayCheckout is still exported
// by the Razorpay module. razorpay-pod <= 1.4.x ships a single Razorpay module
// that declares both, so the extra import is guarded for backward compatibility.
#if __has_include(<RazorpayCore/RazorpayCore-Swift.h>)
#import <RazorpayCore/RazorpayCore-Swift.h>
#endif

#import <Razorpay/Razorpay-Swift.h>

@interface Main : CDVPlugin

@property NSString *callbackId;

- (void)open:(CDVInvokedUrlCommand *)command;

@end

#import <Cordova/CDV.h>
#import <Razorpay/Razorpay-Swift.h>

@interface Main : CDVPlugin

@property NSString *callbackId;

- (void)open:(CDVInvokedUrlCommand *)command;

@end

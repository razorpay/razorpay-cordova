#import <Razorpay/RazorpayCheckout.h>
#import "Main.h"

@interface RazorpayPayment : RazorpayCheckout
@property Main* main;
@property NSString* callbackId;
@end
#import "Main.h"
#import "RazorpayPayment.h"

@implementation Main

- (void)open:(CDVInvokedUrlCommand*)command
{

    NSDictionary * options = [NSJSONSerialization JSONObjectWithData:[[[command arguments] objectAtIndex:0] dataUsingEncoding:NSUTF8StringEncoding] options:0 error:nil];
    
    RazorpayPayment * co = [[RazorpayPayment alloc] initWithKey:@"rzp_test_1DP5mmOlF5G5ag"];
    co.main = self;
    co.callbackId = [command callbackId];
    [co open:options];
}

@end
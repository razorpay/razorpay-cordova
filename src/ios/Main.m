#import "Main.h"
#import "RazorpayPayment.h"

@implementation Main

- (void)open:(CDVInvokedUrlCommand*)command
{

    NSDictionary * options = [NSJSONSerialization JSONObjectWithData:[[[command arguments] objectAtIndex:0] dataUsingEncoding:NSUTF8StringEncoding] options:0 error:nil];
    
    RazorpayPayment * co = [[RazorpayPayment alloc] initWithKey: (NSString *)[options objectForKey:@"key"]];
    co.main = self;
    co.callbackId = [command callbackId];
    [co open:options];
}

@end
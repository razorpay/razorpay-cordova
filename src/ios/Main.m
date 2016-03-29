#import "Main.h"

@interface Main () <RazorpayPaymentCompletionProtocol> {
  Razorpay *razorpay;
}

@end

@implementation Main

- (void)open:(CDVInvokedUrlCommand *)command {

  NSDictionary *options = [NSJSONSerialization
      JSONObjectWithData:[[[command arguments] objectAtIndex:0]
                             dataUsingEncoding:NSUTF8StringEncoding]
                 options:0
                   error:nil];

  razorpay = [Razorpay
            initWithKey:(NSString *)[options objectForKey:@"key"]
            andDelegate:self
      forViewController:[[[[UIApplication sharedApplication] delegate] window]
                            rootViewController]];

  self.callbackId = [command callbackId];
  [razorpay open:options];
}

- (void)onPaymentError:(int)code description:(nonnull NSString *)str {
  CDVPluginResult *result = [CDVPluginResult
         resultWithStatus:CDVCommandStatus_ERROR
      messageAsDictionary:[NSDictionary dictionaryWithObjectsAndKeys:
                                            [NSNumber numberWithInt:code],
                                            @"code", str, @"description", nil]];
  [self.commandDelegate sendPluginResult:result callbackId:self.callbackId];
}

- (void)onPaymentSuccess:(nonnull NSString *)payment_id {
  CDVPluginResult *result =
      [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                        messageAsString:payment_id];
  [self.commandDelegate sendPluginResult:result callbackId:self.callbackId];
}

@end

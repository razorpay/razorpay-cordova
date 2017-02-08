#import "Main.h"

@interface Main () <RazorpayPaymentCompletionProtocolWithData> {
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

  razorpay = [Razorpay initWithKey:(NSString *)[options objectForKey:@"key"]
               andDelegateWithData:self];

  self.callbackId = [command callbackId];
  [razorpay open:options];
}

- (void)onPaymentError:(int)code
           description:(nonnull NSString *)str
               andData:(nullable NSDictionary *)response {
  CDVPluginResult *result = [CDVPluginResult
         resultWithStatus:CDVCommandStatus_ERROR
      messageAsDictionary:[NSDictionary dictionaryWithObjectsAndKeys:
                                            [NSNumber numberWithInt:code],
                                            @"code", str, @"description",
                                            response, @"response", nil]];
  [self.commandDelegate sendPluginResult:result callbackId:self.callbackId];
}

- (void)onPaymentSuccess:(nonnull NSString *)payment_id
                 andData:(nullable NSDictionary *)response {
  CDVPluginResult *result =
      [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                    messageAsDictionary:response];
  [self.commandDelegate sendPluginResult:result callbackId:self.callbackId];
}

@end

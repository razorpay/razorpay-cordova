
#import <Foundation/Foundation.h>
#import "RazorpayPayment.h"

@implementation RazorpayPayment

- (void)failureHandler:(int) code description:(nonnull NSString *) str{
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR 
        messageAsDictionary:[NSDictionary dictionaryWithObjectsAndKeys:
           [NSNumber numberWithInt:code], @"code",
           str, @"description",
           nil]
        ];
    [self.main.commandDelegate sendPluginResult:result callbackId:self.callbackId];
};

- (void)successHandler:(nonnull NSString*) payment_id{
    CDVPluginResult* result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:payment_id];
    [self.main.commandDelegate sendPluginResult:result callbackId:self.callbackId];
};

@end

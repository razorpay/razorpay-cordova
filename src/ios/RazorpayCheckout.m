#import <Foundation/Foundation.h>
#import "RazorpayPayment.h"
#import "Main.h"

@implementation RazorpayPayment

- (void)failureHandler:(int) code description:(nonnull NSString *) str{
    [[[UIAlertView alloc] initWithTitle:@"Error" message:str delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
};

- (void)successHandler:(nonnull NSString*) payment_id{
    [[[UIAlertView alloc] initWithTitle:@"Payment Successful" message:payment_id delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil] show];
};

@end


#import <UIKit/UIKit.h>

@interface Checkout : UIViewController

@property (nonnull) UIWebView * webview;

- (void)failureHandler:(int) code description:(nonnull NSString *) str;

- (void)successHandler:(nonnull NSString*) payment_id;

- (void)open:(nonnull NSDictionary*) options;

- (void)close;

- (nonnull id)initWithKey:(nonnull NSString *) key webView: (nonnull UIWebView *) w;

- (nonnull id)initWithKey:(nonnull NSString *) key;

@end

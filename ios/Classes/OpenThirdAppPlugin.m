#import "OpenThirdAppPlugin.h"
#import <open_third_app/open_third_app-Swift.h>

@implementation OpenThirdAppPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftOpenThirdAppPlugin registerWithRegistrar:registrar];
}
@end

import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:open_third_app/open_third_app.dart';

void main() {
  const MethodChannel channel = MethodChannel('open_third_app');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
//    expect(await OpenThirdApp.platformVersion, '42');
  });
}

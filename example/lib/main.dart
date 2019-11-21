import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:open_third_app/open_third_app.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
//      platformVersion = await OpenThirdApp.openThirdApp("DDDDDD");
//      platformVersion += await OpenThirdApp.openThirdApp;

    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(

          child: Column(children: <Widget>[
            Text('Running on: $_platformVersion\n'),
            FlatButton(child: Text("打开地图",),onPressed: (){
              OpenThirdApp.checkMapAndNavigation("22.556105","113.12779","江门市三连电子有限公司");
            },),
          ],),
        ),
      ),
    );
  }
}


//"https://itunes.apple.com/cn/app/%E5%A4%A9%E5%A4%A9%E4%BA%94%E6%8A%98/id1439149050?mt=8"
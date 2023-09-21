// Package imports:
import 'package:dio/dio.dart';

class TokenInterceptors extends InterceptorsWrapper {
  String? _token;

  @override
  onRequest(RequestOptions options, handler) async {
    //授权码
    if (_token == null) {
      // var authorizationCode = await getAuthorization();
      // if (authorizationCode != null) {
      //   _token = authorizationCode;
      //   await initClient(_token);
      // }
    }
    if(_token != null) {
      options.headers["Authorization"] = _token;
    }
    return super.onRequest(options, handler);
  }

  @override
  onResponse(Response response, handler) async {
    try {
      var responseJson = response.data;
      if (response.statusCode == 201 && responseJson["token"] != null) {
        _token = 'token ' + responseJson["token"];
        // await LocalStorage.save(Config.TOKEN_KEY, _token);
      }
    } catch (e) {
      print(e);
    }
    return super.onResponse(response, handler);
  }
}

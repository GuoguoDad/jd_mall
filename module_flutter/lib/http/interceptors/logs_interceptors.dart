// Dart imports:
import 'dart:convert';

// Package imports:
import 'package:dio/dio.dart';

class LogsInterceptors extends InterceptorsWrapper {
  static List<Map?> sHttpResponses = [];
  static List<String?> sResponsesHttpUrl = [];

  static List<Map<String, dynamic>?> sHttpRequest = [];
  static List<String?> sRequestHttpUrl = [];

  static List<Map<String, dynamic>?> sHttpError = [];
  static List<String?> sHttpErrorUrl = [];

  @override
  onRequest(RequestOptions options, handler) async {
    print("请求url：${options.path} ${options.method}");
    options.headers.forEach((k, v) => options.headers[k] = v ?? "");
    print('请求头: ${options.headers}');
    if (options.data != null) {
      print('请求参数: ${options.data}');
    }
    try {
      addLogic(sRequestHttpUrl, options.path);
      var data;
      if (options.data is Map) {
        data = options.data;
      } else {
        data = <String, dynamic>{};
      }
      var map = {
        "header:": {...options.headers},
      };
      if (options.method == "POST") {
        map["data"] = data;
      }
      addLogic(sHttpRequest, map);
    } catch (e) {
      print(e);
    }
    return super.onRequest(options, handler);
  }

  @override
  onResponse(Response response, handler) async {
    print('返回结果: $response');
    if (response.data is Map || response.data is List) {
      try {
        var data = <String, dynamic>{};
        data["data"] = response.data;
        addLogic(sResponsesHttpUrl, response.requestOptions.uri.toString());
        addLogic(sHttpResponses, data);
      } catch (e) {
        print(e);
      }
    } else if (response.data is String) {
      try {
        var data = <String, dynamic>{};
        data["data"] = response.data;
        addLogic(sResponsesHttpUrl, response.requestOptions.uri.toString());
        addLogic(sHttpResponses, data);
      } catch (e) {
        print(e);
      }
    } else if (response.data != null) {
      try {
        String data = response.data.toJson();
        addLogic(sResponsesHttpUrl, response.requestOptions.uri.toString());
        addLogic(sHttpResponses, json.decode(data));
      } catch (e) {
        print(e);
      }
    }
    return super.onResponse(response, handler);
  }

  @override
  onError(DioException err, handler) async {
    print('请求异常: $err');
    print('请求异常信息: ${err.response?.toString() ?? err.error.toString()}');
    try {
      addLogic(sHttpErrorUrl, err.requestOptions.path);
      var errors = <String, dynamic>{};
      errors["error"] = err.message;
      addLogic(sHttpError, errors);
    } catch (e) {
      print(e);
    }
    return super.onError(err, handler);
  }

  static addLogic(List list, data) {
    if (list.length > 20) {
      list.removeAt(0);
    }
    list.add(data);
  }
}

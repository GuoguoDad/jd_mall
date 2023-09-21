// Dart imports:
import 'dart:collection';

// Flutter imports:
import 'package:flutter/foundation.dart';

// Package imports:
import 'package:dio/dio.dart';

// Project imports:
import 'package:module_flutter/http/base_response.dart';
import 'package:module_flutter/http/code.dart';
import 'package:module_flutter/http/interceptors/logs_interceptors.dart';
import 'package:module_flutter/http/interceptors/network_interceptor.dart';
import 'package:module_flutter/http/interceptors/response_interceptor.dart';
import 'package:module_flutter/http/interceptors/token_Interceptor.dart';

class HttpManager {
  static const contentTypeJson = "application/json";
  static const contentTypeFormData = "multipart/form-data";
  static const contentTypeForm = "application/x-www-form-urlencoded";

  final Dio _dio = Dio(); // 使用默认配置

  HttpManager() {
    _dio.interceptors.add(TokenInterceptors());
    _dio.interceptors.add(NetworkInterceptors());
    if (kDebugMode) {
      _dio.interceptors.add(LogsInterceptors());
    }
    _dio.interceptors.add(ResponseInterceptors());
  }

  Future<BaseResponse> request(String url, params, Options option, bool? noTip) async {
    noTip ??= false;
    option.headers ??= HashMap();
    option.headers!['content-type'] = contentTypeJson;
    option.headers!['accept'] = contentTypeJson;
    option.headers!['connectTimeout'] = 30000;
    option.headers!['receiveTimeout'] = 30000;

    exceptionHandler(DioException err) {
      Response? errResponse;
      if (err.response != null) {
        errResponse = err.response;
      } else {
        errResponse = Response(statusCode: err.response?.statusCode, requestOptions: RequestOptions(path: url));
      }
      if (err.type == DioExceptionType.connectionTimeout || err.type == DioExceptionType.receiveTimeout) {
        errResponse!.statusCode = Code.NETWORK_TIMEOUT;
      }
      String errMsg = err.response?.toString() ?? err.error.toString();
      return BaseResponse(
        code: Code.errorHandleFunction(errResponse?.statusCode, errMsg, false),
        msg: errMsg,
        data: null,
      );
    }

    Response<BaseResponse> response;
    try {
      response = await _dio.request<BaseResponse>(url, data: params, options: option);
    } on DioException catch (e) {
      return exceptionHandler(e);
    }
    if (response.data is DioException) {
      return exceptionHandler(response.data as DioException);
    }
    return BaseResponse.fromJson(response.data?.data as Map<String, dynamic>);
  }

  ///get发起网络请求
  ///[ url] 请求url
  ///[ params] 请求参数
  ///[ option] 配置
  Future<BaseResponse> get(String url, {Object? params, Options? option, bool? noTip}) async {
    option ??= Options();
    option.method = "get";
    return await request(url, params, option, noTip);
  }

  ///post发起网络请求
  ///[ url] 请求url
  ///[ params] 请求参数
  ///[ option] 配置
  Future<BaseResponse> post(String url, {Object? params, Options? option, bool? noTip}) async {
    option ??= Options();
    option.method = "post";
    return await request(url, params, option, noTip);
  }
}

final HttpManager httpManager = HttpManager();

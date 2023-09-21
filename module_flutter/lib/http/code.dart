// Project imports:
import 'package:module_flutter/common/event/http_error_event.dart';
import 'package:module_flutter/common/event/index.dart';

///错误编码
class Code {
  ///网络错误
  static const NETWORK_ERROR = -1;

  ///网络超时
  static const NETWORK_TIMEOUT = -2;

  ///网络返回数据格式化一次
  static const NETWORK_JSON_EXCEPTION = -3;

  ///Connection refused
  static const CONNECTION_REFUSED = -4;

  static const SUCCESS = 200;

  static errorHandleFunction(code, message, hideToast) {
    // print("========code:${code}");
    if (hideToast) {
      return message;
    }
    if (message != null && message is String && (message.contains("Connection refused") || message.contains("Connection reset"))) {
      code = CONNECTION_REFUSED;
    }
    eventBus.fire(HttpErrorEvent(code, message));
    return message;
  }
}

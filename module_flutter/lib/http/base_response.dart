class BaseResponse<T> {
  String? code;
  String? msg;
  T? data;

  BaseResponse({
    required this.code,
    required this.msg,
    required this.data,
  });

  factory BaseResponse.fromJson(Map<String, dynamic> json) => BaseResponse(
    code: json["code"],
    msg: json["msg"],
    data: json["data"],
  );

  Map<String, dynamic> toJson() => {
    "code": code,
    "msg": msg,
    "data": data,
  };
}
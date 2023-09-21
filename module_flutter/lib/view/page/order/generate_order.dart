// Flutter imports:
import 'package:flutter/material.dart';

// Project imports:
import 'package:module_flutter/common/style/common_style.dart';
import 'package:module_flutter/component/common_header.dart';
import 'package:module_flutter/view/page/order/widget/bottom.dart';
import 'package:module_flutter/view/page/order/widget/default_address.dart';
import 'package:module_flutter/view/page/order/widget/order_goods.dart';
import 'package:module_flutter/view/page/order/widget/order_invoice.dart';
import 'package:module_flutter/view/page/order/widget/order_summation.dart';

class GenerateOrder extends StatefulWidget {
  const GenerateOrder({Key? key}) : super(key: key);

  @override
  State<StatefulWidget> createState() => _GenerateOrderState();
}

class _GenerateOrderState extends State<GenerateOrder> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          commonHeader(context, title: "填写订单"),
          Expanded(
            flex: 1,
            child: Container(
              color: CommonStyle.colorF5F5F5,
              child: CustomScrollView(
                slivers: [
                  defaultAddress(context),
                  orderGoods(context),
                  orderSummation(context),
                  orderInvoice(context),
                ],
              ),
            ),
          ),
          fixedBottom(context)
        ],
      ),
    );
  }
}

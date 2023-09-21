// Flutter imports:
import 'package:flutter/material.dart';

class BoxPlaceholder extends StatelessWidget {
  final double width;
  final double height;
  final EdgeInsetsGeometry? margin;
  final Color? color;

  const BoxPlaceholder({
    Key? key,
    required this.width,
    required this.height,
    this.margin,
    this.color,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: width,
      height: height,
      margin: margin,
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(12.0),
        color: color ?? Colors.grey,
      ),
    );
  }
}

class SearchPlaceholder extends StatelessWidget {
  const SearchPlaceholder({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
        height: 44 + MediaQueryData.fromView(View.of(context)).padding.top,
        margin: const EdgeInsets.only(left: 12.0, right: 12),
        padding: EdgeInsets.fromLTRB(0, MediaQueryData.fromView(View.of(context)).padding.top, 0, 0),
        child: Stack(
            alignment: Alignment.center,
            fit: StackFit.expand,
            children: <Widget>[Positioned(top: 5, child: BoxPlaceholder(width: MediaQuery.of(context).size.width - 32, height: 38))]));
  }
}

class BannerPlaceholder extends StatelessWidget {
  const BannerPlaceholder({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      height: 180.0,
      margin: const EdgeInsets.only(left: 16.0, right: 16, top: 10, bottom: 10),
      decoration: BoxDecoration(
        borderRadius: BorderRadius.circular(12.0),
        color: Colors.grey,
      ),
    );
  }
}

class TitlePlaceholder extends StatelessWidget {
  final double width;

  const TitlePlaceholder({
    Key? key,
    required this.width,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: width,
            height: 12.0,
            color: Colors.white,
          ),
          const SizedBox(height: 8.0),
          Container(
            width: width,
            height: 12.0,
            color: Colors.white,
          ),
        ],
      ),
    );
  }
}

enum LineOneType {
  fourLines,
  fiveLines,
}

class LineOnePlaceholder extends StatelessWidget {
  final LineOneType lineType;

  const LineOnePlaceholder({
    Key? key,
    required this.lineType,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0),
      child: Row(
        mainAxisSize: MainAxisSize.max,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Container(
            width: 96.0,
            height: 96.0,
            decoration: BoxDecoration(
              borderRadius: BorderRadius.circular(12.0),
              color: Colors.white,
            ),
          ),
          const SizedBox(width: 12.0),
          Expanded(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Container(
                  width: double.infinity,
                  height: 10.0,
                  color: Colors.white,
                  margin: const EdgeInsets.only(bottom: 8.0),
                ),
                Container(
                  width: double.infinity,
                  height: 10.0,
                  color: Colors.white,
                  margin: const EdgeInsets.only(bottom: 8.0),
                ),
                if (lineType == LineOneType.fiveLines)
                  Container(
                    width: double.infinity,
                    height: 10.0,
                    color: Colors.white,
                    margin: const EdgeInsets.only(bottom: 8.0),
                  ),
                Container(
                  width: 160.0,
                  height: 10.0,
                  color: Colors.white,
                  margin: const EdgeInsets.only(bottom: 8.0),
                ),
                Container(
                  width: 80.0,
                  height: 10.0,
                  color: Colors.white,
                )
              ],
            ),
          )
        ],
      ),
    );
  }
}

enum LineMenuType {
  oneLine,
  twoLine,
}

class LineMenuPlaceholder extends StatelessWidget {
  final LineMenuType lineType;

  const LineMenuPlaceholder({
    Key? key,
    required this.lineType,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
        padding: const EdgeInsets.symmetric(horizontal: 16.0),
        child: Column(
          children: [
            const SizedBox(height: 15),
            const Row(
              mainAxisSize: MainAxisSize.max,
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: [
                BoxPlaceholder(width: 40, height: 40),
                BoxPlaceholder(width: 40, height: 40),
                BoxPlaceholder(width: 40, height: 40),
                BoxPlaceholder(width: 40, height: 40),
                BoxPlaceholder(width: 40, height: 40)
              ],
            ),
            if (lineType == LineMenuType.twoLine) const SizedBox(height: 15),
            if (lineType == LineMenuType.twoLine)
              const Row(
                mainAxisSize: MainAxisSize.max,
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                children: [
                  BoxPlaceholder(width: 40, height: 40),
                  BoxPlaceholder(width: 40, height: 40),
                  BoxPlaceholder(width: 40, height: 40),
                  BoxPlaceholder(width: 40, height: 40),
                  BoxPlaceholder(width: 40, height: 40)
                ],
              ),
          ],
        ));
  }
}

class LineTwoPlaceholder extends StatelessWidget {
  const LineTwoPlaceholder({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    double itemWidth = MediaQuery.of(context).size.width / 2 - 24;
    return Container(
        padding: const EdgeInsets.symmetric(horizontal: 16.0),
        width: MediaQuery.of(context).size.width,
        child: Column(
          children: [
            Row(
              mainAxisSize: MainAxisSize.max,
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Container(
                  width: itemWidth,
                  height: itemWidth,
                  margin: const EdgeInsets.only(bottom: 12),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(12.0),
                    color: Colors.white,
                  ),
                ),
                Container(
                  width: itemWidth,
                  height: itemWidth,
                  margin: const EdgeInsets.only(bottom: 12),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(12.0),
                    color: Colors.white,
                  ),
                ),
              ],
            ),
            Row(
              mainAxisSize: MainAxisSize.max,
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Container(
                  width: itemWidth,
                  height: itemWidth,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(12.0),
                    color: Colors.white,
                  ),
                ),
                Container(
                  width: itemWidth,
                  height: itemWidth,
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(12.0),
                    color: Colors.white,
                  ),
                ),
              ],
            ),
          ],
        ));
  }
}

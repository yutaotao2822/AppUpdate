class AppUpdateStatusBean {
  int maxSize = 0;
  int downSize = 0;
  AppUpdateStatus downStatus = AppUpdateStatus.wait;
  String? error;

  AppUpdateStatusBean(Map? map) {
    if (map == null || map.isEmpty) return;
    maxSize = int.parse(map["maxSize"].toString());
    downSize = int.parse(map["downSize"].toString());
    downStatus = {
          0: AppUpdateStatus.wait,
          1: AppUpdateStatus.start,
          2: AppUpdateStatus.download,
          3: AppUpdateStatus.cancel,
          4: AppUpdateStatus.error,
          5: AppUpdateStatus.done
        }[int.parse(map["downStatus"].toString())] ??
        AppUpdateStatus.wait;
    error = map["errMsg"].toString();
  }

  @override
  String toString() {
    return 'AppUpdateStatusBean{maxSize: $maxSize, downSize: $downSize, downStatus: $downStatus, error: $error}';
  }
}

enum AppUpdateStatus {
  //0未开始 1开始下载 2下载中 3取消下载 4下载异常 5下载完成
  wait(0),
  start(1),
  download(2),
  cancel(3),
  error(4),
  done(5);

  final int id;

  const AppUpdateStatus(this.id);
}

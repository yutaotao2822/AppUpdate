# yt_update

一款APP更新的插件，本插件需要自己定义界面，只提供app下载功能

## Getting Started

    使用方法：

    1.初始化
         final _ytUpdatePlugin = YtUpdate();
    2.通过google渠道更新
        _ytUpdatePlugin.updateGoogle();
        不需要任何参数

    3.ios更新
        _ytUpdatePlugin.update(
            apkUrl: "跳转到apple Store的链接",//如https://apps.apple.com/us/app/smart-m/id6462580670
            listener: (status) {
             //更新是否成功
        });

    4.android内置安装包更新
        _ytUpdatePlugin.update(
            apkUrl: "http://xx.xx.xx.xx:9999/minio/sys/app/Smart_M_1.5.2_1748243119.apk",
            listener: (status) {
              //下载进度
              if (status.maxSize != 0) circleValue = status.downSize / status.maxSize;
            statusMsg = status.toString();
        });

        取消下载
        _ytUpdatePlugin.cancel();


    5.跳转到浏览器，在浏览器进行更新
        _ytUpdatePlugin.jumpBrowser("https://www.pgyer.com/xxxx");
    

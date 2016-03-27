# 前言 #

本程序分两个部，需要分别配置。
  * 客户端是Java程序，用于在本地电脑建立代理服务，将本地程序的数据封装起来发送到服务端。
  * 服务端是一个PHP脚本，运行在PHP空间上，作用是接受客户端发送的数据并转发到目标地址。

# 服务端 #
服务端是个PHP脚本，所以要运行在PHP空间上。
  1. 到[项目下载页](http://code.google.com/p/webpage-tunnel/downloads/list)下载，解压出的“proxy.php”，你可以改成其它比较迷惑性的名字，如“helloworld.php”，上传到你的PHP空间上。
  1. 这个php的地址就是在下面在客户端设置的“代理网页地址”，如果你用浏览器访问就会跳到Google首页。
  1. 最好测试一下你的PHP空间是否满足运行需求，方法可看[服务端测试说明](http://code.google.com/p/webpage-tunnel/wiki/ServerTest)。

https://images2-focus-opensocial.googleusercontent.com/gadgets/proxy?url=http%3A%2F%2Fdl.dropbox.com%2Fu%2F2992664%2Fwiki%2Fwebpage-tunnel.png&container=focus&gadget=a&no_expand=1&resize_h=0&rewriteMime=image%2F*

# 客户端 #

客户端由于是Java程序，所以需要Java虚拟机，准确来说是Java运行时环境（Java Runtime Environment，简称JRE）。
  1. 未安装的话，Window用户可以到[Java官网](http://www.java.com)下载安装JRE，Linux用户可搜索发行版的软件仓库。
  1. 然后到[项目下载页](http://code.google.com/p/webpage-tunnel/downloads/list)下载zip压缩包，并解压，一般装好JRE后双击jar文件就能运行了，如果不是
    * Window用户运行webpage-tunnel.exe
    * Linux用户运行webpage-tunnel.sh
  1. 在“代理网页地址”里填上你上传的php文件地址，在“本地代理端口”里填上开启代理服务的端口，默认使用6050。
  1. 点击“启动”按钮即可启动代理服务，在你的浏览器你把HTTP代理设置为“127.0.0.1:6050”，即可使用代理。
  1. 推荐使用加密传输，牺牲一点觉察不到的速度来保证数据安全，注意加密传输为简单的对称加密，也可以阅读[高级设置](http://code.google.com/p/webpage-tunnel/wiki/AdvancedSetting)里的说明。
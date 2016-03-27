# 高级设置 #

一般来说，高级设置保持默认即可，修改后需要重启程序来生效。

https://images2-focus-opensocial.googleusercontent.com/gadgets/proxy?url=http%3A%2F%2Fdl.dropbox.com%2Fu%2F2992664%2Fwiki%2Fwebpage-tunnel_setting.png&container=focus&gadget=a&no_expand=1&resize_h=0&rewriteMime=image%2F*

  * 常规 两个选项作用跟字面意思一样。
  * 代理 如果你的电脑通过其它代理来上网，可以在这里勾选启用这个功能，填写代理所需信息，否则不要启用。
  * HTTPS 这里两个端口用作程序的内部通信，用于支持HTTPS协议，如果出现这两个端口已被使用问题，请修改成其它未占用端口，否则就不用修改。

# 命令行参数 #

程序接受命令行参数，终端运行
```
java -jar webpage-tunnel.jar [-c | -n]
```

  * -c 以控制台方式启动，按“Ctrl+C”来退出。
  * -n 禁用HTTPS支持。
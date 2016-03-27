# Windows Mobile #

可以在[Mysaifu JVM](http://www2s.biglobe.ne.jp/~dat/java/project/jvm/download_en.html)下运行，但不支持HTTPS。

  1. 到Mysaifu JVM的[下载页](http://sourceforge.jp/projects/mysaifujvm/releases/)下载“jvm.0.4.7-bin.zip“，把解压得cab文件复制到手机上安装。
  1. 按照[使用说明](http://code.google.com/p/webpage-tunnel/wiki/Readme)在电脑上运行一下程序，设置好，测试代理是否可以用。
  1. 然后把“webpage-tunnel.jar”和“webpage-tunnel.cfg”复制到手机上，如记忆卡上的“webpage-tunnel”文件夹。
  1. 不支持自动联网，所以要随便找其它程序把手机连上网络，如Opera Mobile，GPRS和Wifi都可以。
  1. 运行Mysaifu JVM，在主界面按下图设置
    * “Type”选“JAR file”，并选择“webpage-tunnel.jar”
    * 勾选“Show console”
> > https://images2-focus-opensocial.googleusercontent.com/gadgets/proxy?url=http%3A%2F%2Fdl.dropbox.com%2Fu%2F2992664%2Fwiki%2FMysaifu_JVM.png&container=focus&gadget=a&no_expand=1&resize_h=0&rewriteMime=image%2F*
  1. 点击“Options...”作进一步设置。这里有两个2个地方要填，
    * “Command line arguments”填上“-c -n”
    * “Current directory”填上“webpage-tunnel.jar“所在的文件夹路径，这里“Browser...”好像失效了，直接填写吧，如“\Storage Card\webpage-tunnel\“
> > https://images2-focus-opensocial.googleusercontent.com/gadgets/proxy?url=http%3A%2F%2Fdl.dropbox.com%2Fu%2F2992664%2Fwiki%2FMysaifu_JVM_general.png&container=focus&gadget=a&no_expand=1&resize_h=0&rewriteMime=image%2F*
  1. 切换到“Memory”选项卡，这里的设置数值看你的手机性能
    * “Max heap size”改成8M
    * “Java stack size”改成64KB
    * “Native stack size”改成320KB
> > https://images2-focus-opensocial.googleusercontent.com/gadgets/proxy?url=http%3A%2F%2Fdl.dropbox.com%2Fu%2F2992664%2Fwiki%2FMysaifu_JVM_Memory.png&container=focus&gadget=a&no_expand=1&resize_h=0&rewriteMime=image%2F*
  1. 点击OK按钮回到主界面，点击“Execute”运行程序，稍等一会，在控制台看到以下输出即代表运行成功了。
```
Running in console mode
>>> Initializing settings...
>>> Starting proxy server...
Wed Jan 20 18:28:19 CST 2010 Encription Enabled ? true
>>> Now running...
>>> Press Ctrl+C to exit.
```
  1. 然后就不用管Mysaifu JVM了，在其它联网程序把代理为“127.0.0.1:6050”就能使用了,退出直接终止Mysaifu JVM即可。
  1. Mysaifu JVM会自动保存设置的，下次运行直接在“Recent list”选择“webpage-tunnel.jar“，然后点击“Execute”就能运行了。

Opera Mobile的支持代理的，但在没有提供界面设置
  1. 打开“about:config”，搜“http”，找到“Proxy”区域
  1. “HTTP server“填”127.0.0.1:6050“
  1. 勾选“Use HTTP”
  1. 点击“Save”保存
这样切换起来比较麻烦，但Opera Mobile还支持pac文件的，可以把Foxyproxy的列表转换一份。
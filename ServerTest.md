# PHP版 #

在你的上传php地址后加上“?test=1”，即可返回测试结果，是利用Google来做测试网网站的。
为了避免php可能不支持非ASCII字符，所以不翻译了。
  * 返回结果出现下面一行表示测试通过，恭喜你了，php空间支持做代理需求功能。
```
Test Passed: Your server is perfect to run
```
  * 下面的几行都是表示测试不通过，不能做代理，可能是php空间商做了功能限制。
```
Test Failed: Could not open input stream
```
```
Test Failed: Could not connect to google.com:80 allow_open must be on in php.ini
```
```
Test Failed: Could not connect to google.com:443 allow_open must be on in php.ini
SSL support not enable : Openssl may not be installed . fsockopen failed for ssl://
```
> India Web Proxy的作者给出下面的php.ini配置文件建议，如果你有权限就参照修改，没权限只能换个空间咯。
```
register_globals = On
allow_url_fopen = On
expose_php = Off
variables_order = "EGPCS"
extension_dir = ./
upload_tmp_dir = /tmp
precision = 12
max_input_time = 300
max_execution_time = 300
post_max_size = 8M
upload_max_filesize =8M
output_buffering = 4096
expose_php = On
```
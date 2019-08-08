# MailSender
### 简介
MailSender基于[JavaMail for Android](https://javaee.github.io/javamail/Android)开发，旨在帮助开发者在Android平台快速实现邮件发送
### MailSender 特点
* Kotlin开发，兼容Java项目
* 支持发送纯文本、html内容邮件发送
* 支持发送带附件邮件
* 支持抄送，密送

### 效果图
<img src="https://github.com/teprinciple/MailSender/blob/master/img/html.png" width="800">


<img src="https://github.com/teprinciple/MailSender/blob/master/img/spannablestring.png" width="800">

### 集成
```
repositories {
   jcenter()    
}

implementation 'com.teprinciple:mailsender:1.1.0'
```
### 使用
#### kotlin使用
```
// 创建邮箱
 val mail = Mail().apply {
    mailServerHost = "smtp.qq.com"
    mailServerPort = "587"
    fromAddress = "xxxxxxxx@foxmail.com"
    password = "xxxxxxxx"
    toAddress = arrayListOf("xxxxxxxx@qq.com")
    subject = "MailSender"
    content = "MailSender Android快速实现发送邮件"
    attachFiles = arrayListOf(file)
 }
 
 // 发送邮箱
 MailSender.getInstance().sendMail(mail)
```

#### Java使用
```
// 创建邮箱
Mail mail = new Mail();
mail.mailServerHost = "smtp.qq.com";
mail.mailServerPort = "587";
mail.fromAddress = "xxxxxxxx@foxmail.com";
mail.password = "xxxxxxxx";
mail.toAddress = arrayListOf("xxxxxxxx@qq.com");
mail.subject = "MailSender";
mail.content = "MailSender Android快速实现发送邮件";
mail.attachFiles = arrayListOf(file);

 // 发送邮箱
 MailSender.getInstance().sendMail(mail);
```

#### 发送Html、SpannableString格式的邮件

只需将Mail类中的content，换成html或者SpannableString
```
// html 内容的邮件
content = 
    """
        <p1 style = "color: red">MailSender</p1><br/>
        <p1 style = "color: blue">Android快速实现发送邮件</p1><br/>
        <p1 style = "color: blue">https://github.com/teprinciple/MailSender</p1><br/>
        <p6 style = "color: gray">这是html内容的邮件</p1><br/>
        <img src="https://avatars2.githubusercontent.com/u/19629464?s=460&v=4">
    """
    
//SpannableString内容的邮件
content = SpanUtils(this@MainActivity)
    .appendLine("MailSender").setFontSize(28, true).setForegroundColor(Color.RED)
    .appendLine("Android快速实现发送邮件")
    .appendLine("https://github.com/teprinciple/MailSender").setForegroundColor(Color.BLUE)
    .appendLine("这是SpannableString内容的邮件").setForegroundColor(Color.parseColor("#efefef")).setFontSize(12, true)
    .create()    
    
```

#### Mail说明
| 属性             | 说明                               | 是否必须 |
|:-------------- |:------------------------------------ |:------ |
| mailServerHost| 发件邮箱服务器         | true   |
| mailServerPort | 发件邮箱服务器端口      | true   |
| fromAddress    | 发件邮箱地址     | true   |
| password     | 发件箱授权码（密码）     | true   |
| toAddress     | 直接收件人邮箱    | true   |
| ccAddress     | 抄送者邮箱     | false   |
| bccAddress     | 密送者邮箱     | false   |
| subject     | 邮件主题     | false   |
| content     | 邮件内容     | false   |
| attachFiles     | 附件     | false   |

### Demo体验
<img src="https://github.com/teprinciple/MailSender/blob/master/img/demo.png" width="220">

#### 关于授权码的获取
下面是qq邮箱授权码获取
[怎样获取授权码](https://service.mail.qq.com/cgi-bin/help?subtype=1&&id=28&&no=1001256)
![](http://upload-images.jianshu.io/upload_images/2368611-58043f5d5d0b6137.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

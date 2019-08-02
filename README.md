# MailSender
### 简介
MailSender基于[JavaMail for Android](https://javaee.github.io/javamail/Android)开发，旨在帮助开发者在Android平台快速实现邮件发送
### MailSender 特点
* Kotlin开发，兼容Java项目
* 支持发送纯文本、html内容邮件发送
* 支持发送带附件邮件
* 支持抄送，密送

### 集成
```
repositories {
   jcenter()    
}

implementation 'com.teprinciple:mailsender:1.0.0'
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
    subject = "测试邮件"
    content = "这是一个测试邮件"
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
mail.subject = "测试邮件";
mail.content = "这是一个测试邮件";
mail.attachFiles = arrayListOf(file);

 // 发送邮箱
 MailSender.getInstance().sendMail(mail);
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

#### 特别注意：一定要打开邮箱POP3/IMAP/SMTP服务，获取邮箱授权码，不然认证会失败
![](http://upload-images.jianshu.io/upload_images/2368611-58043f5d5d0b6137.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
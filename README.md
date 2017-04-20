# SendMailDemo
Android快速实现发送邮件

######文章地址：http://www.jianshu.com/p/f940ebcae899
####前言
现在一般很少有用Android原生app发送邮件的需求，但是前段时间公司项目需要在Android app 内部发送邮件，于是就在网上收集资料并整理了一个Demo。虽然最后这个需求被减掉了，但是我这里还是把Demo的内容给大家分享一下。

####第一步、导入第三方jar包
Android实现发送邮件，首先需要依赖additional.jar、mail.jar和activation.jar这3个jar包。Google提供了下载地址：https://code.google.com/archive/p/javamail-android/downloads 。下载后添加到依赖（这里我就不详细说明了）。

####第二步、创建相关类
######1、创建MailInfo类，来代表一个即将被发送的邮件

```
package com.shidian.mail;

import java.util.Properties;

public class MailInfo {

	private String mailServerHost;// 发送邮件的服务器的IP
	private String mailServerPort;// 发送邮件的服务器的端口
	private String fromAddress;// 邮件发送者的地址
	private String toAddress;	// 邮件接收者的地址
	private String userName;// 登陆邮件发送服务器的用户名
	private String password;// 登陆邮件发送服务器的密码
	private boolean validate = true;// 是否需要身份验证
	private String subject;// 邮件主题
	private String content;// 邮件的文本内容
	private String[] attachFileNames;// 邮件附件的文件名

	/**
	 * 获得邮件会话属性
	 */
	public Properties getProperties() {
		Properties p = new Properties();
		p.put("mail.smtp.host", this.mailServerHost);
		p.put("mail.smtp.port", this.mailServerPort);
		p.put("mail.smtp.auth", validate ? "true" : "false");
		return p;
	}

	public String getMailServerHost() {
		return mailServerHost;
	}

	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}

	public String getMailServerPort() {
		return mailServerPort;
	}

	public void setMailServerPort(String mailServerPort) {
		this.mailServerPort = mailServerPort;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public String[] getAttachFileNames() {
		return attachFileNames;
	}

	public void setAttachFileNames(String[] fileNames) {
		this.attachFileNames = fileNames;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String textContent) {
		this.content = textContent;
	}
}

```

######2、创建认证类MyAuthenticator
```
package com.shidian.mail;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
public class MyAuthenticator extends Authenticator {
	String userName = null;
	String password = null;
	public MyAuthenticator() {
	}
	public MyAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}
}
```
######3、创建邮件发送类MailSender
```
package com.shidian.mail;

import android.util.Log;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * 发送器
 */
public class MailSender {
	/**
	 * 以文本格式发送邮件
	 * @param mailInfo 待发送的邮件的信息
	 */
	public boolean sendTextMail(final MailInfo mailInfo) {

		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);

//		Session sendMailSession = Session.getInstance(pro, new Authenticator() {
//			@Override
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(mailInfo.getUserName(),mailInfo.getPassword());
//			}
//		});

		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());

			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			mailMessage.setText(mailContent);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 以HTML格式发送邮件
	 * @param mailInfo 待发送的邮件信息
	 */
	public static boolean sendHtmlMail(MailInfo mailInfo) {
		// 判断是否需要身份认证
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		// 如果需要身份认证，则创建一个密码验证器
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
		}
		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address to = new InternetAddress(mailInfo.getToAddress());
			// Message.RecipientType.TO属性表示接收者的类型为TO
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// 将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);
			// 发送邮件
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}


	/**
	 * 发送带附件的邮件
	 * @param info
	 * @return
     */
	public boolean sendFileMail(MailInfo info, File file){
		Message attachmentMail = createAttachmentMail(info,file);
		try {
			Transport.send(attachmentMail);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 创建带有附件的邮件
	 * @return
	 */
	private Message createAttachmentMail(final MailInfo info, File file) {
		//创建邮件
		MimeMessage message = null;
		Properties pro = info.getProperties();
		try {

			Session sendMailSession = Session.getInstance(pro, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(info.getUserName(),info.getPassword());
				}
			});

			message = new MimeMessage(sendMailSession);
			// 设置邮件的基本信息
			//创建邮件发送者地址
			Address from = new InternetAddress(info.getFromAddress());
			//设置邮件消息的发送者
			message.setFrom(from);
			//创建邮件的接受者地址，并设置到邮件消息中
			Address to = new InternetAddress(info.getToAddress());
			//设置邮件消息的接受者, Message.RecipientType.TO属性表示接收者的类型为TO
			message.setRecipient(Message.RecipientType.TO, to);
			//邮件标题
			message.setSubject(info.getSubject());

			// 创建邮件正文，为了避免邮件正文中文乱码问题，需要使用CharSet=UTF-8指明字符编码
			MimeBodyPart text = new MimeBodyPart();
			text.setContent(info.getContent(), "text/html;charset=UTF-8");

			// 创建容器描述数据关系
			MimeMultipart mp = new MimeMultipart();
			mp.addBodyPart(text);
				// 创建邮件附件
				MimeBodyPart attach = new MimeBodyPart();

			FileDataSource ds = new FileDataSource(file);
			DataHandler dh = new DataHandler(ds);
				attach.setDataHandler(dh);
				attach.setFileName(MimeUtility.encodeText(dh.getName()));
				mp.addBodyPart(attach);
			mp.setSubType("mixed");
			message.setContent(mp);
			message.saveChanges();

		} catch (Exception e) {
			Log.e("TAG", "创建带附件的邮件失败");
			e.printStackTrace();
		}
		// 返回生成的邮件
		return message;
	}
}

```
####第三步、发送邮件

这里举例发送文本邮件和带附件的邮件
```
package teprinciple.yang.sendmaildemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.shidian.mail.SendMailUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.toAddEt);
    }


    public void senTextMail(View view) {
        SendMailUtil.send(editText.getText().toString());
    }

    public void sendFileMail(View view) {

        File file = new File(Environment.getExternalStorageDirectory()+File.separator+"test.txt");
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            String str = "hello world";
            byte[] data = str.getBytes();
            os.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if (os != null)os.close();
            } catch (IOException e) {
            }
        }
        SendMailUtil.send(file,editText.getText().toString());
    }
}

```

下面是发送邮件的SendMailUtil

```
package com.shidian.mail;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by Administrator on 2017/4/10.
 */

public class SendMailUtil {
    //qq
    private static final String HOST = "smtp.qq.com";
    private static final String PORT = "587";
    private static final String FROM_ADD = "teprinciple@foxmail.com";
    private static final String FROM_PSW = "lfrlpganzjrwbeci";

//    //163
//    private static final String HOST = "smtp.163.com";
//    private static final String PORT = "465"; //或者465  994
//    private static final String FROM_ADD = "teprinciple@163.com";
//    private static final String FROM_PSW = "teprinciple163";
////    private static final String TO_ADD = "2584770373@qq.com";

    public static void send(final File file,String toAdd){
        final MailInfo mailInfo = creatMail(toAdd);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendFileMail(mailInfo,file);
            }
        }).start();
    }

    public static void send(String toAdd){
        final MailInfo mailInfo = creatMail(toAdd);
        final MailSender sms = new MailSender();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sms.sendTextMail(mailInfo);
            }
        }).start();
    }

    @NonNull
    private static MailInfo creatMail(String toAdd) {
        final MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost(HOST);
        mailInfo.setMailServerPort(PORT);
        mailInfo.setValidate(true);
        mailInfo.setUserName(FROM_ADD); // 你的邮箱地址
        mailInfo.setPassword(FROM_PSW);// 您的邮箱密码
        mailInfo.setFromAddress(FROM_ADD); // 发送的邮箱
        mailInfo.setToAddress(toAdd); // 发到哪个邮件去
        mailInfo.setSubject("Hello"); // 邮件主题
        mailInfo.setContent("Android 测试"); // 邮件文本
        return mailInfo;
    }
}

```





package com.teprinciple.mailsender

import android.text.Html
import android.text.Spannable
import android.text.Spanned
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.internet.*

/**
 * desc: 邮件帮助类
 * time: 2019/8/1
 * @author teprinciple
 */
object MailUtil {

    /**
     * 创建邮件
     */
    fun createMailMessage(mail: Mail): MimeMessage {
        val properties = Properties()
        properties["mail.smtp.host"] = mail.mailServerHost
        properties["mail.smtp.port"] = mail.mailServerPort
        properties["mail.smtp.auth"] = "true"

        val authenticator = MailAuthenticator(mail.fromAddress, mail.password)
        val session = Session.getDefaultInstance(properties, authenticator)

        return MimeMessage(session).apply {

            // 设置发件箱
            setFrom(InternetAddress(mail.fromAddress))

            // 设置直接接收者收件箱
            val toAddress = mail.toAddress.map {
                InternetAddress(it)
            }.toTypedArray()
            setRecipients(Message.RecipientType.TO, toAddress)

            // 设置抄送者收件箱
            val ccAddress = mail.ccAddress.map {
                InternetAddress(it)
            }.toTypedArray()
            setRecipients(Message.RecipientType.CC, ccAddress)

            // 设置密送者收件箱
            val bccAddress = mail.bccAddress.map {
                InternetAddress(it)
            }.toTypedArray()
            setRecipients(Message.RecipientType.BCC, bccAddress)

            // 邮件主题
            subject = mail.subject

            // 邮件内容
            val contentPart = MimeMultipart()

            // 邮件正文
            val textBodyPart = MimeBodyPart()
            if (mail.content is Spanned){
                textBodyPart.setContent(Html.toHtml(mail.content as Spanned), "text/html;charset=UTF-8")
            }else{
                textBodyPart.setContent(mail.content, "text/html;charset=UTF-8")
            }
            contentPart.addBodyPart(textBodyPart)

            // 邮件附件
            mail.attachFiles.forEach {
                val fileBodyPart = MimeBodyPart()
                val ds = FileDataSource(it)
                val dh = DataHandler(ds)
                fileBodyPart.dataHandler = dh
                fileBodyPart.fileName = MimeUtility.encodeText(dh.name)
                contentPart.addBodyPart(fileBodyPart)
            }
            contentPart.setSubType("mixed")
            setContent(contentPart)
            saveChanges()
        }
    }

    /**
     * 发件箱auth校验
     */
    class MailAuthenticator(username: String?, private var password: String?) : Authenticator() {
        private var userName: String? = username
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(userName, password)
        }
    }
}
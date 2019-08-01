package com.shidian.mail

import android.util.Log

import java.io.File
import java.util.Date
import java.util.Properties

import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Address
import javax.mail.Authenticator
import javax.mail.BodyPart
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Multipart
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.internet.MimeUtility
import kotlin.concurrent.thread


object MailSender {

    fun sendTextMail(mailInfo: MailInfo): Boolean {

        var authenticator: MyAuthenticator? = null
        val pro = mailInfo.properties
        if (mailInfo.isValidate) {
            authenticator = MyAuthenticator(mailInfo.userName, mailInfo.password)
        }
        val sendMailSession = Session.getDefaultInstance(pro, authenticator)

        //		Session sendMailSession = Session.getInstance(pro, new Authenticator() {
        //			@Override
        //			protected PasswordAuthentication getPasswordAuthentication() {
        //				return new PasswordAuthentication(mailInfo.getUserName(),mailInfo.getPassword());
        //			}
        //		});

        try {
            val mailMessage = MimeMessage(sendMailSession)
            val from = InternetAddress(mailInfo.fromAddress!!)
            mailMessage.setFrom(from)
            val to = InternetAddress(mailInfo.toAddress!!)
            mailMessage.setRecipient(Message.RecipientType.TO, to)
            mailMessage.subject = mailInfo.subject
            mailMessage.sentDate = Date()

            val mailContent = mailInfo.content
            mailMessage.setText(mailContent)
            Transport.send(mailMessage)
            return true
        } catch (ex: MessagingException) {
            ex.printStackTrace()
        }

        return false
    }

    fun sendFileMail(info: MailInfo, file: File): Boolean {
        val attachmentMail = createAttachmentMail(info, file)
        try {
            Transport.send(attachmentMail!!)
            return true
        } catch (e: MessagingException) {
            e.printStackTrace()
            return false
        }

    }

    private fun createAttachmentMail(info: MailInfo, file: File): Message? {
        var message: MimeMessage? = null
        val pro = info.properties
        try {

            val sendMailSession = Session.getInstance(pro, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(info.userName, info.password)
                }
            })

            message = MimeMessage(sendMailSession)
            val from = InternetAddress(info.fromAddress!!)
            message.setFrom(from)
            val to = InternetAddress(info.toAddress!!)

            // to: 直接接受者 cc: 抄送  bcc：暗送
            message.setRecipient(Message.RecipientType.TO, to)
            message.subject = info.subject

            val text = MimeBodyPart()
            text.setContent(info.content, "text/html;charset=UTF-8")

            val mp = MimeMultipart()
            mp.addBodyPart(text)
            val attach = MimeBodyPart()

            val ds = FileDataSource(file)
            val dh = DataHandler(ds)
            attach.dataHandler = dh
            attach.fileName = MimeUtility.encodeText(dh.name)
            mp.addBodyPart(attach)

            mp.setSubType("mixed")
            message.setContent(mp)
            message.saveChanges()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return message
    }


    fun sendHtmlMail(mailInfo: MailInfo): Boolean {
        var authenticator: MyAuthenticator? = null
        val pro = mailInfo.properties
        if (mailInfo.isValidate) {
            authenticator = MyAuthenticator(mailInfo.userName, mailInfo.password)
        }
        val sendMailSession = Session.getDefaultInstance(pro, authenticator)
        try {
            val mailMessage = MimeMessage(sendMailSession)
            val from = InternetAddress(mailInfo.fromAddress!!)
            mailMessage.setFrom(from)
            val to = InternetAddress(mailInfo.toAddress!!)
            mailMessage.setRecipient(Message.RecipientType.TO, to)
            mailMessage.subject = mailInfo.subject
            mailMessage.sentDate = Date()
            val mainPart = MimeMultipart()
            val html = MimeBodyPart()
            html.setContent(mailInfo.content, "text/html; charset=utf-8")
            mainPart.addBodyPart(html)
            mailMessage.setContent(mainPart)
            Transport.send(mailMessage)
            return true
        } catch (ex: MessagingException) {
            ex.printStackTrace()
        }

        return false
    }

    @JvmStatic
    fun getInstance() = this

    fun sendMail(mail: Mail) {
        val properties = Properties()
        properties["mail.smtp.host"] = mail.mailServerHost
        properties["mail.smtp.port"] = mail.mailServerPort
        properties["mail.smtp.auth"] = "true"

        val authenticator = MyAuthenticator(mail.fromAddress, mail.password)
        val session = Session.getDefaultInstance(properties, authenticator)

        val message = MimeMessage(session).apply {

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
            textBodyPart.setContent(mail.content, "text/html;charset=UTF-8")
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

        Transport.send(message)
    }
}

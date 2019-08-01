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


class MailSender {

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

    companion object {


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
    }

}

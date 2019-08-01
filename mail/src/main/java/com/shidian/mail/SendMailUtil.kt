package com.shidian.mail

import java.io.File

/**
 * Created by Administrator on 2017/4/10.
 */

object SendMailUtil {

    private val HOST = "smtp.qq.com"
    private val PORT = "587"
    private val FROM_ADD = "teprinciple@foxmail.com"
    private val FROM_PSW = "gnlcupcfxifwbdhi"

    fun send(file: File, toAdd: String) {
        val mailInfo = creatMail(toAdd)
        val sms = MailSender()
        Thread(Runnable { sms.sendFileMail(mailInfo, file) }).start()
    }

    fun send(toAdd: String) {
        val mailInfo = creatMail(toAdd)
        val sms = MailSender()
        Thread(Runnable { sms.sendTextMail(mailInfo) }).start()
    }

    private fun creatMail(toAdd: String): MailInfo {
        val mailInfo = MailInfo()
        mailInfo.mailServerHost = HOST
        mailInfo.mailServerPort = PORT
        mailInfo.isValidate = true
        mailInfo.userName = FROM_ADD // 你的邮箱地址
        mailInfo.password = FROM_PSW// 您的邮箱密码
        mailInfo.fromAddress = FROM_ADD // 发送的邮箱
        mailInfo.toAddress = toAdd // 发到哪个邮件去
        mailInfo.subject = "Hello" // 邮件主题
        mailInfo.content = "Android 测试" // 邮件文本
        return mailInfo
    }

}

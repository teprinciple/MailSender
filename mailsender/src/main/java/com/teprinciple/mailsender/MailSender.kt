package com.teprinciple.mailsender

import javax.mail.Transport

/**
 * 邮件发送器
 */
object MailSender {

    /**
     * 获取单例
     */
    @JvmStatic
    fun getInstance() = this

    /**
     * 发送邮件
     */
    fun sendMail(mail: Mail, onMailSendListener: OnMailSendListener? = null) {
        runCatching {
            Transport.send(MailUtil.createMailMessage(mail))
            onMailSendListener?.onSuccess()
        }.onFailure {
            onMailSendListener?.onError(it.message)
        }
    }

    /**
     * 发送回调
     */
    interface OnMailSendListener {
        fun onSuccess()
        fun onError(message: String?)
    }
}

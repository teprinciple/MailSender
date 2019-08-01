package com.teprinciple.mailsender

import javax.mail.Transport

object MailSender {

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

    interface OnMailSendListener {
        fun onSuccess()
        fun onError(message: String?)
    }
}

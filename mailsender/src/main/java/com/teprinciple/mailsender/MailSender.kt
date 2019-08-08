package com.teprinciple.mailsender

import android.os.Handler
import android.os.Looper
import android.util.Log
import javax.mail.Transport
import kotlin.concurrent.thread

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
        thread {
            runCatching {
                Transport.send(MailUtil.createMailMessage(mail))
                onMailSendListener?.onSuccess()
            }.onFailure {
                Log.e("MailSender", it.message)
                onMailSendListener?.onError(it.message)
            }
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

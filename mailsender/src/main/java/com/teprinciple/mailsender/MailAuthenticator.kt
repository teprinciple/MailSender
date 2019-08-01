package com.teprinciple.mailsender

import javax.mail.Authenticator
import javax.mail.PasswordAuthentication

/**
 * 发件箱auth校验
 */
class MailAuthenticator(username: String?, private var password: String?) : Authenticator() {
    private var userName: String? = username
    override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(userName, password)
    }
}
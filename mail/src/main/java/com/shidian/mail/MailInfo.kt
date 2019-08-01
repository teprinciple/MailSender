package com.shidian.mail

import java.util.Properties

class MailInfo {

    var mailServerHost: String? = null
    var mailServerPort: String? = null
    var fromAddress: String? = null
    var toAddress: String? = null
    var userName: String? = null
    var password: String? = null
    var isValidate = true
    var subject: String? = null
    var content: String? = null
    var attachFileNames: Array<String>? = null

    val properties: Properties
        get() {
            val p = Properties()
            p["mail.smtp.host"] = this.mailServerHost
            p["mail.smtp.port"] = this.mailServerPort
            p["mail.smtp.auth"] = if (isValidate) "true" else "false"
            return p
        }
}

package com.shidian.mail

import java.io.File

/**
 * desc: 邮件实体类
 * time: 2019/8/1
 * @author yk
 */
data class Mail(
    var mailServerHost: String = "", // 发件箱邮箱服务器地址
    var mailServerPort: String = "", // 发件箱邮箱服务器端口
    var fromAddress: String = "", // 发件箱
    var password: String = "", // 发件箱授权码（密码）

    var toAddress: ArrayList<String> = ArrayList(), // 直接收件人邮箱
    var ccAddress: ArrayList<String> = ArrayList(), // 抄送者邮箱
    var bccAddress: ArrayList<String> = ArrayList(), // 密送者邮箱

    var subject: String = "",  // 邮件主题
    var content: String? = "", // 邮件内容
    var contentType: String = "", // 邮件类型
    var attachFiles: ArrayList<File> = ArrayList() // 附件
)
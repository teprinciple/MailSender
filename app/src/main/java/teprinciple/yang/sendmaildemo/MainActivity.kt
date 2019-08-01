package teprinciple.yang.sendmaildemo

import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.teprinciple.mailsender.Mail
import com.teprinciple.mailsender.MailSender

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class MainActivity : AppCompatActivity() {

    private var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText = findViewById(R.id.toAddEt) as EditText
    }


    private val mail by lazy {
        Mail().apply {
            mailServerHost = "smtp.qq.com"
            mailServerPort = "587"
            fromAddress = "teprinciple@foxmail.com"
            password = "gnlcupcfxifwbdhi"
            toAddress = arrayListOf("2584770373@qq.com")
            subject = "测试邮件"
//            content = "这是一个测试邮件"

            content = """
                <html>
<head>
	<title></title>
</head>
<body>
	<p1 style = "color: red">这是Html内容的邮件</p1><br/>
	<img src="https://www.baidu.com/img/bd_logo1.png?where=super">
</body>
</html>
            """.trimIndent()
        }
    }

    fun senTextMail(view: View) {
        //SendMailUtil.send(editText!!.text.toString())

        val file = File(Environment.getExternalStorageDirectory().toString() + File.separator + "test.txt")
        var os: OutputStream? = null
        try {
            os = FileOutputStream(file)
            val str = "hello world"
            val data = str.toByteArray()
            os.write(data)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                os?.close()
            } catch (e: IOException) {
            }
        }

        mail.attachFiles = arrayListOf(file)
        MailSender.getInstance().sendMail(mail)
    }
}

package teprinciple.yang.sendmaildemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.teprinciple.mailsender.Mail
import com.teprinciple.mailsender.MailSender
import java.io.*

class MainActivity : AppCompatActivity() {

    private var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText = findViewById(R.id.toAddEt)
    }


    private val mail by lazy {
        Mail().apply {
            mailServerHost = "smtp.qq.com"
            mailServerPort = "587"
            fromAddress = "xxx@foxmail.com" // 替换成你的发件箱
            password = "xxxx" // 替换成你的发件箱授权码
            toAddress = arrayListOf("xxxx@qq.com") // 替换成你的收件箱

            subject = "MailSender测试邮件"
//            content = "这是一个测试邮件"
            content = """
                <p1 style = "color: red">MailSender</p1><br/>
                <p1 style = "color: blue">Android快速实现发送邮件</p1><br/>
                <p1 style = "color: blue">https://github.com/teprinciple/MailSender</p1><br/>
	            <img src="https://avatars2.githubusercontent.com/u/19629464?s=460&v=4">
            """
        }
    }

    fun senTextMail(view: View) {

        val file = File(getExternalFilesDir("file").absolutePath + File.separator + "test.txt")
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

        MailSender.getInstance().sendMail(mail,object : MailSender.OnMailSendListener{
            override fun onSuccess() {
                //Toast.makeText(this@MainActivity,"发送成功",Toast.LENGTH_SHORT).show()
            }

            override fun onError(message: String?) {
                //Toast.makeText(this@MainActivity,"发送失败: $message",Toast.LENGTH_SHORT).show()
            }
        })
    }
}

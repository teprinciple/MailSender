package teprinciple.yang.sendmaildemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.shidian.mail.SendMailUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.toAddEt);
    }


    public void senTextMail(View view) {
        SendMailUtil.send(editText.getText().toString());
    }

    public void sendFileMail(View view) {

        File file = new File(Environment.getExternalStorageDirectory()+File.separator+"test.txt");
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            String str = "hello world";
            byte[] data = str.getBytes();
            os.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if (os != null)os.close();
            } catch (IOException e) {
            }
        }
        SendMailUtil.send(file,editText.getText().toString());
    }




}

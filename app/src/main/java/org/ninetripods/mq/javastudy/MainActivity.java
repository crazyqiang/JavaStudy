package org.ninetripods.mq.javastudy;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.javastudy.Annotation.AnnotationInfo;
import com.suyun.aopermission.annotation.CompileAnnotation;
import com.suyun.aopermission.annotation.CompileAutoAnnotation;

@CompileAutoAnnotation(value = 100)
public class MainActivity extends AppCompatActivity {

    @CompileAnnotation(value = 200)
    private TextView tv_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_text = (TextView) findViewById(R.id.tv_text);
        tv_text.setText("请在下面的JavaLib包中直接运行Java程序看效果");
    }

    /**
     * 注解模拟请求权限
     */
    @AnnotationInfo(value = {Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA}, requestCode = 10)
    public void requestPermission() {

    }


}

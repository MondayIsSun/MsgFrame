package mr.lmd.personal.handler_09;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity {

    private TextView textView;

    private Handler handler = new Handler() {
        //配合method_1的handleMessage()
        //@Override
        //public void handleMessage(Message msg) {
        //
        //}

        //配合method_2的handleMessage() --->
        //自己要理解清楚哪个handler是出于UI线程
        //哪个handler是出于子线程
        //@Override
        //public void handleMessage(Message msg) {
        //    textView.setText("子线程通知UI线程更新UI");
        //}
    };

    private void method_1() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.setText("handler.post()实现更新UI");
            }
        });
    }

    private void method_2() {
        handler.sendEmptyMessage(1);
    }

    private void method_3() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText("runOnUiThread()方式更新UI");
            }
        });
    }

    private void method_4() {
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText("通过UI控件的post方式更新UI");
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //开始更新UI
                //method_1();
                //method_2();
                //method_3();
                method_4();
            }
        }.start();
    }

}

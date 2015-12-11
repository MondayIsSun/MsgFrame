package mr.lmd.personal.handler_04;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    /**
     * 另一种实现Handler在新的独立线程当中处理消息的方式
     */

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println("1-Handler------->" + Thread.currentThread().getId() + " , " + Thread.currentThread().getName());
        }
    };

    class MyThread extends Thread {

        public Handler handler;

        @Override
        public void run() {
            Looper.prepare();
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    System.out.println("2-MyThread------->" + Thread.currentThread().getId() + " , " + Thread.currentThread().getName());
                }
            };
            Looper.loop();
        }
    }

    private MyThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("0-MainActivity------->" + Thread.currentThread().getId() + " , " + Thread.currentThread().getName());

        //为什么必须提到这里才能运行？
        thread = new MyThread();
        thread.start();

        Button btnNewThread = (Button) findViewById(R.id.btnNewThread);
        btnNewThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这样是错误的，必须提到外面去？
                //thread = new MyThread();
                //thread.start();
                thread.handler.sendEmptyMessage(1);
                //thread.handler.sendMessage(new Message());
                //Looper.loop();
                //ThreadLocal set() get() ---> 当前的线程为key,而value当然是你自己设置的啦
            }
        });

        Button btnMainThread = (Button) findViewById(R.id.btnMainThread);
        btnMainThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(1);
            }
        });
    }
}

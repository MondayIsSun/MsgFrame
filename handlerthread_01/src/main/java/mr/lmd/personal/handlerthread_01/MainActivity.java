package mr.lmd.personal.handlerthread_01;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

/**
 * handler在默认的情况下是与Activity一起处于主线程的
 * 注意在handler当中的线程启动方式不是通过start方法，而是直接调用线程的run方法
 * <p/>
 * System.out﹕ Handler: id--->1 || name--->main
 * System.out﹕ MainActivity: id--->1 || name--->main
 * 证明了在handler当中启动的线程确实是运行在主线程当中的，也就是说就一个线程
 * 本质原因就是因为handler当中其实没有启动一个线程而是直接运行了线程类的run方法
 * 注意要真正地启动一个线程确实要调用线程类的start()方法
 */

public class MainActivity extends Activity {

    private Button btnStart;
    private Handler handler = new Handler();

    Runnable r = new Runnable() {
        @Override
        public void run() {
            //获取handler当中启动的线程的信息
            System.out.println("Handler: id--->" + Thread.currentThread().getId() + " || name--->" + Thread.currentThread().getName());
            //System.out﹕ Handler: id--->1 || name--->main
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取Activity所处的主线程的信息
        System.out.println("MainActivity: id--->" + Thread.currentThread().getId() + " || name--->" + Thread.currentThread().getName());
        //System.out﹕ MainActivity: id--->1 || name--->main

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(r);
            }
        });

        //真正地在一个独立的线程当中运行
        Thread thread = new Thread(r);
        thread.start();
    }
}

package mr.lmd.personal.handler_05;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    //这里被注释掉的代码是一种没有使用HandlerThread的情况
    //而现在的代码是使用了HandlerThread的情况
    /*
    private Handler handler;

    class MyThread extends Thread {

        public Looper looper;

        @Override
        public void run() {
            Looper.prepare();
            looper = Looper.myLooper();
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    System.out.println("MyHandler ---> " + Thread.currentThread().getName());
                }
            };

            Looper.loop();

        }
    }
    */

    private HandlerThread handlerThread;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        MyThread thread = new MyThread();
        thread.start();
        */

        //多线程并发导致空指针问题
        //这段代码是会报错程 ---> 多线程并发 ---> 掌握好多线程编程思维 ---> 时间片
        /*
        handler = new Handler(thread.looper){
            @Override
            public void handleMessage(Message msg) {
                System.out.println(msg);
            }
        };
        handler.sendEmptyMessage(1);
        */

        //使用HandlerThread处理上面代码出现的多线程并发问题
        handlerThread = new HandlerThread("firstHandlerThread");
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                System.out.println("current thread ---> " + Thread.currentThread().getId() + "  ||  " + Thread.currentThread().getName());
            }
        };
        handler.sendEmptyMessage(1);



    }
}

package mr.lmd.personal.handler_03;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    //自己实现的Handler对象
    class MyHandler extends Handler {

        public MyHandler() {

        }

        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            //验证handleMessage所处的线程
            System.out.println("handleMessage的Thread : " + Thread.currentThread().getId() + " || " + Thread.currentThread().getName());

            Bundle b = msg.getData();
            int age = b.getInt("age");
            String name = b.getString("name");
            System.out.println("name="+name+"||"+"age="+age);
            System.out.println("HandleMessage");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("MainActivity");
        System.out.println("MainActivity所处的主线程 : " + Thread.currentThread().getId() + " || " + Thread.currentThread().getName());

        //HandlerThread继承了Thread
        //创建出HandlerThread的同时指定了这个线程的名字
        HandlerThread handlerThread = new HandlerThread("handler_thread");
        handlerThread.start();

        //handlerThread.start();这样就真正地启动了一个新线程了
        //获取到handlerThread.getLooper();其实是为了把当前的Handler对象绑定到这个线程当中
        //handler不是线程
        //looper的功能就是轮询消息队列
        //looper的底层实现其实就是一个死循环
        //创建自己实现的Handler对象的同时绑定looper
        MyHandler myHandler = new MyHandler(handlerThread.getLooper());
        Message message = myHandler.obtainMessage();
        Bundle b = new Bundle();
        b.putInt("age",20);
        b.putString("name","Marry");
        message.setData(b);
        //sentToTarget()的作用就是就是把这个消息发送给这个消息的父类(默认)，就是myHandler对象啦
        //message.setTarget(Handler handler);
        message.sendToTarget();

        //验证这样执行Runnable所处的线程
        myHandler.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("Runnable所处的线程 : " + Thread.currentThread().getId() + " || " + Thread.currentThread().getName());
            }
        });
    }
}

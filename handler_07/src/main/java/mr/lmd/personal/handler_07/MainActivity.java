package mr.lmd.personal.handler_07;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends Activity {

    //定义线程
    HandlerThread handlerThread = new HandlerThread("HandlerThread") {
        @Override
        public void run() {
            //这个线程里面的业务逻辑就在这里面实现
            System.out.println("HandlerThread ---> " + " ||  " + Thread.currentThread().getId() + " || " + Thread.currentThread().getName());
            super.run();
        }
    };

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    //定义handler
    //Android中使用Handler引发的内存泄露
    //注意这样的定义方式可能会导致内存泄漏
    //在Java中，非静态的内部类和匿名内部类都会隐式地持有其外部类的引用。静态的内部类不会持有外部类的引用
    /*
    android.os.Handler handler = new android.os.Handler(handlerThread.getLooper()) {
        @Override
        public void handleMessage(Message msg) {
            //
            System.out.println("Handler ---> " + msg.arg1 + " ||  " + Thread.currentThread().getId() + " || " + Thread.currentThread().getName());
        }
    };
    */

    //使用静态的内部类 或 把Handler的类代码放在单独的类文件当中  或 使用弱引用---> 这样就不会导致静态内部类持有外部类的引用了
    private static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //如果在这里面处理耗时的操作同样会导致UI线程的阻塞
            super.handleMessage(msg);
        }
    }

    Handler handler = new MyHandler();

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("MainActivity ---> " + Thread.currentThread().getId() + " || " + Thread.currentThread().getName());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void doClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
                handlerThread.start();
                Message msg = handler.obtainMessage();
                msg.arg1 = 1;
                handler.sendMessage(msg);
                break;
            default:
                break;
        }
    }

}

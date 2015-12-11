package mr.lmd.personal.handler_08;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        new Thread() {
            @Override
            public void run() {

                //这里演示一下一个常见的异常
                //java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
                //原因是：
                //去查看Handler的源码其实会发现Handler在创建出来的时候会去关联某个线程(一般是当前线程)的Looper
                //由于下面的创建Handler的代码在子线程当中创建，这个时候的子线程并没有去初始化它的的Looper
                //所以这个时候创建出的Handler对象没有办法去关联当前线程都Looper，所以就抛出了异常啦
                //Handler handler = new Handler();

                //上面异常的处理方式是
                //在子线程当中初始化自己的looper
                //这样创建出来的Handler就能实现自己去关联当前线程都Looper了
                Looper.prepare();
                Handler handler = new Handler();
                Looper.loop();

                //在非UI线程当中更新UI ---> 不让线程sleep(),直接下面这样更新是OK的
                //原因是ViewRootImp是在Activity的onResume()方法当中被创建的
                //由于onCreate()方法是比onResume()早调用，
                //所以这个地方就没有ViewRootImp，也就没有checkThread()的操作，也就是没有验证是否是当前线程，
                //所以这样可以更新UI
                //当时还是强烈建议不要这样更新UI
                //还是推荐使用Handler的机制去更新UI，之前涉及到的四种更新UI的方式其实底层都是使用了handler机制来更新UI的
                textView.setText("在非UI线程当中更新UI");

                try {
                    Thread.sleep(2000);
                    //在非UI线程当中更新UI ---> 让线程sleep(),直接下面这样更新却会报错
                    //setText() ---> checkForRelayout ---> invalidate() ---> ViewRootImp ---> checkThread()
                    //textView.setText("在非UI线程当中更新UI");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}

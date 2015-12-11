package mr.lmd.personal.handler_06;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


public class MainActivity extends Activity implements View.OnClickListener {

    class MyObj {
        String infor;
    }

    private Button btnSend, btnStop;

    //创建主线程的handler ---> 主线程给子线程发送消息
    //主线程当中的Handler,只要不给Handler绑定Looper就默认是在主线程的thread当中呀
    //所以呀，Handler是Handler，而Thread是Thread，二者是独立开的
    private Handler main_handler = new Handler() {

        //主线程当中的Handler处理消息的回调方法
        @Override
        public void handleMessage(Message msg) {
            System.out.println("Main Handler");


            msg = MainActivity.this.main_handler.obtainMessage();
            MyObj obj = new MyObj();
            obj.infor = "主线程给子线程发送的消息数据";
            msg.obj = obj;

            //主线程通过子线程的handler与子线程交互
            thread_handler.sendMessageDelayed(msg, 1000);
        }
    };

    //子线程当中的Handler
    private HandlerThread handlerThread = new HandlerThread("thread_handler");
    private Handler thread_handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(this);

        handlerThread.start();

        //创建子线程的handler ---> 实现在主线程当中与子线程交互
        thread_handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                System.out.println("handlerThread");
                msg = thread_handler.obtainMessage();

                //子线程通过主线程的handler和主线程交互
                main_handler.sendMessageDelayed(msg, 1000);
            }
        };
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                main_handler.sendEmptyMessage(1);
                break;
            case R.id.btnStop:
                //thread_handler.removeMessages(1);
                main_handler.removeMessages(1);
                break;
            default:
                break;
        }

    }
}

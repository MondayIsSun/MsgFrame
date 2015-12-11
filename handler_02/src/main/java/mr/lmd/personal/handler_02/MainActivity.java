package mr.lmd.personal.handler_02;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 看源码
 * 看IDE提供的注释
 */

public class MainActivity extends Activity {

    /*******************************************************************/

    //不断打印
    private Button btnPrint, btnStop;
    private Handler handler = new Handler();
    Runnable printThread = new Runnable() {
        @Override
        public void run() {
            System.out.println("run ---> " + Math.random());
            handler.postDelayed(printThread, 2000);
        }
    };

    /*******************************************************************/

    //加载进度条
    private Button btnLoad, btnStopLoad;
    private ProgressBar progressBar;


    private Handler loadBarHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressBar.setProgress(msg.arg1);
            loadBarHandler.post(loadBarThread);
        }
    };

//    private Message message = loadBarHandler.obtainMessage();

    private Runnable loadBarThread = new Runnable() {
        int i = 0;

        @Override
        public void run() {
            //i++返回自增前的值
            i = i + 100;
            if (i <= 1000) {
                Message message = loadBarHandler.obtainMessage();

                message.arg1 = i;
                try {
                    Thread.sleep(1000);
                    //message.arg1 = i;
                    //loadBarHandler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                loadBarHandler.sendMessage(message);
            } else {
                loadBarHandler.removeCallbacks(loadBarThread);
                Toast.makeText(MainActivity.this, "加载结束", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //重置
    private Button btnReset;

    /*******************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*******************************************************************/

        btnPrint = (Button) findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(printThread);
            }
        });

        btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(printThread);
            }
        });

        /*******************************************************************/

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //开始加载进度条
        btnLoad = (Button) findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBarHandler.post(loadBarThread);
                Toast.makeText(MainActivity.this, "开始加载", Toast.LENGTH_SHORT).show();
            }
        });

        //停止加载
        btnStopLoad = (Button) findViewById(R.id.btnStopLoad);
        btnStopLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadBarHandler.removeCallbacks(loadBarThread);
                Toast.makeText(MainActivity.this, "已经停止加载", Toast.LENGTH_SHORT).show();
            }
        });

        //重置
        btnReset = (Button) findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "这个功能还没完成", Toast.LENGTH_SHORT).show();
//                Message message = loadBarHandler.obtainMessage();
//                message.arg1 = 0;
//                progressBar.setProgress(0);
            }
        });

    }

}

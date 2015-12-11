package mr.lmd.personal.handler_01;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private Looper looper;

    private TextView txtView;

    private Handler handler = new Handler();

    /**
     * ********************************************
     */

    private ImageView imageView;
    private int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3};
    private int index;

    class MyRunnable implements Runnable {
        //handler.postDelayed()
        //这个线程被启动后的效果是不断地更换图片
        @Override
        public void run() {
            index++;
            index %= 3;
            imageView.setImageResource(images[index]);
            //下面两个语句是有区别的
            //handler.postDelayed(new MyRunnable(),1000);
            handler.postDelayed(img_runnable, 1000);
        }
    }

    private MyRunnable img_runnable = new MyRunnable();

    /**
     * **********************************************************************
     */

    private Handler handler_1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            //注意没有绑定looper的handler还是在主线程当中处理消息的
            System.out.println("3--->" + Thread.currentThread().getName());

            System.out.println("-------->开始处理消息");
            txtView.setText("接收到的消息:" + msg.arg1 + "--->" + msg.arg2);
            Person p = (Person) msg.obj;
            String person_str = "name=" + p.name + " || age=" + p.age;
            Toast.makeText(MainActivity.this, person_str, Toast.LENGTH_SHORT).show();
        }
    };

    private class Person {
        public int age;
        public String name;

        @Override
        public String toString() {
            return "name=" + name + "||" + "age=" + age;
        }
    }

    /*
    new Handler(Callback callback){
    }
    */
    private Handler handler_2 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), "" + 1, Toast.LENGTH_SHORT).show();

            //表示截获Handler发送过来的消息 并且 下面的handleMessage方法就不会执行了
            //return true;

            //表示截获Handler发送过来的消息 并且 下面的handleMessage方法就仍然会执行
            return false;
        }
    }) {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(), "" + 2, Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**************************************************************************/

        //handler用法1：handler.post();
        //更新UI的操作
        txtView = (TextView) findViewById(R.id.txtView);
        Button btnChange = (Button) findViewById(R.id.btnChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //直接在新开启的一个非UI线程当中更新UI会抛出异常(wrong)
                //上面的理解是当初的错误理解，正确的补充理解是：
                //如果没有去绑定新的线程，那么使用handler只是调用了线程对象的run方法，
                //并没有真正的开启新的线程（Java当中启动新的线程一定是要调用线程对象的start方法）
                //这个时候仍然是运行在hosted thread（主线程）当中的
                /*
                new Thread(){
                    @Override
                    public void run() {
                        try{
                            //报出异常的关键原因应该就是这个地方
                            //这个地方其实是让主线程睡眠2秒，
                            //而不是让新的线程睡眠2秒（因为这里根本就没有启动新的线程）
                            //google的设计应该是不能在界面（Activity）当中让主线程休眠的，
                            //因为这样会导致用户体验不佳
                            Thread.sleep(2000);
                            txtView.setText("update thread");
                        }catch (InterruptedException ex){
                            ex.printStackTrace();
                        }
                    }
                }.start();
                //当然你在这个地方如果使用了.run()就不会报错啦，因为相当于只是调用了一个方法啦
                //并没有启动新的线程，也就没有所谓的在非UI线程当中更新UI了
                //(即其实还是在UI线程当中更新了UI啦)
                //反正记住了线程对象只有调用了.start()方法才算是真正启动了一个新的线程
                */

                txtView.setText("update thread ---> OK");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("0--->" + Thread.currentThread().getName());

                //通过使用handler实现非UI线程(即非主线程啦)更新UI
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("1--->" + Thread.currentThread().getName());
                            Thread.sleep(2000);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    /**
                                     * 需要特别注意的是，这里面的代码确实是运行在主线程当中的
                                     * 即使把handler.post()方法放在了一个新的线程当中执行了
                                     * 当时这样handler.post()还是在主线程当中执行的
                                     */
                                    System.out.println("2--->" + Thread.currentThread().getName());
                                    /**
                                     * 这个地方又验证了上面的一些观点也是错误的
                                     * 在Activity所在的hosted thread当中调用Thread.sleep()并不会抛出异常的
                                     * 只是这个时候用户不能操作这个界面而已(Activity)
                                     * 在这里面睡眠主线程只是会导致用户体验不佳啦，不会抛出异常
                                     */
                                    txtView.setTextColor(Color.BLUE);
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    txtView.setText("update thread" + Math.random());
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                //当然，你这个地方.run()也是可以的，只是不再启动一个新的线程而已，
                //.run()其实就相当于运行了一个方法啦

            }
        });

        /**************************************************************************/

        //handler用法2：handler之postDelayed()
        imageView = (ImageView) findViewById(R.id.imageView);
        //handler.postDelayed(new MyRunnable(),1000);
        handler.postDelayed(img_runnable, 1000);

        /**************************************************************************/

        //handler用法3：
        //handler消息处理机制handler_1.sendMessage() ---> handler_1.handleMessage()
        //点击按钮开始发送消息

        System.out.println("0--->" + Thread.currentThread().getName());

        Button btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);

                            System.out.println("1--->" + Thread.currentThread().getName());

                            //自己创建出Message对象
                            //Message msg = new Message();
                            //使用系统提供的Message对象
                            Message msg = handler_1.obtainMessage();
                            msg.arg1 = 217580;
                            msg.arg2 = 100;
                            Person p = new Person();
                            p.age = 10;
                            p.name = "Marry";
                            msg.obj = p;
                            //通过handler来发送一个消息对象
                            //handler_1.sendMessage(msg);
                            //也可以通过消息对象自己来发送自己
                            System.out.println("-------->开始发送消息");
                            msg.sendToTarget();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        /**************************************************************************/

        //handler用法4：handler.removeCallbacks()
        //点击按钮的时候停止图片轮播效果
        Button btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(MainActivity.this);
        //the key code is ---> handler.removeCallbacks(img_runnable);

        /**************************************************************************/

        //handler用法5：new handler时给出一个Callback参数 ---> 拦截发送的消息
        Button btnHandlerOther = (Button) findViewById(R.id.btnHandlerOther);
        btnHandlerOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler_2.sendEmptyMessage(1);
            }
        });

    }

    //配合handler用法4
    //多多思考学习Activity本事implements OnClickListener类似的做法
    @Override
    public void onClick(View v) {
        //在这里面移除掉线程对象
        handler.removeCallbacks(img_runnable);
    }
}

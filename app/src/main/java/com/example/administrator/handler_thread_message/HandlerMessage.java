package com.example.administrator.handler_thread_message;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class HandlerMessage extends Activity {

    private ProgressBar mPgBar;
    private Button mStartBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_message);
        //成员变量
        mPgBar = (ProgressBar) this.findViewById(R.id.PgBar);
        mStartBt = (Button) this.findViewById(R.id.StartBt);

        mStartBt.setOnClickListener(new ButtonListener());
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v){
            mPgBar.setVisibility(View.VISIBLE);
            updateBarHandler.post(updateThread);
        }
    }

    //使用匿名内部类来复写Handler当中的handleMessage方法
    Handler updateBarHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mPgBar.setProgress(msg.arg1);
            updateBarHandler.post(updateThread);
        }

    };

    //线程类，该类使用匿名内部类的方式进行声明
    Runnable updateThread = new Runnable(){
        int i = 0 ;
        @Override
        public void run() {
            System.out.println("Begin Thread");
            i = i + 10 ;
            //得到一个消息对象，Message类是有Android操作系统提供
            Message msg = updateBarHandler.obtainMessage();
            //将msg对象的arg1参数的值设置为i,用arg1和arg2这两个成员变量传递消息
            msg.arg1 = i ;
            try {
                //睡眠时间：1秒
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //将msg对象加入到消息队列当中
            updateBarHandler.sendMessage(msg);
            if( i == 100){
                Toast.makeText(HandlerMessage.this, "下载完成！", Toast.LENGTH_SHORT).show();
                updateBarHandler.removeCallbacks(updateThread);
            }
        }
    };
}

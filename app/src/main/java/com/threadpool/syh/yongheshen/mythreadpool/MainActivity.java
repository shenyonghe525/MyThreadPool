package com.threadpool.syh.yongheshen.mythreadpool;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews()
    {
        Button btnFixed = (Button) findViewById(R.id.btn_fixed);
        Button btnSingle = (Button) findViewById(R.id.btn_single);
        Button btnCached = (Button) findViewById(R.id.btn_cache);
        Button btnScheduled = (Button) findViewById(R.id.btn_scheduled);
        Button btnSingleScheduled = (Button) findViewById(R.id.btn_singleScheduled);
        Button btnMyPool = (Button) findViewById(R.id.btn_myPool);
        btnFixed.setOnClickListener(this);
        btnSingle.setOnClickListener(this);
        btnCached.setOnClickListener(this);
        btnScheduled.setOnClickListener(this);
        btnSingleScheduled.setOnClickListener(this);
        btnMyPool.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_fixed:
                setFixedThreadPool();
                break;
            case R.id.btn_single:
                setSingleThreadPool();
                break;
            case R.id.btn_cache:
                setCacheThreadPool();
                break;
            case R.id.btn_scheduled:
                setScheduledThreadPool();
                break;
            case R.id.btn_singleScheduled:
                setSingleScheduledThreadPool();
                break;
            case R.id.btn_myPool:
                setMyThreadPool();
                break;
            default:
                break;
        }
    }

    /**
     * 自定义线程池
     */
    private void setMyThreadPool()
    {
        PausableThreadPoolExecutor pausableThreadPoolExecutor = new PausableThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS, new PriorityBlockingQueue());
        for (int i = 1; i < 100; i++) {
        final int priority = i;
        pausableThreadPoolExecutor.execute(new PriorityRunnable(priority) {

            @Override
            public void doSth() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    }

    /**
     * 该方法返回一个可以控制线程池内线程定时或周期性执行某任务的线程池。只不过和上面的区别是该线程池大小为1，
     * 而上面的可以指定线程池的大小。
     */
    private void setSingleScheduledThreadPool()
    {
        ScheduledExecutorService singleThreadScheduledPool = Executors.newSingleThreadScheduledExecutor();
        //延迟1秒后，每隔2秒执行一次该任务
        singleThreadScheduledPool.scheduleAtFixedRate(new Runnable()
        {

            @Override
            public void run()
            {
                String threadName = Thread.currentThread().getName();
                Log.v("zxy", "线程：" + threadName + ",正在执行");
            }
        }, 1, 2, TimeUnit.SECONDS);
    }

    /**
     * 该方法返回一个可以控制线程池内线程定时或周期性执行某任务的线程池。
     */
    private void setScheduledThreadPool()
    {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);
        //延迟2秒后执行该任务
//        scheduledThreadPool.schedule(new Runnable()
//        {
//
//            @Override
//            public void run()
//            {
//
//            }
//        }, 2, TimeUnit.SECONDS);
        //延迟1秒后，每隔2秒执行一次该任务
        scheduledThreadPool.scheduleAtFixedRate(new Runnable()
        {

            @Override
            public void run()
            {
                Log.v("zxy", "线程：" + Thread.currentThread().getName() + ",正在执行");
            }
        }, 1, 2, TimeUnit.SECONDS);
    }

    /**
     * 该方法返回一个可以根据实际情况调整线程池中线程的数量的线程池。即该线程池中的线程数量不确定，是根据实际情况动态调整的。
     */
    private void setCacheThreadPool()
    {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 1; i < 10; i++)
        {
            final int index = i;
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            cachedThreadPool.execute(new Runnable()
            {

                @Override
                public void run()
                {
                    String threadName = Thread.currentThread().getName();
                    Log.v("zxy", "线程：" + threadName + ",正在执行第" + index + "个任务");
                    try
                    {
                        long time = index * 500;
                        Thread.sleep(time);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 该方法返回一个只有一个线程的线程池，即每次只能执行一个线程任务，多余的任务会保存到一个任务队列中，等待这一个线程空闲，
     * 当这个线程空闲了再按FIFO方式顺序执行任务队列中的任务。
     */
    private void setSingleThreadPool()
    {
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        for (int i = 1; i < 10; i++)
        {
            final int index = i;
            singleThreadPool.execute(new Runnable()
            {

                @Override
                public void run()
                {
                    String threadName = Thread.currentThread().getName();
                    Log.v("zxy", "线程：" + threadName + ",正在执行第" + index + "个任务");
                    try
                    {
                        Thread.sleep(1000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 该方法返回一个固定线程数量的线程池，该线程池中的线程数量始终不变，即不会再创建新的线程，也不会销毁已经创建好的线程，
     * 自始自终都是那几个固定的线程在工作，所以该线程池可以控制线程的最大并发数。
     */
    private void setFixedThreadPool()
    {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 1; i < 10; i++)
        {
            final int index = i;
            fixedThreadPool.execute(new Runnable()
            {

                @Override
                public void run()
                {
                    String threadName = Thread.currentThread().getName();
                    Log.v("zxy", "线程：" + threadName + ",正在执行第" + index + "个任务");
                    try
                    {
                        Thread.sleep(1000);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}

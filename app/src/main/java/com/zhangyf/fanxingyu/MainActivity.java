package com.zhangyf.fanxingyu;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    FrameLayout iv0;
    FrameLayout iv1;
    FrameLayout iv2;
    FrameLayout iv3;
    FrameLayout iv4;
    FrameLayout iv5;
    FrameLayout iv6;
    FrameLayout iv7;
    FrameLayout iv8;
    Button btnStart;

    private List<IvStatus> ivStatusList = new ArrayList<>();

    //点击坐标数组
    List<Integer> shouldclickIvPositionSequence = new ArrayList<>();

    //音频
    SoundPool soundPool;
    int soundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv0 = findViewById(R.id.iv_1);
        iv1 = findViewById(R.id.iv_2);
        iv2 = findViewById(R.id.iv_3);
        iv3 = findViewById(R.id.iv_4);
        iv4 = findViewById(R.id.iv_5);
        iv5 = findViewById(R.id.iv_6);
        iv6 = findViewById(R.id.iv_7);
        iv7 = findViewById(R.id.iv_8);
        iv8 = findViewById(R.id.iv_9);
        btnStart = findViewById(R.id.btn_start);
        ivStatusList.add(new IvStatus(0, false));
        ivStatusList.add(new IvStatus(1, false));
        ivStatusList.add(new IvStatus(2, false));
        ivStatusList.add(new IvStatus(3, false));
        ivStatusList.add(new IvStatus(4, false));
        ivStatusList.add(new IvStatus(5, false));
        ivStatusList.add(new IvStatus(6, false));
        ivStatusList.add(new IvStatus(7, false));
        ivStatusList.add(new IvStatus(8, false));
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLightedEmpty(ivStatusList)) {
                    startLoop();
                } else {
                    Toast.makeText(MainActivity.this, "请先按顺序清空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        setOnIvClick(iv0, 0);
        setOnIvClick(iv1, 1);
        setOnIvClick(iv2, 2);
        setOnIvClick(iv3, 3);
        setOnIvClick(iv4, 4);
        setOnIvClick(iv5, 5);
        setOnIvClick(iv6, 6);
        setOnIvClick(iv7, 7);
        setOnIvClick(iv8, 8);
        initSound();
    }


    private void setOnIvClick(final FrameLayout iv, final int position) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!shouldclickIvPositionSequence.isEmpty()) {
                    if (position == shouldclickIvPositionSequence.get(0)) {//正确
                        shouldclickIvPositionSequence.remove(0);
                        iv.setAlpha(0.5f);
                        ivStatusList.get(position).setLighted(true);

                        //全部被用户点亮了，也就是全部正确了 将9宫格所有按钮置为灭的状态
                        if (shouldclickIvPositionSequence.isEmpty()) {
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    for (int i = 0; i <= 8; i++) {
                                        lightIv(i, false);
                                        ivStatusList.get(i).setLighted(false);
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "恭喜你！全部答对！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }, 1000);
                        }
                    } else {//错误
                        soundPool.play(soundId, 1, 1, 0, 0, 1);
                    }
                }
            }
        });
    }

    //开始播
    private void startLoop() {
        final int[] randomArr = randomNumber();
        final int[] index = {0};
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (index[0] < 5) {
                    shouldclickIvPositionSequence.add(randomArr[index[0]]);
                    lightIv(randomArr[index[0]], true);
                    index[0]++;
                } else {
                    for (int i = 0; i <= 4; i++) {
                        lightIv(randomArr[i], false);
                    }
                    cancel();
                }
            }
        }, 100, 1000);
    }

    //随机生成5个数
    private int[] randomNumber() {
        int arr[] = new int[5];//5个数的数组
        for (int i = 0; i < arr.length; i++) {
            //生产一个1-9的随机数
            int index = (int) (Math.random() * 8 + 1);
            arr[i] = index;//把随机数赋值给下标为数组下标为i的值
            //（遍历数组中储存进去的值，i中有几个值则循环几次）
            for (int j = 0; j < i; j++) {
                //把储存在数组中的值j 和 随机出的值i 做比较
                if (arr[j] == arr[i]) {
                    i--; //数组的值下标-1，i的循环次数回到上次
                    break;
                }
            }
        }
        for (int i = 0; i < arr.length; i++) {
            Log.d("!!!!!!!!!!!!!!", arr[i] + " ");
        }
        return arr;

    }

    //点亮/熄灭一个按钮
    private void lightIv(int position, Boolean light) {
        ivStatusList.get(position).setLighted(true);
        switch (position) {
            case 0:
                if (light) iv0.setAlpha(0.5f);
                else iv0.setAlpha(1f);
                break;
            case 1:
                if (light) iv1.setAlpha(0.5f);
                else iv1.setAlpha(1f);
                break;
            case 2:
                if (light) iv2.setAlpha(0.5f);
                else iv2.setAlpha(1f);
                break;
            case 3:
                if (light) iv3.setAlpha(0.5f);
                else iv3.setAlpha(1f);
                break;
            case 4:
                if (light) iv4.setAlpha(0.5f);
                else iv4.setAlpha(1f);
                break;
            case 5:
                if (light) iv5.setAlpha(0.5f);
                else iv5.setAlpha(1f);
                break;
            case 6:
                if (light) iv6.setAlpha(0.5f);
                else iv6.setAlpha(1f);
                break;
            case 7:
                if (light) iv7.setAlpha(0.5f);
                else iv7.setAlpha(1f);
                break;
            case 8:
                if (light) iv8.setAlpha(0.5f);
                else iv8.setAlpha(1f);
                break;

        }

    }

    //是否点没有亮着的
    private Boolean isLightedEmpty(List<IvStatus> ivStatusList) {
        for (IvStatus ivStatus : ivStatusList
        ) {
            if (ivStatus.isLighted) {
                return false;
            }
        }
        return true;
    }

    //提示音初始化
    private void initSound() {
        soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 0);
        soundId = soundPool.load(MainActivity.this, R.raw.sound_error, 1);
    }

    class IvStatus {
        int position;
        Boolean isLighted;

        public IvStatus(int position, Boolean isLighted) {
            this.position = position;
            this.isLighted = isLighted;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public Boolean getLighted() {
            return isLighted;
        }

        public void setLighted(Boolean lighted) {
            isLighted = lighted;
        }
    }

}

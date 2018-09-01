package com.google.thumbseekbardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.blankj.utilcode.util.ToastUtils;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar seekBar = findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ToastUtils.showShort("progress = " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar.setMax(1000);//设置取值范围

        SeekBarHelper.Companion.getInstance(seekBar)//直接传入你的SeekBar对象后使用
                .setTextSize(12.0f)//字体大小
                .setTextFlavor(null, " 米")//前缀 后缀
                .setBackground(R.drawable.shape_thumb_bg, 200.0f, 80.0f)//thumb 背景 宽 高
                .execute();
    }
}

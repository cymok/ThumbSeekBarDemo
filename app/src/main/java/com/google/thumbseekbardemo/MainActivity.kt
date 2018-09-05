package com.google.thumbseekbardemo

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val seekBar: SeekBar = findViewById(R.id.seek_bar)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                ToastUtils.showShort("progress = $progress")
                findViewById<TextView>(R.id.tv).text = "progress = $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        seekBar.max = 1000//设置取值范围

        SeekBarHelper.getInstance(seekBar)//直接传入你的SeekBar对象后使用
                .setTextSize(12.0f)//字体大小
                .setTextColor(Color.RED)//字体颜色
                .setTextFlavor(null, " 米")//前缀 后缀
                .setBackground(R.drawable.shape_thumb_bg, 200.0f, 80.0f)//thumb 背景 宽 高
                .execute()
    }
}

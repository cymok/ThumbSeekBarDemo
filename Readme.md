动态修改SeekBar的Thumb
=======

直入主题, 看图 (不好意思, 没gif动图)
----
![image](https://github.com/cymok/ThumbSeekBarDemo/blob/master/Screenshot/Screenshot.png)

拷贝 SeekBarHelper 类到自己项目中, 然后...

Kotlin代码中使用: 
```kotlin
SeekBarHelper.getInstance(seekBar)//直接传入你的SeekBar对象后使用
    .setTextSize(12.0f)//字体大小
    .setTextFlavor(null, " 米")//前缀 后缀
    .setBackground(R.drawable.shape_thumb_bg, 200.0f, 80.0f)//thumb 背景 宽 高
    .execute()
```
字体颜色和其它的就自己搬代码动手吧

Java代码中使用:
```java
SeekBarHelper.Companion.getInstance(seekBar)//直接传入你的SeekBar对象后使用
    .setTextSize(12.0f)//字体大小
    .setTextFlavor(null, " 米")//前缀 后缀
    .setBackground(R.drawable.shape_thumb_bg, 200.0f, 80.0f)//thumb 背景 宽 高
    .execute();
```

理论上不会影响SeekBar其他api正常使用

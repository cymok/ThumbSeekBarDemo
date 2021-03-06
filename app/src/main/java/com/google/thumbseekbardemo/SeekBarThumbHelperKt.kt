package com.google.thumbseekbardemo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import com.blankj.utilcode.util.ConvertUtils

/**
 * When I wrote this, only God and I understood what I was doing.
 * Now, God only knows.
 *
 *
 * Created by @Harry on 2018-09-01
 */
open class SeekBarThumbHelperKt private constructor(private val seekBar: SeekBar) {

    private var mSeekBar: SeekBar? = seekBar

    private var mPrepositive: String? = ""
    private var mSuffix: String? = ""

    private var mSpTextSize: Float = 12.0f
    private var mTextColor: Int = 0xffffff
    private var mDpWidth: Float = 160.0f
    private var mDpHeight: Float = 100.0f
    private var mDrawableRes: Int = -1

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var sInstance: SeekBarThumbHelperKt? = null

        fun getInstance(seekBar: SeekBar): SeekBarThumbHelperKt {
            if (sInstance == null) {
                synchronized(SeekBarThumbHelperKt::class) {
                    if (sInstance == null) {
                        sInstance = SeekBarThumbHelperKt(seekBar)
                    }
                }
            }
            return sInstance!!
        }
    }

    open fun setTextFlavor(prepositive: String?, suffix: String?): SeekBarThumbHelperKt {
        mPrepositive = prepositive
        mSuffix = suffix
        return this
    }

    open fun setBackground(@DrawableRes drawableRes: Int, dpWidth: Float, dpHeight: Float): SeekBarThumbHelperKt {
        mDpWidth = dpWidth
        mDpHeight = dpHeight
        mDrawableRes = drawableRes
        return this
    }

    open fun setTextSize(spTextSize: Float): SeekBarThumbHelperKt {
        mSpTextSize = spTextSize
        return this
    }

    open fun setTextColor(@ColorInt textColor: Int): SeekBarThumbHelperKt {
        mTextColor = textColor
        return this
    }

    private fun getFormatText(int: Int): String {
        return (if (mPrepositive == null) "" else mPrepositive) + int + (if (mSuffix == null) "" else mSuffix)
    }

    open fun execute() {
        val onSeekBarChangeListener = getOnSeekBarChangeListener(mSeekBar)
        mSeekBar?.thumb = getDrawable(mSeekBar?.context!!, getFormatText(mSeekBar?.progress!!))
        mSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                seekBar.thumb = getDrawable(mSeekBar?.context!!, getFormatText(progress))
                onSeekBarChangeListener?.onProgressChanged(seekBar, progress, fromUser)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                onSeekBarChangeListener?.onStartTrackingTouch(seekBar)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                onSeekBarChangeListener?.onStopTrackingTouch(seekBar)
            }
        })
    }

    private fun getOnSeekBarChangeListener(seekBar: SeekBar?): SeekBar.OnSeekBarChangeListener? {
        if (seekBar == null) {
            return null
        }
        val clazz = SeekBar::class.java
        val field = clazz.getDeclaredField("mOnSeekBarChangeListener")
        field.isAccessible = true
        val any = field.get(seekBar) ?: return null
        return any as SeekBar.OnSeekBarChangeListener
    }

    private fun getDrawable(context: Context, text: String): Drawable {
        val width = ConvertUtils.dp2px(mDpWidth)
        val height = ConvertUtils.dp2px(mDpHeight)
        val textSize = ConvertUtils.sp2px(mSpTextSize)

        val view = Button(context)
        when {
            mDrawableRes >= 0 -> view.setBackgroundResource(mDrawableRes)
        }
        view.text = text
        view.textSize = textSize.toFloat()
        view.setTextColor(mTextColor)
        view.isAllCaps = false
        view.maxLines = 1
        //?
        view.gravity = Gravity.CENTER
        //未显示的view必须layout
        view.setPadding(0, (height - view.lineHeight) / 2, 0, (height - view.lineHeight) / 2)

        //未显示的view必须layout
        view.layout(0, 0, width, height)
        return loadDrawableFromView(view)
    }

    private fun loadBitmapFromView(view: View): Bitmap {
        val width = view.width
        val height = view.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        //也可以从画布画背景
//        canvas.drawColor(Color.WHITE)//如果不设置canvas画布为白色，则生成透明
        view.draw(canvas)
        return bitmap
    }

    private fun loadDrawableFromView(view: View): Drawable {
        val bitmap = loadBitmapFromView(view)
        return BitmapDrawable(null, bitmap)
    }

}

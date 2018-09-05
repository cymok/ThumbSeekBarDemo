package com.google.thumbseekbardemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.blankj.utilcode.util.ConvertUtils;

import java.lang.reflect.Field;

/**
 * When I wrote this, only God and I understood what I was doing.
 * Now, God only knows.
 * <p>
 *
 * @author @Harry
 * @date 2018-09-05
 */
public class SeekBarThumbHelper {

    private SeekBar mSeekBar;

    private String mPrepositive = "";
    private String mSuffix = "";

    private Float mSpTextSize = 12.0f;
    private Integer mTextColor = 0xffffff;
    private Float mDpWidth = 160.0f;
    private Float mDpHeight = 100.0f;
    private Integer mDrawableRes = -1;

    private SeekBarThumbHelper(SeekBar seekBar) {
        mSeekBar = seekBar;
    }

    @SuppressLint("StaticFieldLeak")
    private static SeekBarThumbHelper instance;

    public static SeekBarThumbHelper getInstance(SeekBar seekBar) {
        if (instance == null) {
            synchronized (SeekBarThumbHelper.class) {
                if (instance == null) {
                    instance = new SeekBarThumbHelper(seekBar);
                }
            }
        }
        return instance;
    }


    public SeekBarThumbHelper setTextFlavor(String prepositive, String suffix) {
        mPrepositive = prepositive;
        mSuffix = suffix;
        return this;
    }

    public SeekBarThumbHelper setBackground(@DrawableRes Integer drawableRes, Float dpWidth, Float dpHeight) {
        mDpWidth = dpWidth;
        mDpHeight = dpHeight;
        mDrawableRes = drawableRes;
        return this;
    }

    public SeekBarThumbHelper setTextSize(Float spTextSize) {
        mSpTextSize = spTextSize;
        return this;
    }

    public SeekBarThumbHelper setTextColor(@ColorInt Integer textColor) {
        mTextColor = textColor;
        return this;
    }

    private String getFormatText(Integer integer) {
        return (mPrepositive == null ? "" : mPrepositive) + integer + (mSuffix == null ? "" : mSuffix);
    }

    public void execute() {
        final SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = getOnSeekBarChangeListener(mSeekBar);
        if (mSeekBar == null) {
            return;
        }
        mSeekBar.setThumb(getDrawable(mSeekBar.getContext(), getFormatText(mSeekBar.getProgress())));
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setThumb(getDrawable(mSeekBar.getContext(), getFormatText(progress)));
                if (onSeekBarChangeListener != null) {
                    onSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (onSeekBarChangeListener != null) {
                    onSeekBarChangeListener.onStartTrackingTouch(seekBar);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (onSeekBarChangeListener != null) {
                    onSeekBarChangeListener.onStopTrackingTouch(seekBar);
                }
            }
        });
    }

    private SeekBar.OnSeekBarChangeListener getOnSeekBarChangeListener(SeekBar seekBar) {
        if (seekBar == null) {
            return null;
        }
        Class clazz = SeekBar.class;
        try {
            //noinspection JavaReflectionMemberAccess
            Field field = clazz.getDeclaredField("mOnSeekBarChangeListener");
            field.setAccessible(true);
            try {
                Object listener = field.get(seekBar);
                return (SeekBar.OnSeekBarChangeListener) listener;
            } catch (IllegalAccessException e) {
                return null;
            }
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private Drawable getDrawable(Context context, String text) {
        Integer width = ConvertUtils.dp2px(mDpWidth);
        Integer height = ConvertUtils.dp2px(mDpHeight);
        Integer textSize = ConvertUtils.sp2px(mSpTextSize);

        Button view = new Button(context);
        if (mDrawableRes >= 0) {
            view.setBackgroundResource(mDrawableRes);
        }
        view.setText(text);
        view.setTextSize(textSize);
        view.setTextColor(mTextColor);
        view.setAllCaps(false);
        view.setMaxLines(1);
        //?
        view.setGravity(Gravity.CENTER);
        //未显示的view必须layout
        view.setPadding(0, (height - view.getLineHeight()) / 2, 0, (height - view.getLineHeight()) / 2);

        //未显示的view必须layout
        view.layout(0, 0, width, height);
        return loadDrawableFromView(view);
    }

    private Bitmap loadBitmapFromView(View view) {
        Integer width = view.getWidth();
        Integer height = view.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //也可以从画布画背景
//        canvas.drawColor(Color.WHITE)//如果不设置canvas画布为白色，则生成透明
        view.draw(canvas);
        return bitmap;
    }

    private Drawable loadDrawableFromView(View view) {
        Bitmap bitmap = loadBitmapFromView(view);
        return new BitmapDrawable(null, bitmap);
    }

}

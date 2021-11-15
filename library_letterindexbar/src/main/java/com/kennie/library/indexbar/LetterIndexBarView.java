/*
 * Copyright (c) 2021 Kennie<xdpvxv@163.com>
 *
 * The software is licensed under the Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *     http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v2 for more details.
 */
package com.kennie.library.indexbar;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

/**
 * @项目名 KennieIndexBar
 * @类名称 LetterIndexBarView
 * @类描述 字母侧边栏
 * @创建人 kennie
 * @修改人
 * @创建时间 2021/10/21 22:49
 */
public class LetterIndexBarView extends View {

    private static final String TAG = "LetterIndexBarView";

//    public static final String[] DEFAULT_LETTERS = {"↑", "★", "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
//            , "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static final String[] DEFAULT_LETTERS = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
            , "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private String[] mLetters = DEFAULT_LETTERS; // 字母表

    private int currentPosition = -1; // 当前选中的字母位置
    private int mOldPosition;
    private int mNewPosition;

    private Paint mLettersPaint = new Paint(); // 字母列表画笔
    private Paint mTextPaint = new Paint(); // 提示字母画笔
    private Paint mWavePaint = new Paint(); // 波浪效果画笔


    private float mIndexTextSize; // 索引文字大小
    private int mIndexTextColor; // 索引文字颜色

    private float mLargeTextSize;

    private int mWaveColor;
    private int mTextColorChoose;
    private int mWidth; // 字符所在区域宽度
    private int mHeight; // 字符所在区域高度
    private int mItemHeight; // 单个字母的高度
    private int mPadding;

    // 计算波浪贝塞尔曲线的角弧长值
    private static final double ANGLE = Math.PI * 45 / 180;
    private static final double ANGLE_R = Math.PI * 90 / 180;

    // 圆形路径
    private Path mBallPath = new Path();

    // 手指滑动的Y点作为中心点
    private int mCenterY; //中心点Y

    // 贝塞尔曲线的分布半径
    private int mRadius;

    // 圆形半径
    private int mBallRadius;
    // 用于过渡效果计算
    ValueAnimator mRatioAnimator;

    // 用于绘制贝塞尔曲线的比率
    private float mRatio;

    // 选中字体的坐标
    private float mPosX, mPosY;

    // 圆形中心点X
    private float mBallCentreX;


    private OnLetterChangeListener onLetterChangeListener;


    public LetterIndexBarView(Context context) {
        this(context, null);
    }

    public LetterIndexBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterIndexBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //mLetters = context.getResources().getStringArray(R.array.Letters);

        mIndexTextSize = context.getResources().getDimensionPixelSize(R.dimen.textSize_sidebar);
        mIndexTextColor = Color.GRAY;

        mWaveColor = Color.parseColor("#be2580D5");

        mTextColorChoose = context.getResources().getColor(android.R.color.black);
        mLargeTextSize = context.getResources().getDimensionPixelSize(R.dimen.large_textSize_sidebar);
        mPadding = context.getResources().getDimensionPixelSize(R.dimen.textSize_sidebar_padding);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LetterIndexBarView);
            mIndexTextSize = typedArray.getFloat(R.styleable.LetterIndexBarView_indexTextSize, mIndexTextSize);
            mIndexTextColor = typedArray.getColor(R.styleable.LetterIndexBarView_indexTextColor, mIndexTextColor);

            mTextColorChoose = typedArray.getColor(R.styleable.LetterIndexBarView_sidebarChooseTextColor, mTextColorChoose);
            mLargeTextSize = typedArray.getFloat(R.styleable.LetterIndexBarView_sidebarLargeTextSize, mLargeTextSize);
            mWaveColor = typedArray.getColor(R.styleable.LetterIndexBarView_sidebarBackgroundColor, mWaveColor);
            mRadius = typedArray.getInt(R.styleable.LetterIndexBarView_sidebarRadius, context.getResources().getDimensionPixelSize(R.dimen.radius_sidebar));
            mBallRadius = typedArray.getInt(R.styleable.LetterIndexBarView_sidebarBallRadius, context.getResources().getDimensionPixelSize(R.dimen.ball_radius_sidebar));
            typedArray.recycle();
        }

        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);
        mWavePaint.setStyle(Paint.Style.FILL);
        mWavePaint.setColor(mWaveColor);

        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColorChoose);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mLargeTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mItemHeight = (mHeight - mPadding) / mLetters.length;
        mPosX = mWidth - 1.6f * mIndexTextSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景
        drawBackground(canvas);
        // 绘制字母列表
        drawLetters(canvas);
        // 绘制圆
        drawBallPath(canvas);
        // 绘制选中的字体
        drawChooseText(canvas);

    }

    private void drawBackground(Canvas canvas) {
        RectF rectF = new RectF();
        rectF.left = mPosX - mIndexTextSize;
        rectF.right = mPosX + mIndexTextSize;
        rectF.top = mIndexTextSize / 2;
        rectF.bottom = mHeight - mIndexTextSize / 2;

        // 绘制背景
        mLettersPaint.reset();
        mLettersPaint.setStyle(Paint.Style.FILL);
        mLettersPaint.setColor(Color.parseColor("#F9F9F9"));
        mLettersPaint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, mIndexTextSize, mIndexTextSize, mLettersPaint);

        // 绘制背景边框
        mLettersPaint.reset();
        mLettersPaint.setStyle(Paint.Style.STROKE);
        mLettersPaint.setColor(mIndexTextColor);
        mLettersPaint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, mIndexTextSize, mIndexTextSize, mLettersPaint);
    }

    private void drawLetters(Canvas canvas) {

        for (int i = 0; i < mLetters.length; i++) {
            mLettersPaint.reset();
            mLettersPaint.setColor(mIndexTextColor);
            mLettersPaint.setAntiAlias(true);
            mLettersPaint.setTextSize(mIndexTextSize);
            mLettersPaint.setTextAlign(Paint.Align.CENTER);

            Paint.FontMetrics fontMetrics = mLettersPaint.getFontMetrics();
            float baseline = Math.abs(-fontMetrics.bottom - fontMetrics.top);

            float posY = mItemHeight * i + baseline / 2 + mPadding;

            if (i == currentPosition) {
                mPosY = posY;
            } else {
                canvas.drawText(mLetters[i], mPosX, posY, mLettersPaint);
            }
        }

    }

    private void drawChooseText(Canvas canvas) {
        if (currentPosition != -1) {
            // 绘制右侧选中字符
            mLettersPaint.reset();
            mLettersPaint.setColor(Color.BLACK);
            mLettersPaint.setTextSize(mIndexTextSize);
            mLettersPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mLetters[currentPosition], mPosX, mPosY, mLettersPaint);
        }
    }


    private void drawBallPath(Canvas canvas) {
        //x轴的移动路径
        mBallCentreX = (mWidth + mBallRadius) - (2.0f * mRadius + 2.0f * mBallRadius) * mRatio;
        mBallPath.reset();
        mBallPath.addCircle(mBallCentreX, mCenterY, mBallRadius, Path.Direction.CW);
        mBallPath.close();
        canvas.drawPath(mBallPath, mWavePaint);

        if (currentPosition != -1) {
            // 绘制提示字符
            if (mRatio >= 0.9f) {
                String target = mLetters[currentPosition];
                Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
                float baseline = Math.abs(-fontMetrics.bottom - fontMetrics.top);
                float x = mBallCentreX;
                float y = mCenterY + baseline / 2;
                canvas.drawText(target, x, y, mTextPaint);
            }
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final float y = event.getY();
        final float x = event.getX();

        mOldPosition = currentPosition;
        mNewPosition = (int) (y / mHeight * mLetters.length);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (x < mWidth - 2 * mRadius) {
                    return false;
                }
                mCenterY = (int) y;
                startAnimator(mRatio, 1.0f);
                break;
            case MotionEvent.ACTION_MOVE:
                // 开始触摸
                mCenterY = (int) y;
                // 手指滑动
                if (mOldPosition != mNewPosition) {
                    if (mNewPosition >= 0 && mNewPosition < mLetters.length) {
                        currentPosition = mNewPosition;
                        if (null != onLetterChangeListener && !TextUtils.isEmpty(mLetters[mNewPosition])) {
                            onLetterChangeListener.onLetterChanged(mNewPosition, mLetters[mNewPosition]);
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // 手指抬起
                // 关闭波浪效果
                startAnimator(mRatio, 0f);
                currentPosition = -1;
                if (null != onLetterChangeListener) {
                    if (mNewPosition >= 0 && mNewPosition < mLetters.length) {
                        onLetterChangeListener.onLetterClosed(mNewPosition, mLetters[mNewPosition]);
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }


    private void startAnimator(float... value) {
        if (mRatioAnimator == null) {
            mRatioAnimator = new ValueAnimator();
        }
        mRatioAnimator.cancel();
        mRatioAnimator.setFloatValues(value);
        mRatioAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator value) {
                mRatio = (float) value.getAnimatedValue();
                //球弹到位的时候，并且点击的位置变了，即点击的时候显示当前选择位置
                if (mRatio == 1f && mOldPosition != mNewPosition) {
                    if (mNewPosition >= 0 && mNewPosition < mLetters.length) {
                        currentPosition = mNewPosition;
                        if (onLetterChangeListener != null) {
                            onLetterChangeListener.onLetterChanged(mNewPosition, mLetters[mNewPosition]);
                        }
                    }
                }
                invalidate();
            }
        });
        mRatioAnimator.start();
    }


    /**
     * @param listener
     */
    public void setOnLetterChangeListener(OnLetterChangeListener listener) {
        this.onLetterChangeListener = listener;
    }


    /**
     * 获取字母表
     *
     * @return 字母集合
     */
    public String[] getLetters() {
        return mLetters;
    }

    /**
     * 设置字母表
     *
     * @param letters 字母集合
     */
    public void setLetters(String[] letters) {
        this.mLetters = letters;
        requestLayout();
        invalidate();
    }

    public static class Builder {

        private LetterIndexBarView letterIndexBarView;
        private Context context;

        public Builder(Context context) {
            this.letterIndexBarView = new LetterIndexBarView(context);
            this.context = context;
        }

        /**
         * 设置索引文字大小
         *
         * @param indexTextSize 索引文字大小
         * @return
         */
        public Builder setIndexTextSize(float indexTextSize) {
            letterIndexBarView.mIndexTextSize = indexTextSize;
            return this;
        }

        /**
         * 设置索引文字大小
         *
         * @param dimenId
         * @return
         */
        public Builder setIndexTextSize(int dimenId) {
            letterIndexBarView.mIndexTextSize = this.context.getResources().getDimensionPixelSize(dimenId);
            return this;
        }

        public LetterIndexBarView build() {
            return this.letterIndexBarView;
        }
    }

}

package com.intent.letterview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述:
 * 作者:xues
 * 时间:2019年02月24日
 */

public class LetterView extends View {
    /*字母属性*/
    private Paint mLetterPaint;
    private float mTextSize = 50;
    private int mTextColor = Color.WHITE;
    private float baseYOff = 0;
    private Paint.FontMetrics mFontMetrics;
    private Rect mTextRect = new Rect();
    /*下划线属性*/
    private Paint mLinePaint;
    private int mUnderLineColor = Color.WHITE;
    private float mLineLineDistance = 10;//下划线间距
    private float mUnderLineWidth = 60;//下划线宽度
    private float mUnderLineHeight = 4;//下划线高度
    private List<Character> mAllLetters = new ArrayList<>();
    private List<Character> mCurLetters = new ArrayList<>();
    private float mTextRectHeight;
    private int mLineType = LINE_TYPE_SQUARE;

    private static final int LINE_TYPE_SQUARE = 0;
    private static final int LINE_TYPE_CIRCLE = 1;


    private int mTotalLength = 5;//总长度
    private float mLineTextDistance = 20;//文字与下划线的距离
    private String mText = "";

    public LetterView(Context context) {
        this(context, null);
    }

    public LetterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LetterView, 0, 0);
        mText = a.getString(R.styleable.LetterView_text);
        mTextSize = a.getDimension(R.styleable.LetterView_text_size, mTextSize);
        mTextColor = a.getColor(R.styleable.LetterView_text_color, mTextColor);
        mUnderLineColor = a.getColor(R.styleable.LetterView_underline_color, mUnderLineColor);
        mUnderLineWidth = a.getDimension(R.styleable.LetterView_underline_width, mUnderLineWidth);
        mUnderLineHeight = a.getDimension(R.styleable.LetterView_underline_height, mUnderLineHeight);
        mLineTextDistance = a.getDimension(R.styleable.LetterView_line_text_distance, mLineTextDistance);
        mLineLineDistance = a.getDimension(R.styleable.LetterView_line_line_distance, mLineLineDistance);
        mLineType = a.getInt(R.styleable.LetterView_line_type, mLineType);
        a.recycle();


        try {
            char[] arras = mText.toCharArray();
            for (char arra : arras) {
                mAllLetters.add(arra);
                mCurLetters.add(arra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*文字*/
        mLetterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLetterPaint.setColor(mTextColor);
        mLetterPaint.setTextSize(mTextSize);
        mFontMetrics = mLetterPaint.getFontMetrics();
        baseYOff = (mFontMetrics.bottom - mFontMetrics.top) / 2f - mFontMetrics.bottom;
        mTextRectHeight = mFontMetrics.bottom - mFontMetrics.top;
//        mLetterPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
        /*下划线*/
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(mUnderLineColor);
        mLinePaint.setStrokeWidth(mUnderLineHeight);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        switch (mLineType) {
            case LINE_TYPE_CIRCLE:
                mLinePaint.setStrokeCap(Paint.Cap.ROUND);
                break;
            case LINE_TYPE_SQUARE:
            default:
                mLinePaint.setStrokeCap(Paint.Cap.SQUARE);
                break;
        }

    }

    private int columnNum;
    private int rowNum;
    private int singleBoxW;
    private int singleBoxH;

    private int mWidthMeasureSpec, mHeightMeasureSpec;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mWidthMeasureSpec = widthMeasureSpec;
        this.mHeightMeasureSpec = heightMeasureSpec;
        reMeasure();

    }

    private void reMeasure() {
        int wSize = MeasureSpec.getSize(mWidthMeasureSpec);
        int hSize = MeasureSpec.getSize(mHeightMeasureSpec);
        int hMode = MeasureSpec.getMode(mHeightMeasureSpec);

        singleBoxW = (int) (mUnderLineWidth + mLineLineDistance);
        singleBoxH = (int) (mFontMetrics.bottom - mFontMetrics.top + mLineTextDistance);
        columnNum = wSize / singleBoxW;//列数
        mTotalLength = mAllLetters.size();
        rowNum = mTotalLength / columnNum;
        rowNum = mTotalLength % columnNum != 0 ? rowNum + 1 : rowNum;//行数

        switch (hMode) {
            case MeasureSpec.AT_MOST:
                hSize = rowNum * singleBoxH;
                break;
            case MeasureSpec.EXACTLY:
                break;
        }

        setMeasuredDimension(wSize, hSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mAllLetters.size(); i++) {
            int row = i / columnNum;
            int column = i % columnNum;
            float startX = (column * singleBoxW) + mLineLineDistance / 2f;
            float startY = (row + 1) * singleBoxH - mUnderLineHeight / 2f;
            float stopX = (column * singleBoxW) + mLineLineDistance / 2f + mUnderLineWidth;
            float stopY = (row + 1) * singleBoxH - mUnderLineHeight / 2f;
            canvas.drawLine(startX,
                    startY,
                    stopX,
                    stopY,
                    mLinePaint);

            if (mCurLetters.size() > i) {
                mLetterPaint.getTextBounds(mCurLetters.get(i) + "", 0, 1, mTextRect);
                canvas.drawText(mCurLetters.get(i) + "", column * singleBoxW + (singleBoxW - mTextRect.width()) / 2f, row * singleBoxH + singleBoxH / 2f + baseYOff, mLetterPaint);
            }


        }
    }

    public void initWord(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        this.mText = text;
        char[] letters = mText.toCharArray();
        mAllLetters.clear();
        for (char arra : letters) {
            mAllLetters.add(arra);
        }
        requestLayout();
        invalidate();
    }

    /**
     * 新增
     *
     * @param c
     */
    public void append(char c) {
        if (mText.length() <= mCurLetters.size()) {
            return;
        }
        mCurLetters.add(c);
        requestLayout();
        invalidate();
    }

    /**
     * 删除
     */
    public void del() {
        if (mCurLetters.size() > 0) {
            mCurLetters.remove(mCurLetters.size() - 1);
            requestLayout();
            invalidate();
        }

    }
}

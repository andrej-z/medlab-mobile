package com.az.testresultview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class TestResultViewBase extends View {
    int HEIDHT = 10;
    int BOTTOM_OFFSET = 19;
    public void setLowLimit(double lowLimit) {
        LowLimit = lowLimit;
        this.invalidate();
    }



    public void setHighLimit(double highLimit) {
        HighLimit = highLimit;
        this.invalidate();
    }
    double LowLimit = 40;
    double HighLimit = 60;
    Double Result = 150.0;
    public void setResultValue(Double result) {
        Result = result;
        this.invalidate();


    }


    double LimitFontSize =16;
    double ResultFontSize = 20;
    Typeface font = Typeface.DEFAULT;
    int PassedColor;
    int FailedColor;

    public void setDimension(String dimension) {
        Dimension = dimension;
        this.invalidate();
    }

    String Dimension;
    NumberFormat nf = new DecimalFormat("##.##");
    public TestResultViewBase(Context context) {
        super(context);
    }

    public TestResultViewBase(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TestResultViewBase,
                0, 0);

        try {
            HEIDHT = a.getInt(R.styleable.TestResultViewBase_ChartHeight,16);
            // LowLimit = a.getFloat(R.styleable.TestResultViewBase_LowLimit,-1);
            // HighLimit = a.getFloat(R.styleable.TestResultViewBase_LowLimit,-1);
            //Result = a.getFloat(R.styleable.TestResultViewBase_ResultValue,-1);
            LimitFontSize = a.getDimension(R.styleable.TestResultViewBase_LimitFontSize,16);
            ResultFontSize=a.getDimension(R.styleable.TestResultViewBase_ResultValue,20);
            FailedColor = a.getColor(R.styleable.TestResultViewBase_FailedColor, Color.parseColor("#EB5757"));
            PassedColor = a.getColor(R.styleable.TestResultViewBase_FailedColor, Color.parseColor("#27AE60"));
            Dimension = a.getString(R.styleable.TestResultViewBase_Dimension);
            if (Dimension == null)
                Dimension = "mg/ml";
        } finally {
            a.recycle();
        }
    }

    public TestResultViewBase(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context,attrs);
        // super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        double height = LimitFontSize+HEIDHT+ResultFontSize+50;

        super.onMeasure(widthMeasureSpec,  MeasureSpec.makeMeasureSpec((int)height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Result == null)
            return;
        double max = Result<(HighLimit+HighLimit*0.125)?(HighLimit+HighLimit*0.125):
                (Result+Result*0.125);

        // double min = (HighLimit-LowLimit)/2;
        double min = LowLimit - LowLimit*0.125;
        if (Result< min)
            min = Result - Result*0.125;
        min = min>LowLimit?0:min;

        double percent = (max-min);
        double LowerLimitPercent = (LowLimit-min)/percent;
        double UpperLimitPercent = (HighLimit-min)/percent;
        double ResultPercentage = (Result-min)/percent;
        if (LowLimit ==0 && HighLimit == 0 && Result ==0)
            return;

        drawResultPart(canvas,FailedColor,0,LowerLimitPercent);
        drawResultPart(canvas,PassedColor,LowerLimitPercent,UpperLimitPercent);
        drawResultPart(canvas,FailedColor,UpperLimitPercent,100);
        drawLimitMark(canvas,Color.GRAY,LowerLimitPercent,LowLimit);
        drawLimitMark(canvas,Color.GRAY,UpperLimitPercent,HighLimit);
        drawResultMark(canvas,Color.DKGRAY,ResultPercentage,Result);
    }

    private void drawResultPart(Canvas canvas, int color, double start, double end) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        double StartPixel = getWidth()*start;
        double EndPixel = getWidth()*end;
        Rect rect = new Rect();
        rect.left = (int)StartPixel;
        rect.right = (int)EndPixel;

        rect.top = getHeight() - (int)LimitFontSize - BOTTOM_OFFSET - HEIDHT/2;
        rect.bottom = getHeight() - (int)LimitFontSize - BOTTOM_OFFSET + HEIDHT/2;

        canvas.drawRect(rect, paint);
    }

    private void drawLimitMark(Canvas canvas, int color, double start,double result) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        double StartPixel = getWidth()*start-3;
        if (StartPixel<2)
            StartPixel = 2;
        double EndPixel = StartPixel+3;

        Rect rect = new Rect();
        rect.left = (int)StartPixel;
        rect.right = (int)EndPixel;
        rect.top = getHeight() - (int)LimitFontSize - BOTTOM_OFFSET - HEIDHT/2;
        rect.bottom = getHeight() - (int)LimitFontSize - BOTTOM_OFFSET + HEIDHT/2 +10;

        canvas.drawRect(rect, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);
        paint.setTextSize((int)LimitFontSize);
        String text = nf.format(result);
        double xCoord = rect.left-text.length()*5/2;

        paint.setTypeface(font);
        canvas.drawText(text, (int)xCoord, (int)(rect.bottom+LimitFontSize), paint);
    }

    private void drawResultMark(Canvas canvas, int color, double start, double result) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        double StartPixel = getWidth()*start-15;
        if (StartPixel<1)
            StartPixel = 1;
        double EndPixel = StartPixel+15;
        double leftx = StartPixel;
        double topy =  getHeight() - (int)LimitFontSize - BOTTOM_OFFSET - HEIDHT/2-10;
        double rightx =  EndPixel;
        double bottomy =  getHeight() - (int)LimitFontSize - BOTTOM_OFFSET + HEIDHT/2 +10;
        // FILL
        canvas.drawRect((float) leftx, (float) topy, (float) rightx, (float) bottomy, paint);

        paint.setStrokeWidth(2);

        paint.setColor(color);
        paint.setTypeface(font);
        paint.setStyle(Paint.Style.STROKE);
        // BORDER
        canvas.drawRect((float) leftx, (float) topy, (float) rightx, (float) bottomy, paint);


//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.GRAY);
//        paint.setTextSize((int)ResultFontSize);
//        String text = nf.format(result)+" "+Dimension;
//        double xCoord = leftx-text.length()*7/2;
//        if ((xCoord+text.length()*7)>getWidth()){
//            xCoord = getWidth()-text.length()*7;
//        }
//        canvas.drawText(text, (int)xCoord, (int)topy-(int)ResultFontSize, paint);

    }
}

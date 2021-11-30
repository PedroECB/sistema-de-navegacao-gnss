package com.example.trabalhosemestral;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class CircleView extends View {

    private int mXc, mYc; //Coordenadas do centro do circulo
    private int mRadius; //Raio do circulo
    private int mCircleColor; //Cor do circulo
    private int mTextColor; //Cor do texto
    private String mText; //Texto

    public CircleView(Context context, @Nullable AttributeSet attributeSet){
        super(context, attributeSet);

        TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.CircleView, 0,0);

        try {
            mXc = a.getInt(R.styleable.CircleView_Xc, 0);
            mYc = a.getInt(R.styleable.CircleView_Yc, 0);
            mRadius = a.getInt(R.styleable.CircleView_Radius, 0);
            mCircleColor = a.getInt(R.styleable.CircleView_CircleColor, 0);
            mTextColor = a.getInt(R.styleable.CircleView_TextColor,0);
            mText = a.getString(R.styleable.CircleView_Text);

        }finally {
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        Paint paint = new Paint();
        int viewWidthHalf = this.getMeasuredWidth()/2;
        int viewHeightHalf = this.getMeasuredHeight()/2;

        if(viewWidthHalf > viewHeightHalf)
            mRadius = viewHeightHalf-10;
        else
            mRadius = viewWidthHalf-10;
        mXc = viewWidthHalf;
        mYc = viewHeightHalf;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mCircleColor);
        canvas.drawCircle(mXc, mYc, mRadius, paint);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        canvas.drawText(mText, mXc, mYc, paint);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(8f);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(this.getMeasuredWidth()/2, this.getMeasuredHeight()/2, this.getMeasuredWidth()/3, paint);
        canvas.drawCircle(this.getMeasuredWidth()/2, this.getMeasuredHeight()/2, (this.getMeasuredWidth()/7), paint);
        paint.setStyle(Paint.Style.FILL);
        //canvas.drawCircle(mXc, mYc, 30f, paint);

        canvas.drawLine(10, (this.getMeasuredHeight()/2), this.getMeasuredWidth()-10, (this.getMeasuredHeight()/2), paint);
        canvas.drawLine(this.getMeasuredWidth()/2, 340, this.getMeasuredWidth()/2, this.getMeasuredHeight()-340, paint);

        //canvas.drawLine(200, 0, 0, 200, paint);
        //canvas.drawLine(this.getMeasuredWidth()/2, 384,180, 384f, paint);
        //canvas.drawLine(mXc+15, (mYc-10),0.0f, 0.0f, paint);

        Toast.makeText(getContext(), "MeansureWidth:"+this.getMeasuredWidth(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "MeansureHeight:"+this.getMeasuredHeight(), Toast.LENGTH_SHORT).show();


    }


}

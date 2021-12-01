package com.example.trabalhosemestral;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.GnssStatus;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class CircleView extends View {

    private int mXc, mYc; //Coordenadas do centro do circulo
    private int mRadius; //Raio do circulo
    private int mCircleColor; //Cor do circulo
    private int mTextColor; //Cor do texto
    private String mText; //Texto

    private GnssStatus newStatus;
    private int r;
    private int height,width;


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

    public void onSatelliteStatusChanged(GnssStatus status) {
        newStatus = status;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // coletando informações do tamanho tela de desenho
        width=getMeasuredWidth();
        height=getMeasuredHeight();

        // definindo o raio da esfera celeste
        if (width<height)
            r=(int)(width/2*0.9);
        else
            r=(int)(height/2*0.9);

        // configurando o pincel para desenhar a projeção da esfera celeste
        Paint paint=new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);

        // Desenha a projeção da esfera celeste
        // desenhando círculos concêntricos
        int radius=r;
        canvas.drawCircle(computeXc(0), computeYc(0), radius, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        radius=(int)(radius*Math.cos(Math.toRadians(45)));
        canvas.drawCircle(computeXc(0), computeYc(0), radius, paint);
        radius=(int)(radius*Math.cos(Math.toRadians(60)));
        canvas.drawCircle(computeXc(0), computeYc(0), radius, paint);

        //desenhando os eixos
        paint.setColor(Color.WHITE);
        canvas.drawLine(computeXc(0),computeYc(-r),computeXc(0),computeYc(r),paint);
        canvas.drawLine(computeXc(-r),computeYc(0),computeXc(r),computeYc(0),paint);

        // configura o pincel para desenhar os satélites
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);

        // desenhando os satélites
        if (newStatus!=null) {
            for(int i=0;i<newStatus.getSatelliteCount();i++) {

                float az=newStatus.getAzimuthDegrees(i);
                float el=newStatus.getElevationDegrees(i);
                float x=(float)(r*Math.cos(Math.toRadians(el))*Math.sin(Math.toRadians(az)));
                float y=(float)(r*Math.cos(Math.toRadians(el))*Math.cos(Math.toRadians(az)));

                drawSattelite(computeXc(x), computeYc(y), newStatus, i, canvas);

/*                canvas.drawCircle(computeXc(x), computeYc(y), 30, paint);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(30);
                String satID=newStatus.getSvid(i)+"";
                canvas.drawText(satID, computeXc(x)-15, computeYc(y)+10, paint);
                paint.setColor(Color.YELLOW);
                String satConstellation = newStatus.getConstellationType(i)+"";
                canvas.drawText(satConstellation, computeXc(x), computeYc(y)+50, paint);*/

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void drawSattelite(float x, float y, GnssStatus status, int index, Canvas canvas){

        String satConstellation = newStatus.getConstellationType(index)+"";
        String satID= newStatus.getSvid(index)+"";

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(25);
        paint.setTextAlign(Paint.Align.LEFT);

        switch (status.getConstellationType(index)){

                //GPS - EUA
            case 1:
                paint.setColor(Color.YELLOW);
                canvas.drawCircle(x, y, 30, paint);
                paint.setColor(Color.BLUE);
                canvas.drawText("("+satID+")", x-15, y+10, paint);
                paint.setColor(Color.BLACK);
                canvas.drawText(satConstellation, x, y+50, paint);
                break;

                //SBAS - CHINA
            case 2:
                paint.setColor(Color.RED);
                canvas.drawCircle(x, y, 30, paint);
                paint.setColor(Color.YELLOW);
                canvas.drawText("("+satID+")", x-15, y+10, paint);
                paint.setColor(Color.YELLOW);
                canvas.drawText(satConstellation, x, y+50, paint);
                break;

                //GLONASS - RÚSSIA
            case 3:
                paint.setColor(Color.MAGENTA);
                canvas.drawCircle(x, y, 30, paint);
                paint.setColor(Color.WHITE);
                canvas.drawText("("+satID+")", x-15, y+10, paint);
                paint.setColor(Color.YELLOW);
                canvas.drawText(satConstellation, x, y+50, paint);
                break;

                //QZSS - JAPÃO
            case 4:
                paint.setColor(Color.WHITE);
                canvas.drawCircle(x, y, 30, paint);
                paint.setColor(Color.RED);
                canvas.drawText("("+satID+")", x-15, y+10, paint);
                paint.setColor(Color.YELLOW);
                canvas.drawText(satConstellation, x, y+50, paint);
                break;

                //BEIDOU - CHINA
            case 5:
                paint.setColor(Color.RED);
                canvas.drawCircle(x, y, 30, paint);
                paint.setColor(Color.WHITE);
                canvas.drawText("("+satID+")", x-15, y+10, paint);
                paint.setColor(Color.YELLOW);
                canvas.drawText(satConstellation, x, y+50, paint);
                break;

                //GALILEO - UNIAO EUROPEIA
            case 6:
                paint.setColor(Color.BLUE);
                canvas.drawCircle(x, y, 30, paint);
                paint.setColor(Color.YELLOW);
                canvas.drawText("("+satID+")", x-15, y+10, paint);
                paint.setColor(Color.YELLOW);
                canvas.drawText(satConstellation, x, y+50, paint);
                break;

            //GALILEO - UNIAO EUROPEIA
            case 7:
                paint.setColor(Color.CYAN);
                canvas.drawCircle(x, y, 30, paint);
                paint.setColor(Color.BLACK);
                canvas.drawText("("+satID+")", x-15, y+10, paint);
                paint.setColor(Color.YELLOW);
                canvas.drawText(satConstellation, x, y+50, paint);
                break;
                //DEFAULT
            default:
                paint.setColor(Color.BLACK);
                canvas.drawCircle(x, y, 30, paint);
                paint.setColor(Color.GREEN);
                canvas.drawText("("+satID+")", x-15, y+10, paint);
                paint.setColor(Color.YELLOW);
                canvas.drawText(satConstellation, x, y+50, paint);
                break;

        }


    }

/*    @Override
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


    }*/

    private int computeXc(double x) {
        return (int)(x+width/2);
    }
    private int computeYc(double y) {
        return (int)(-y+height/2);
    }


}

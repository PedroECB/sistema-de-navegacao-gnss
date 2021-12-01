package com.example.trabalhosemestral;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EsferaCeleste extends AppCompatActivity {

    CircleView circleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_esfera_celeste);
        setContentView(R.layout.circle_view);

    }



    public void Invalidate(){
        this.circleView.invalidate();
    }
}
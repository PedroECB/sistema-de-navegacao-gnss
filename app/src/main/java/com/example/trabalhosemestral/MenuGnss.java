package com.example.trabalhosemestral;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuGnss extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_gnss);

        Button botaoEsferaCeleste = (Button) findViewById(R.id.btnEsferaCeleste);
        Button botaoInfoSatelite = (Button) findViewById(R.id.btnInfoSatelite);


        botaoEsferaCeleste.setOnClickListener(this);
        botaoInfoSatelite.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnEsferaCeleste:
                Intent esferaCeleste = new Intent(this, EsferaCeleste.class);
                startActivity(esferaCeleste);
                break;

            case R.id.btnInfoSatelite:
                Intent infoSatelite = new Intent(this, LocationManagerActivity.class);
                startActivity(infoSatelite);
                break;

        }
    }
}
package com.example.trabalhosemestral;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button botaoConfiguracao = (Button) findViewById(R.id.button_telaconfiguracao);
        Button botaoCreditos = (Button) findViewById(R.id.button_telacreditos);

        botaoConfiguracao.setOnClickListener(this);
        botaoCreditos.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()){
            case R.id.button_telaconfiguracao:
                Intent configTela = new Intent(this, ConfigActivity.class);
                startActivity(configTela);
                break;

            case R.id.button_telacreditos:
                Intent configCredito = new Intent(this, ConfigActivity.class);
                startActivity(configCredito);
                break;
        }
    }
}
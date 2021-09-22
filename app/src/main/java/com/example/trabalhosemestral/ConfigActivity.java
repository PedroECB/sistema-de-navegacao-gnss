package com.example.trabalhosemestral;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;


public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Spinner SpinnerCoordenadas = findViewById(R.id.spinnerCoordenadas);
        Spinner SpinnerFormatoVelocidade = findViewById(R.id.spinnerFormatoVelocidade);
        Spinner SpinnerOrientacaoMapa = findViewById(R.id.spinnerOrientacaoMapa);
        Spinner SpinnerTipoMapa = findViewById(R.id.spinnerTipoMapa);

        String coordenadas = SpinnerCoordenadas.getSelectedItem().toString();
        String formatoVelocidade = SpinnerFormatoVelocidade.getSelectedItem().toString();
        String orientacaoMapa = SpinnerOrientacaoMapa.getSelectedItem().toString();
        String tipoMapa = SpinnerTipoMapa.getSelectedItem().toString();
    }


    public void onClick(View view) {


        switch (view.getId()){
            case R.id.btnSalvarConfig:
                Intent configSalvar = new Intent(this, ConfigActivity.class);
                startActivity(configSalvar);
                break;
        }
    }
}

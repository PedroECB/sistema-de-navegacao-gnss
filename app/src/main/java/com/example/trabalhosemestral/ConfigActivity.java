package com.example.trabalhosemestral;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sharedPreferences;

    Spinner coordenadas;
    Spinner velocidade;
    Spinner orientacoesmapa;
    Spinner tiposMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);


        coordenadas = (Spinner) findViewById(R.id.spinnerCoordenadas);
        velocidade = (Spinner) findViewById(R.id.spinnerFormatoVelocidade);
        orientacoesmapa = (Spinner) findViewById(R.id.spinnerOrientacaoMapa);
        tiposMapa = (Spinner) findViewById(R.id.spinnerTipoMapa);

        ArrayAdapter arrayCoordenadas = ArrayAdapter.createFromResource(this, R.array.fomatosCoordenadas, android.R.layout.simple_spinner_item);
        ArrayAdapter arrayVelocidade = ArrayAdapter.createFromResource(this, R.array.fomatosVelocidade, android.R.layout.simple_spinner_item);
        ArrayAdapter arrayOrientacoesMapa = ArrayAdapter.createFromResource(this, R.array.orientacoesMapa, android.R.layout.simple_spinner_item);
        ArrayAdapter arrayTiposMapa = ArrayAdapter.createFromResource(this, R.array.tiposMapa, android.R.layout.simple_spinner_item);

        coordenadas.setAdapter(arrayCoordenadas);
        velocidade.setAdapter(arrayVelocidade);
        orientacoesmapa.setAdapter(arrayOrientacoesMapa);
        tiposMapa.setAdapter(arrayTiposMapa);


        Button bntSalvar = (Button) findViewById(R.id.btnSalvarConfig);
        Button bntCancelar = (Button) findViewById(R.id.btnCancelarConfig);
        bntSalvar.setOnClickListener(this);
        bntCancelar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSalvarConfig:
              /*  Spinner SpinnerCoordenadas = findViewById(R.id.spinnerCoordenadas);
                Spinner SpinnerFormatoVelocidade = findViewById(R.id.spinnerFormatoVelocidade);
                Spinner SpinnerOrientacaoMapa = findViewById(R.id.spinnerOrientacaoMapa);
                Spinner SpinnerTipoMapa = findViewById(R.id.spinnerTipoMapa);

                String coordenadas = SpinnerCoordenadas.getSelectedItem().toString();
                String formatoVelocidade = SpinnerFormatoVelocidade.getSelectedItem().toString();
                String orientacaoMapa = SpinnerOrientacaoMapa.getSelectedItem().toString();
                String tipoMapa = SpinnerTipoMapa.getSelectedItem().toString(); */


                String textCoordenadas = coordenadas.getSelectedItem().toString();
                String textVelocidade = velocidade.getSelectedItem().toString();
                String textOrientacoesMapa = orientacoesmapa.getSelectedItem().toString();
                String textTiposMapa = tiposMapa.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(), "Item escolhido: " + textCoordenadas,Toast.LENGTH_LONG).show();
                /* Toast.makeText(this, "Salvo",Toast.LENGTH_LONG).show(); */
                finish();
                break;
            case R.id.btnCancelarConfig:
                finish();
                break;
        }
    }

}

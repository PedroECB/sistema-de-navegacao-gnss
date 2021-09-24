package com.example.trabalhosemestral;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;


public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPrefsEditor;

    Spinner spinnerCoordenadas;
    Spinner spinnerVelocidade;
    Spinner spinnerOrientacoesmapa;
    Spinner spinnerTiposMapa;
    Switch switchInfoTrafego;

    ArrayAdapter arrayCoordenadas;
    ArrayAdapter arrayVelocidade;
    ArrayAdapter arrayOrientacoesMapa;
    ArrayAdapter arrayTiposMapa;

    String preferenciaCoordenadas;
    String preferenciaVelocidade;
    String preferenciaOrientacaoMapa;
    String preferenciaTipoMapa;
    String preferenciaInfoTrafego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);


        spinnerCoordenadas = (Spinner) findViewById(R.id.spinnerCoordenadas);
        spinnerVelocidade = (Spinner) findViewById(R.id.spinnerFormatoVelocidade);
        spinnerOrientacoesmapa = (Spinner) findViewById(R.id.spinnerOrientacaoMapa);
        spinnerTiposMapa = (Spinner) findViewById(R.id.spinnerTipoMapa);
        switchInfoTrafego = (Switch) findViewById(R.id.switchInformacaoTrafego);

        arrayCoordenadas = ArrayAdapter.createFromResource(this, R.array.fomatosCoordenadas, android.R.layout.simple_spinner_item);
        arrayVelocidade = ArrayAdapter.createFromResource(this, R.array.fomatosVelocidade, android.R.layout.simple_spinner_item);
        arrayOrientacoesMapa = ArrayAdapter.createFromResource(this, R.array.orientacoesMapa, android.R.layout.simple_spinner_item);
        arrayTiposMapa = ArrayAdapter.createFromResource(this, R.array.tiposMapa, android.R.layout.simple_spinner_item);

        spinnerCoordenadas.setAdapter(arrayCoordenadas);
        spinnerVelocidade.setAdapter(arrayVelocidade);
        spinnerOrientacoesmapa.setAdapter(arrayOrientacoesMapa);
        spinnerTiposMapa.setAdapter(arrayTiposMapa);


        //Obtendo as preferências salvar pelo usuários, se houver.
        String teste = arrayVelocidade.getItem(1).toString();

        Toast.makeText(this, "Texto velocidade padrão array: "+teste, Toast.LENGTH_SHORT).show();

        sharedPreferences = getSharedPreferences("UserMapPreferences", MODE_PRIVATE);

        //Se não houver valores salvos, por padrão será obtido o valor referente ao primeiro índice de cada spinner
        preferenciaCoordenadas = sharedPreferences.getString("FormatoCoordenadas", arrayCoordenadas.getItem(0).toString());
        preferenciaVelocidade = sharedPreferences.getString("FormatoVelocidade", arrayVelocidade.getItem(0).toString());
        preferenciaOrientacaoMapa = sharedPreferences.getString("OrientacaoMapa", arrayOrientacoesMapa.getItem(0).toString());
        preferenciaTipoMapa = sharedPreferences.getString("TipoMapa", arrayTiposMapa.getItem(0).toString());
        preferenciaInfoTrafego = sharedPreferences.getString("InfoTrafego", "Off");

        //Setando o valor do spinner baseado nas configurações salvas pelo usuário
        spinnerCoordenadas.setSelection(arrayCoordenadas.getPosition(preferenciaCoordenadas));
        spinnerVelocidade.setSelection(arrayVelocidade.getPosition(preferenciaVelocidade));
        spinnerOrientacoesmapa.setSelection(arrayOrientacoesMapa.getPosition(preferenciaOrientacaoMapa));
        spinnerTiposMapa.setSelection(arrayTiposMapa.getPosition(preferenciaTipoMapa));
        switchInfoTrafego.setChecked(preferenciaInfoTrafego.equals("On"));



        //Obtendo referências dos botões
        Button bntSalvar = (Button) findViewById(R.id.btnSalvarConfig);
        Button bntCancelar = (Button) findViewById(R.id.btnCancelarConfig);
        bntSalvar.setOnClickListener(this);
        bntCancelar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSalvarConfig:

                String textCoordenadas = spinnerCoordenadas.getSelectedItem().toString();
                String textVelocidade = spinnerVelocidade.getSelectedItem().toString();
                String textOrientacaoMapa = spinnerOrientacoesmapa.getSelectedItem().toString();
                String textTipoMapa = spinnerTiposMapa.getSelectedItem().toString();
                String textInfoTrafego = switchInfoTrafego.isChecked() ? "On": "Off";

                int indexCoordenadas = spinnerCoordenadas.getSelectedItemPosition();

                Toast.makeText(this, "Formato coordenadas:"+indexCoordenadas, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Formato velocidade:"+textVelocidade, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Orientação do mapa:"+textOrientacaoMapa, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Tipo de mapa:"+textTipoMapa, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Info Tráfego:"+textInfoTrafego, Toast.LENGTH_SHORT).show();


                sharedPreferences = getSharedPreferences("UserMapPreferences", MODE_PRIVATE);
                sharedPrefsEditor = sharedPreferences.edit();

                sharedPrefsEditor.putString("FormatoCoordenadas",textCoordenadas );
                sharedPrefsEditor.putString("FormatoVelocidade", textVelocidade);
                sharedPrefsEditor.putString("OrientacaoMapa", textOrientacaoMapa);
                sharedPrefsEditor.putString("TipoMapa", textTipoMapa);
                sharedPrefsEditor.putString("InfoTrafego", textInfoTrafego);

                sharedPrefsEditor.commit();




                /* Toast.makeText(this, "Salvo",Toast.LENGTH_LONG).show();
                *
                * finish();
                * */

                break;
            case R.id.btnCancelarConfig:
                finish();
                break;
        }
    }

}

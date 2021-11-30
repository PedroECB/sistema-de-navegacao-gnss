package com.example.trabalhosemestral;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MenuHistorico extends AppCompatActivity implements View.OnClickListener{

    public static final String FILE_NAME = "historico.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_historico2);


        Button botaoTabela = (Button) findViewById(R.id.btnModeTable);
        Button botaoMapa = (Button) findViewById(R.id.btnModeMap);
        Button botaoLimparHistorico = (Button) findViewById(R.id.btnLimparHistorico2);



        botaoTabela.setOnClickListener(this);
        botaoMapa.setOnClickListener(this);
        botaoLimparHistorico.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnModeTable:
                Intent historicoTabela = new Intent(this, HistoricoTabela.class);
                startActivity(historicoTabela);
                break;
            case R.id.btnModeMap:
                Intent historicoMapa = new Intent(this, HistoricoMapa.class);
                startActivity(historicoMapa);
                break;
            case R.id.btnLimparHistorico2:
                this.clearTextFile();
                Toast.makeText(this, "Limpeza do histórico concluída!", Toast.LENGTH_SHORT).show();
                break;

        }
    }



    public void clearTextFile(){

            String text = "";

            FileOutputStream fos = null;

            try {
                fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
                fos.write(text.getBytes(StandardCharsets.UTF_8));

            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Falha ao abrir arquivo",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }finally {
                //Fechando abertura de arquivo
                if(fos != null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

    }






}
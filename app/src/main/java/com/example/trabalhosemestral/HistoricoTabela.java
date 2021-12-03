package com.example.trabalhosemestral;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HistoricoTabela extends AppCompatActivity {

    public static final String FILE_NAME = "historico.txt";
    TextView labelLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_tabela);

        labelLog = (TextView) findViewById(R.id.labelLog);

        this.loadText();

    }



    public void loadText(){
        FileInputStream fis = null;

        try {

            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String text;

            //Percorrendo as linhas do arquivo
            int cont = 0;
            while((text = br.readLine()) != null){
                cont++;
                String stringLine = cont+" | "+text;
                sb.append(stringLine).append("\n");
                //Toast.makeText(this, "Arquivo com conteúdo: "+text, Toast.LENGTH_SHORT).show();
            }

            labelLog.setText(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //Fechando leitura de arquivo
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }





/*    public void clearText(){
        String text = "";

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes(StandardCharsets.UTF_8));

            Toast.makeText(this, "Linhas removidas com sucesso! "+getFilesDir()+"/"+FILE_NAME, Toast.LENGTH_LONG).show();
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

    }*/








} //Fim da classe
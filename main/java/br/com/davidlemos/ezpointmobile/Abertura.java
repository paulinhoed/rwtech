package br.com.davidlemos.ezpointmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Abertura extends AppCompatActivity implements Runnable {

    public Intent intent;
    public Context instancia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abertura);
        instancia = this;
        Handler handler = new Handler();
        handler.postDelayed(this, 3000);

    }
    public void run(){
        SharedPreferences settings = getSharedPreferences("dados", 0);
        String nomeEmpresa =  (settings.getString("nomeEmpresa", ""));

        if(nomeEmpresa.equals(""))
            chamarTela(1); // chama tela principal
        else
            chamarTela(2); // chama tela de registrar ponto

    }

    private void chamarTela(int opcao) {
        if(opcao ==1)
            intent = new Intent(instancia, MainActivity.class);
        else
            intent = new Intent(instancia, RegistrarPonto.class);
        startActivity(intent);
        finish();
    }

}

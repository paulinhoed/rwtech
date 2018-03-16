package br.com.davidlemos.ezpointmobile;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btCriarConta, btJaPossuo, btSaibaMais;
    Intent intent;
    Context instancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instancia = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        btCriarConta = (Button) findViewById(R.id.btCriarConta);
        btCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamarTela(1);
            }
        });

        btJaPossuo = (Button) findViewById(R.id.btPossuoConta);
        btJaPossuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamarTela(2);
            }
        });


        btSaibaMais = (Button) findViewById(R.id.btSaibaMais);
        btSaibaMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://rwtech.com.br/ezpointmobile"));
                startActivity(intent);
            }
        });

    }

    private void exibirMensagem( String mensagem) {
        Toast.makeText(instancia, mensagem, Toast.LENGTH_LONG).show();
    }

    private void chamarTela(int opcao) {
        if(opcao ==1)
            intent = new Intent(instancia, CriarConta.class);
        else
            intent = new Intent(instancia, DigitarNomeDaEmpresa.class);
        startActivity(intent);
        finish();
    }

}

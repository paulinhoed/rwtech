package br.com.davidlemos.ezpointmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DigitarNomeDaEmpresa extends AppCompatActivity {

    public Button btAvancar;
    public EditText etNomeDaEmpresa;
    public Context instancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digitar_nome_da_empresa);
        instancia = this;

        etNomeDaEmpresa = (EditText) findViewById(R.id.eTNomeDaEmpresa);

        btAvancar = (Button) findViewById(R.id.btAvancar);
        btAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNomeDaEmpresa.getText().toString().equals(""))
                    Toast.makeText(instancia, "Digite o nome da empresa para continuar!", Toast.LENGTH_LONG).show();
                else{
                    SharedPreferences settings = getSharedPreferences("dados", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("nomeEmpresa", etNomeDaEmpresa.getText().toString());
                    editor.commit();

                    Intent intent = new Intent(instancia, RegistrarPonto.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(instancia, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

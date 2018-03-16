package br.com.davidlemos.ezpointmobile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Desconectado extends AppCompatActivity {
    public Button btAtivado;
    Context instancia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desconetado);
        instancia = this;
        btAtivado = (Button) findViewById(R.id.btativado);
        btAtivado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(instancia, Abertura.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

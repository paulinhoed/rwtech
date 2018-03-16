package br.com.davidlemos.ezpointmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GpsDesativado extends AppCompatActivity {
    public Button btAtivado;
    Context instancia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_desativado);
        instancia = this;
        btAtivado = (Button) findViewById(R.id.btativado);
        btAtivado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (enabled) {
            SharedPreferences settings = getSharedPreferences("dados", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("retomarRegistro", true);
            editor.commit();
            Intent intent2 = new Intent(instancia, RegistrarPonto.class);
            startActivity(intent2);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent2 = new Intent(instancia, RegistrarPonto.class);
        startActivity(intent2);
        finish();
    }
}

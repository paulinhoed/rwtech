package br.com.davidlemos.ezpointmobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.com.davidlemos.ezpointmobile.model.Model;


public class MostrarDetalhesEspelhoDePonto extends AppCompatActivity {
    private Context instancia;

    private Toolbar toolbar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_espelho_de_ponto);

        instancia = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        this.overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left);
        init();

        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.azulescuro)));
        toolbarTitle.setText(R.string.title_detalhes_espelho_de_ponto);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);

        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MostrarDetalhesEspelhoDePonto.this, "Toolbar", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(instancia, EspelhoDePonto.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.anim_slide_in_right,
                                R.anim.anim_slide_out_right);
                    }
                }
        );


//
//        Bundle bundle = getIntent().getExtras();
//        Model model = bundle.getParcelable("com.example.cities.Model");
//        Log.i("model", String.valueOf(model));
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title) ;
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(instancia, EspelhoDePonto.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }
}

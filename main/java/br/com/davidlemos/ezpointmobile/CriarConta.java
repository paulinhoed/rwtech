package br.com.davidlemos.ezpointmobile;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class CriarConta extends AppCompatActivity {

    public WebView wb;
    Context instancia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);
        instancia = this;

        boolean conectado = verificarConexao();
        if(!conectado){
            Intent intent = new Intent(instancia, Desconectado.class);
            startActivity(intent);
            finish();
        }


        wb = (WebView) findViewById(R.id.webViewCriarConta);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wb.loadUrl("http://registro.ezpoint.com.br/LoginAction.do?mobile=true");
    }

    private boolean verificarConexao() {
        ConnectivityManager cm = (ConnectivityManager) instancia.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(instancia, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

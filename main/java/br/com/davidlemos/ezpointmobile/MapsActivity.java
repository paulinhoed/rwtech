package br.com.davidlemos.ezpointmobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Context instancia;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
    public int ma = 0;
    public Double mla = 0.0, mlo = 0.0;
    public ProgressDialog dialog;

    public boolean localizacao = true;
    public AlertDialog alerta, alertalocalizacao;
    public String empresa, pin;
    public TextView tv;
    private  Button bt;
    private boolean isMock = true, registroConfiavel = true;
    private int contadorMock = 0, contadorNotMock =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_mapa);
        instancia = this;

        Intent intent = getIntent();
        pin = intent.getStringExtra("pin");
        empresa = intent.getStringExtra("empresa");

        tv = (TextView) findViewById(R.id.tv);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_FINE_LOCATION);
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(instancia, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(instancia, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

       bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mla = mMap.getMyLocation().getLatitude();
                    mlo = mMap.getMyLocation().getLongitude();
                }catch (Exception e){
                    Toast.makeText(instancia, "Aguarde! Buscando sua localização.", Toast.LENGTH_SHORT).show();
                }
                if(isMock){
                    exibirAlertDialogVermelho(instancia, "Você está utilizando um programa para alterar sua localização.\nDesative-o e tente novamente!");
                }else{
                    if(!registroConfiavel){
                        exibirAlertDialogVermelho(instancia, "Opps!\nSua localização não parece muito confiável, tente novamente!");
                    }
                    else if(mla!=0 && !isMock){
                        exibirProgressDialog(instancia, "Processando solicitação!");
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm");
                        String date = sdf.format(new Date());
                        //String url = "http://www.ezpoint.com.br/ezweb-ws/registraPonto/AIzaSyCZrVbzG6jxaHmZDb9jkHK8jrohwyEq6aEZPOINTMOBILE/" + empresa + "/" + pin + "/" + mla + "/" + mlo + "/" + date;
                        //String url = "http://10.0.2.89:8080/EzWeb-Client/ezweb-ws/registraPonto/AIzaSyCZrVbzG6jxaHmZDb9jkHK8jrohwyEq6aEZPOINTMOBILE/" + empresa + "/" + pin + "/" + mla + "/" + mlo + "/" + date+"/"+ma;
                        //exibirAlertDialogVermelho(instancia, Double.toString(mla)+" "+Double.toString(mlo));
                        String url = "http://186.202.64.70:7080/EzWeb-Client/ezweb-ws/registraPonto/AIzaSyCZrVbzG6jxaHmZDb9jkHK8jrohwyEq6aEZPOINTMOBILE/" + empresa + "/" + pin + "/" + mla + "/" + mlo + "/" + date;
                        new JSONTask().execute(url);
                    }else{
                        exibirAlertDialogVermelho(instancia, "Você está utilizando um programa para alterar sua localização. Desative-o e tente novamente!");
                    }
                }

            }
        });



    }

    private void exibirProgressDialog(Context instancia, String mensagem) {
        dialog = new ProgressDialog(instancia);
        dialog.setMessage(mensagem);
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    private void exibirProgressDialog(Context instancia, String mensagem, int time) {
        dialog = new ProgressDialog(instancia);
        dialog.setMessage(mensagem);
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, time);
    }
    private void exibirAlertDialogVermelho(Context instancia, String mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(instancia);
        LayoutInflater inflater = getLayoutInflater();
        View view;
        TextView tv;
        view = inflater.inflate(R.layout.custom_title_status0, null);
        tv = (TextView) view.findViewById(R.id.tvmensagemtitle);
        tv.setText(mensagem);
        builder.setCustomTitle(view);
        builder.setTitle("Atenção");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alertalocalizacao.dismiss();
            }
        });
        alertalocalizacao = builder.create();
        alertalocalizacao.getWindow().setBackgroundDrawableResource(R.drawable.dialogvermelho);
        alertalocalizacao.show();
        Button pbutton = alertalocalizacao.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.RED);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.clear();


        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                CameraUpdate center=CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                CameraUpdate zoom=CameraUpdateFactory.zoomTo(20);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
                ma = (int) location.getAccuracy();
                tv.setText("Precisão de "+ma+" metros"  +" ");

                //Verifico se esta usando Localização falsa
                if (android.os.Build.VERSION.SDK_INT >= 18) {
                    isMock =location.isFromMockProvider();
                   // ultimasLocalizacoesFalsas.add(Double.toString(location.getLatitude())+" "+Double.toString(location.getLongitude()));
                } else {
                    isMock = !Settings.Secure.getString(instancia.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
                   // ultimasLocalizacoesFalsas.add(Double.toString(location.getLatitude())+" "+Double.toString(location.getLongitude()));
                }
                if(isMock) {
                    contadorMock++;
                    registroConfiavel = false;
                    contadorNotMock=0;
                }
                else{
                    contadorNotMock++;
                    if(contadorNotMock>2) {
                        registroConfiavel = true;
                    }
                }
                //Toast.makeText(instancia, String.valueOf(isMock) +" "+ String.valueOf(registroConfiavel)+" "+ String.valueOf(contadorMock), Toast.LENGTH_SHORT).show();
                //Toast.makeText(instancia, String.valueOf(isMock)+" "+ultimasLocalizacoesFalsas, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("resposta");

                JSONObject finalObject = parentArray.getJSONObject(0);

                int status = finalObject.getInt("status");
                String mensagem = finalObject.getString("msg");

                return mensagem + "///" + status;


            } catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if (localizacao == false) {
                AlertDialog.Builder builder = new AlertDialog.Builder(instancia);
                LayoutInflater inflater = getLayoutInflater();
                View view;
                TextView tv;
                view = inflater.inflate(R.layout.custom_title_status0, null);
                tv = (TextView) view.findViewById(R.id.tvmensagemtitle);
                tv.setText("Localização não encontrada!");
                builder.setCustomTitle(view);
                builder.setTitle("Localização não encontrada");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        alertalocalizacao.dismiss();
                        Intent intent = new Intent(instancia, RegistrarPonto.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.dismiss();
                alertalocalizacao = builder.create();
                alertalocalizacao.getWindow().setBackgroundDrawableResource(R.drawable.dialogvermelho);
                Button pbutton = alertalocalizacao.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.RED);
            }

            if (result == null) {
                result = "Erro ao conectar ao servidor, tente novamente!///0";
            }
            String[] rsp = result.split("///");
            String mensagem = rsp[0];
            final int status = Integer.parseInt(rsp[1]);

            AlertDialog.Builder builder = new AlertDialog.Builder(instancia);
            LayoutInflater inflater = getLayoutInflater();
            View view;
            TextView tv;
            if (status == 0) {
                view = inflater.inflate(R.layout.custom_title_status0, null);
                tv = (TextView) view.findViewById(R.id.tvmensagemtitle);
            } else {
                view = inflater.inflate(R.layout.custom_title_status1, null);
                tv = (TextView) view.findViewById(R.id.tvmensagemtitle1);
            }
            tv.setText(mensagem);
            builder.setCustomTitle(view);
            builder.setTitle(mensagem);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    alerta.dismiss();
                    Intent intent = new Intent(instancia, RegistrarPonto.class);
                    startActivity(intent);
                    finish();
                }
            });
            alerta = builder.create();
            if (status == 0) {
                alerta.getWindow().setBackgroundDrawableResource(R.drawable.dialogvermelho);
            }
            else
                alerta.getWindow().setBackgroundDrawableResource(R.drawable.dialogverde);
            alerta.setCancelable(false);
            dialog.dismiss();
            alerta.show();
            if (status == 0) {
                Button pbutton = alerta.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.RED);
            }
            if (localizacao == false) {
                alertalocalizacao.show();
                localizacao = true;
            }
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(instancia);
        LayoutInflater inflater = getLayoutInflater();
        View view;
        TextView tv;
        view = inflater.inflate(R.layout.custom_title_status0, null);
        tv = (TextView) view.findViewById(R.id.tvmensagemtitle);
        tv.setText("Atenção!\n Se retornar seu ponto não será registrado!");
        builder.setCustomTitle(view);
        builder.setTitle("Localização não encontrada");
        builder.setPositiveButton("RETORNAR MESMO ASSIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alerta.dismiss();
                Intent intent = new Intent(instancia, RegistrarPonto.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alerta.dismiss();
            }
        });
        alerta = builder.create();
        alerta.getWindow().setBackgroundDrawableResource(R.drawable.dialogvermelho);
        alerta.show();
        Button pbutton = alerta.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.RED);
        Button nbutton = alerta.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.RED);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        exibirProgressDialog(instancia, "Atualizando Mapa", 5000);

    }
}


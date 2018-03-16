package br.com.davidlemos.ezpointmobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;



public class RegistrarPonto extends AppCompatActivity {
   public Context instancia;
    public TextView tvNomeEmpresa, tvPIN;
    public Button bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt0, btLimpar, btAlterarEmpregador, btRegistrarPonto;
    public String mac, empresa, pin;
    public boolean registrarPonto = false, verificarBloqueio = true, localizacao = true, gpsobrigatorio = false;
    public AlertDialog alerta, alertalocalizacao;
    public boolean retomarRegistro = false;
    private boolean requergps = true;
    public ProgressDialog dialog;


    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).getSubMenu().getItem(0).setEnabled(false).setVisible(false);
        menu.getItem(0).getSubMenu().getItem(2).setEnabled(false).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_registrar_ponto:
                //Toast.makeText(this, "Registrar Ponto", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_espelho_ponto:
                //Toast.makeText(this, "Espelho de Pontos", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(instancia, EspelhoDePonto.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_sincronizar_ponto:
                //Toast.makeText(this, "Espelho de Pontos", Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_alterar_empregador:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_alterar_empregador)
                        .setMessage(R.string.dialog_alterar_empregador)
                        .setIcon(R.drawable.ic_error_black_24dp)
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Toast.makeText(this, "Alterar Empregador", Toast.LENGTH_LONG).show();
                                SharedPreferences settings = getSharedPreferences("dados", 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("nomeEmpresa", "");
                                editor.commit();

                                Intent intent = new Intent(instancia, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }})
                        .setNegativeButton(R.string.nao, null).show();

                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_ponto);
        instancia = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title) ;
        //toolbar.setNavigationIcon(R.drawable.ic_fingerprint);


        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.azulescuro)));
        textView.setText(R.string.title_registrar_ponto);


//        toolbar.setNavigationOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(RegistrarPonto.this, "Toolbar", Toast.LENGTH_SHORT).show();
//                        PopupMenu popupMenu = new PopupMenu(RegistrarPonto.this, v);
//                        popupMenu.inflate(R.menu.menu_main);
//                        popupMenu.show();
//                    }
//                }
//        );

        //Obter endereço MAC
        mac = getMacAddr();

        //CHECK PERMISSION PARA ANDROID 6 OU SUPERIOR
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(instancia, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(instancia, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }


        //Restaura as preferencias gravadas
        SharedPreferences settings = getSharedPreferences("dados", 0);
        empresa = settings.getString("nomeEmpresa", "");
        retomarRegistro = settings.getBoolean("retomarRegistro", false);


        tvNomeEmpresa = (TextView)findViewById(R.id.tvNomeEmpresa);
        tvNomeEmpresa.setText(empresa.toString());
        empresa = empresa.replace(" ", "");

        //Botões numericos
        tvPIN = (TextView)findViewById(R.id.tvPin);
        bt1 = (Button)findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = tvPIN.getText().toString() + 1;
                tvPIN.setText(pin);
            }
        });
        bt2 = (Button)findViewById(R.id.bt2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = tvPIN.getText().toString() + 2;
                tvPIN.setText(pin);
            }
        });
        bt3 = (Button)findViewById(R.id.bt3);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = tvPIN.getText().toString() + 3;
                tvPIN.setText(pin);
            }
        });
        bt4 = (Button)findViewById(R.id.bt4);
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = tvPIN.getText().toString() + 4;
                tvPIN.setText(pin);
            }
        });
        bt5 = (Button)findViewById(R.id.bt5);
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = tvPIN.getText().toString() + 5;
                tvPIN.setText(pin);
            }
        });
        bt6 = (Button)findViewById(R.id.bt6);
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = tvPIN.getText().toString() + 6;
                tvPIN.setText(pin);
            }
        });
        bt7 = (Button)findViewById(R.id.bt7);
        bt7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = tvPIN.getText().toString() + 7;
                tvPIN.setText(pin);
            }
        });
        bt8 = (Button)findViewById(R.id.bt8);
        bt8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = tvPIN.getText().toString() + 8;
                tvPIN.setText(pin);
            }
        });
        bt9 = (Button)findViewById(R.id.bt9);
        bt9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = tvPIN.getText().toString() + 9;
                tvPIN.setText(pin);
            }
        });
        bt0 = (Button)findViewById(R.id.bt0);
        bt0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = tvPIN.getText().toString() + 0;
                tvPIN.setText(pin);
            }
        });
        btLimpar = (Button)findViewById(R.id.btlimpar);
        btLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPIN.setText("");
            }
        });


        btRegistrarPonto = (Button)findViewById(R.id.bt_registrar_ponto);
        btRegistrarPonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvPIN.getText().toString().equals("")) {
                    Toast.makeText(instancia, "DIGITE SEU PIN!", Toast.LENGTH_LONG).show();
                } else {
                    boolean internetAtivada = verificarConexao();
                    if (internetAtivada) {
                        verificarBloqueio = true;
                        registrarPonto = false;
                        exibirProgressDialog(instancia, "Processando solicitação!");
                        String url = "http://186.202.64.70:7080/EzWeb-Client/ezweb-ws/verificaBloqueio/AIzaSyCZrVbzG6jxaHmZDb9jkHK8jrohwyEq6aEZPOINTMOBILE/"+empresa;
                        new JSONTask().execute(url);
                    } else {
                        Intent intent = new Intent(instancia, Desconectado.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        if(retomarRegistro){
            exibirProgressDialog(instancia, "Processando solicitação!");
            pin = settings.getString("pinEmpresa", "");
            mac = settings.getString("mac", "");
            validaDados();
        }

//        btAlterarEmpregador = (Button)findViewById(R.id.btAlterarEmpregador);
//        btAlterarEmpregador.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences settings = getSharedPreferences("dados", 0);
//                SharedPreferences.Editor editor = settings.edit();
//                editor.putString("nomeEmpresa", "");
//                editor.commit();
//
//                Intent intent = new Intent(instancia, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

    }

    private void exibirProgressDialog(Context instancia, String mensagem) {
        dialog = new ProgressDialog(instancia);
        dialog.setMessage(mensagem);
        dialog.setIndeterminate(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }


    //Método que faz a leitura de fato dos valores recebidos do GPS
    public boolean verificarGPS() {
        LocationManager service = (LocationManager)getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return enabled;
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

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();
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


            if(localizacao==false) {
                AlertDialog.Builder builder = new AlertDialog.Builder(instancia);
                LayoutInflater inflater = getLayoutInflater();
                View view;
                TextView tv;
                view = inflater.inflate(R.layout.custom_title_status0, null);
                tv = (TextView)view.findViewById(R.id.tvmensagemtitle);
                tv.setText("Localização não encontrada!");
                builder.setCustomTitle(view);
                builder.setTitle("Localização não encontrada");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        alertalocalizacao.dismiss();
                    }
                });
                dialog.dismiss();
                alertalocalizacao = builder.create();
                alertalocalizacao.getWindow().setBackgroundDrawableResource(R.drawable.dialogvermelho);
                Button pbutton = alertalocalizacao.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.RED);
            }

            if (result == null) {
                dialog.dismiss();
                result = "Erro ao conectar ao servidor, tente novamente!///0";
            }
            String[] rsp = result.split("///");
            String mensagem = rsp[0];
            int status = Integer.parseInt(rsp[1]);

            //SE FOR A PRIMEIRA CHAMADA VERIFICA SE IRÁ NECESSITAR USAR O GPS OU SE A EMPRESA EXISTE
            if (verificarBloqueio){
                //ALERTA AO USUARIO QUE A EMPRESA NÃO EXITE
                if (status == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(instancia);
                    LayoutInflater inflater = getLayoutInflater();
                    View view;
                    TextView tv;
                    view = inflater.inflate(R.layout.custom_title_status0, null);
                    tv = (TextView)view.findViewById(R.id.tvmensagemtitle);
                    tv.setText(mensagem);
                    builder.setCustomTitle(view);
                    builder.setTitle(mensagem);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            alerta.dismiss();
                        }
                    });
                    alerta = builder.create();
                    alerta.getWindow().setBackgroundDrawableResource(R.drawable.dialogvermelho);
                    alerta.setCancelable(false);
                    dialog.dismiss();
                    alerta.show();
                    Button pbutton = alerta.getButton(DialogInterface.BUTTON_POSITIVE);
                    pbutton.setTextColor(Color.RED);
                }
                //SE A EMPRESA EXISTIR VERIFICA COM O USUÁRIO SE QUER UTILIZAR O GPS
                else if (status == 1){
                    boolean gpsAtivado = verificarGPS();
                    if (!gpsAtivado) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(instancia);
                        View view = View.inflate(instancia, R.layout.custom_title_status1, null);
                        TextView tv = (TextView)view.findViewById(R.id.tvmensagemtitle1);
                        tv.setText(mensagem);
                        builder.setCustomTitle(view);
                        builder.setPositiveButton("Desejo ativar!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                alerta.dismiss();
                                exibirProgressDialog(instancia, "Processando solicitação!");
                                //Abre a tela para ativar o GPS
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                requergps = true;
                                validaDados();
                            }
                        });
                        builder.setNegativeButton("Não, obrigado!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                alerta.dismiss();
                                exibirProgressDialog(instancia, "Processando solicitação!");
                                requergps = false;
                                validaDados();
                            }
                        });
                        alerta = builder.create();
                        alerta.getWindow().setBackgroundDrawableResource(R.drawable.dialogverde);
                        dialog.dismiss();
                        alerta.show();
                    } else {
                        validaDados();
                    }
                }
                //É NECESSÁRIO A UTILIZAÇÃO DO GPS, SE NÃO ESTIVER ATIVADO IRÁ PARA A TELA DE AVISO DE GPS DESATIVADO
                else {
                    boolean gpsAtivado = verificarGPS();
                    if (!gpsAtivado) {
                        SharedPreferences settings = getSharedPreferences("dados", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("pinEmpresa", tvPIN.getText().toString());
                        editor.putString("mac", mac);
                        editor.commit();

                        Intent intent = new Intent(instancia, GpsDesativado.class);
                        startActivity(intent);
                        //dialog.dismiss();
                        finish();
                    } else {
                        gpsobrigatorio =true;
                        requergps =true;
                        validaDados();
                    }
                }
            }
            //APÓS A PRIMEIRA CHAMADA - CRIA ALERTAS PARA O VALIDA DADOS E REGISTRA PONTO
            else {
                //ALERTAS PARA O REGISTRO DE PONTO
                if (registrarPonto){
                    registrarPonto = false;
                    AlertDialog.Builder builder = new AlertDialog.Builder(instancia);
                    LayoutInflater inflater = getLayoutInflater();
                    View view;
                    TextView tv;
                    if (status == 0) {
                        view = inflater.inflate(R.layout.custom_title_status0, null);
                        tv = (TextView)view.findViewById(R.id.tvmensagemtitle);
                    } else {
                        view = inflater.inflate(R.layout.custom_title_status1, null);
                        tv = (TextView)view.findViewById(R.id.tvmensagemtitle1);
                    }
                    tv.setText(mensagem);
                    builder.setCustomTitle(view);
                    builder.setTitle(mensagem);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            alerta.dismiss();
                        }
                    });
                    alerta = builder.create();
                    if (status == 0)
                        alerta.getWindow().setBackgroundDrawableResource(R.drawable.dialogvermelho);
                    else
                        alerta.getWindow().setBackgroundDrawableResource(R.drawable.dialogverde);
                    alerta.setCancelable(false);
                    dialog.dismiss();
                    alerta.show();
                    if (status == 0) {
                        Button pbutton = alerta.getButton(DialogInterface.BUTTON_POSITIVE);
                        pbutton.setTextColor(Color.RED);
                    }
                    if(localizacao==false){
                        alertalocalizacao.show();
                        localizacao=true;
                    }
                }
                //ALERTAS PARA O VALIDA DADOS
                else {
                    if (status == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(instancia);
                        View view = View.inflate(instancia, R.layout.custom_title_status0, null);
                        TextView tv = (TextView)view.findViewById(R.id.tvmensagemtitle);
                        tv.setText(mensagem);
                        builder.setCustomTitle(view);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                alerta.dismiss();
                            }
                        });
                        alerta = builder.create();
                        alerta.getWindow().setBackgroundDrawableResource(R.drawable.dialogvermelho);
                        alerta.setCancelable(false);
                        dialog.dismiss();
                        alerta.show();
                        Button pbutton = alerta.getButton(DialogInterface.BUTTON_POSITIVE);
                        pbutton.setTextColor(Color.RED);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(instancia);
                        View view = View.inflate(instancia, R.layout.custom_title_status1, null);
                        TextView tv = (TextView)view.findViewById(R.id.tvmensagemtitle1);
                        tv.setText(mensagem);
                        builder.setCustomTitle(view);
                        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                alerta.dismiss();
                                registrarPonto();
                            }
                        });
                        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                alerta.dismiss();
                            }
                        });
                        alerta = builder.create();
                        alerta.getWindow().setBackgroundDrawableResource(R.drawable.dialogverde);
                        alerta.setCancelable(false);
                        if(dialog!=null)
                        dialog.dismiss();
                        alerta.show();
                    }
                }
            }
        }

    }

    private void validaDados() {
        verificarBloqueio = false;

        if(retomarRegistro){
            SharedPreferences settings = getSharedPreferences("dados", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("pinEmpresa", "");
            editor.putString("mac", "");
            editor.putBoolean("retomarRegistro", false);
            editor.commit();
            retomarRegistro = false;
        }

        //url valida dados
        String url = "http://186.202.64.70:7080/EzWeb-Client/ezweb-ws/validaDados/AIzaSyCZrVbzG6jxaHmZDb9jkHK8jrohwyEq6aEZPOINTMOBILE/"+empresa+"/"+pin+"/"+mac;
        new JSONTask().execute(url);
    }

    public void registrarPonto() {

        //VERFICA SE O GPS É OBRIGATORIO E SE ESTA ATIVADO
       if(gpsobrigatorio && !verificarGPS()){
            //gpsdesativado = true;
           Intent intent = new Intent(instancia, GpsDesativado.class);
           startActivity(intent);
            //finish();
       }
       else{
            //SE O USUARIO OPTOU POR UTILIZAR O GPS OU SEU USO FOR OBRIGATÓRIO
            if (requergps) {
                dialog.dismiss();
                exibirProgressDialog(instancia, "Processando Solicitação!");
                Intent intent = new Intent(instancia, MapsActivity.class);
                intent.putExtra("pin", pin);
                intent.putExtra("empresa", empresa);
                startActivity(intent);
                finish();
            }
            else{
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH:mm");
                String date = sdf.format(new Date());
                exibirProgressDialog(instancia, "Processando solicitação!");
                String url = "http://186.202.64.70:7080/EzWeb-Client/ezweb-ws/registraPonto/AIzaSyCZrVbzG6jxaHmZDb9jkHK8jrohwyEq6aEZPOINTMOBILE/"+empresa+"/"+pin+"/0.0/0.0/"+date;
                registrarPonto = true;
                gpsobrigatorio = false;
                new JSONTask().execute(url);

            }
        }

    }


    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(instancia, MainActivity.class);
        startActivity(intent);
        finish();
    }

}



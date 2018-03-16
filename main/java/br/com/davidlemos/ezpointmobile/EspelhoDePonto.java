package br.com.davidlemos.ezpointmobile;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.davidlemos.ezpointmobile.adapter.Adapter;
import br.com.davidlemos.ezpointmobile.adapters.PontoAdapter;
import br.com.davidlemos.ezpointmobile.model.Model;
import br.com.davidlemos.ezpointmobile.models.Ponto;
import br.com.davidlemos.ezpointmobile.tasks.PontoEvents;
import br.com.davidlemos.ezpointmobile.tasks.PontoTasks;
import br.com.davidlemos.ezpointmobile.util.ToastMessage;
import br.com.davidlemos.ezpointmobile.webservice.CheckNetworkConnection;
import br.com.davidlemos.ezpointmobile.webservice.WebServiceResponse;


public class EspelhoDePonto extends AppCompatActivity implements PontoEvents{
    private Context instancia;
    private Calendar calendarMin = Calendar.getInstance();
    private Calendar calendarMax = Calendar.getInstance();
    private Calendar calendarToday = Calendar.getInstance();
    private EditText minDateText;
    private EditText maxDateText;
    private DatePickerDialog.OnDateSetListener datePickerMin;
    private DatePickerDialog.OnDateSetListener datePickerMax;
    private ImageButton btnFilterRegisters;
    private ListView listView;
    private TextView noRegisters;
    private Adapter adapter;
    private Toolbar toolbar;
    private TextView textView;
    private int onStartCount = 0;
    private View rootView;

    private ListView listViewPontos;
    private ArrayList<Ponto> pontos;

    Map<Integer, Ponto> pontosMap;

    public EspelhoDePonto() {
        pontosMap = new HashMap<Integer, Ponto>();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).getSubMenu().getItem(1).setEnabled(false).setVisible(false);
        menu.getItem(0).getSubMenu().getItem(2).setEnabled(false).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_registrar_ponto:
                //Toast.makeText(this, "Registrar Ponto", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(instancia, RegistrarPonto.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_espelho_ponto:
                //Toast.makeText(this, "Espelho de Pontos", Toast.LENGTH_LONG).show();
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_espelho_de_ponto,
                container, false);

//        if (CheckNetworkConnection.isNetworkConnected(this)) {
//            PontoTasks pontoTasks = new PontoTasks(this, this);
//            pontoTasks.getPontos();
//        }

        return rootView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espelho_de_ponto);
        instancia = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        init();

        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.azulescuro)));
        textView.setText(R.string.title_espelho_de_ponto);

        setTextDateWith30daysOfDiff();
//        filterRegisters(minDate.getText(), maxDate.getText());//AQUI VAI FAZER O FILTRO DOS REGISTROS
                                                                //PARA 30 DIAS QUANDO ABRIR O ESPELHO

        datePickerMin = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                String minDateAttempt = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;// monthOfYear+1 because January starts with 0
                Calendar dateAttempt = convertTextToDate(minDateAttempt);
                Calendar minDateAllowed = convertTextToDate(String.valueOf(minDateText.getText()));

                int diffDaysToToday = getDifferenceOfDaysFromTwoDates(calendarToday,dateAttempt);
                if(diffDaysToToday > 0)
                    callDialog(1);
                else{
                    setCalendarMin(calendarMin, year, monthOfYear, dayOfMonth);
                    updateDateLabel(minDateText, calendarMin,false);
                }
            }
        };
        datePickerMax = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                String maxDateAttempt = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;// monthOfYear+1 because January starts with 0
                Calendar dateAttempt = convertTextToDate(maxDateAttempt);
                Calendar maxDateAllowed = convertTextToDate(String.valueOf(maxDateText.getText()));

                int diffDaysToToday = getDifferenceOfDaysFromTwoDates(calendarToday,dateAttempt);
                if(diffDaysToToday > 0)
                    callDialog(0);
                else{
                    setCalendarMax(calendarMax, year, monthOfYear, dayOfMonth);
                    updateDateLabel(maxDateText, calendarMax,false);
                }
            }
        };
        minDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EspelhoDePonto.this, datePickerMin, calendarMin
                        .get(Calendar.YEAR), calendarMin.get(Calendar.MONTH),
                        calendarMin.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        maxDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EspelhoDePonto.this, datePickerMax, calendarMax
                        .get(Calendar.YEAR), calendarMax.get(Calendar.MONTH),
                        calendarMax.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnFilterRegisters.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    int diffDaysBetweenDates = getDifferenceOfDaysFromTwoDates(calendarMin,calendarMax);
                    if(diffDaysBetweenDates < 0)
                        callDialog(2);
                    else if(diffDaysBetweenDates > 40)
                        callDialog(3);
                    else
                        filterRegisters(minDateText.getText(), maxDateText.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        if (CheckNetworkConnection.isNetworkConnected(this)) {
//            PontoTasks pontoTasks = new PontoTasks(this, this);
//            pontoTasks.getPontos();
//        }
    }

    private int getDifferenceOfDaysFromTwoDates(Calendar fixedCal,  Calendar attemptCal) {
        int fixedYear = fixedCal.get(Calendar.YEAR);
        int fixedMonth = fixedCal.get(Calendar.MONTH);
        int fixedDay = fixedCal.get(Calendar.DAY_OF_MONTH);

        int attemptYear = attemptCal.get(Calendar.YEAR);
        int attemptMonth = attemptCal.get(Calendar.MONTH);
        int attemptDay = attemptCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(fixedYear, fixedMonth, fixedDay);
        date2.clear();
        date2.set(attemptYear, attemptMonth, attemptDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();
        long daysDiff = diff / (24 * 60 * 60 * 1000);
        return (int) daysDiff;
    }

    private void setCalendarMin(Calendar calendar, int year, int monthOfYear, int dayOfMonth) {
        calendarMin.set(Calendar.YEAR, year);
        calendarMin.set(Calendar.MONTH, monthOfYear);
        calendarMin.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    private void setCalendarMax(Calendar calendar, int year, int monthOfYear, int dayOfMonth) {
        calendarMax.set(Calendar.YEAR, year);
        calendarMax.set(Calendar.MONTH, monthOfYear);
        calendarMax.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    private void setTextDateWith30daysOfDiff() {
        calendarMin.add(calendarMin.MONTH, -1);
        calendarMax.add(calendarMax.MONTH, 0);
        updateDateLabel(minDateText,calendarMin,false);
        updateDateLabel(maxDateText,calendarMax,true);
    }

    private void updateDateLabel(EditText thisDate, Calendar calendar, Boolean now) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ROOT);
        if(!now){
            thisDate.setText(sdf.format(calendar.getTime()));
        }
        else {
            Calendar cal = Calendar.getInstance();
            thisDate.setText(sdf.format(cal.getTime()));
        }
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textView = (TextView) findViewById(R.id.toolbar_title) ;
        minDateText = (EditText) findViewById(R.id.item_min_date);
        maxDateText = (EditText) findViewById(R.id.item_max_date);
        btnFilterRegisters = (ImageButton) findViewById(R.id.btn_filter_registers);
        listView = (ListView) findViewById(R.id.list_espelho_ponto);
        noRegisters = (TextView) findViewById(R.id.no_registers);
    }

    private void filterRegistersByDate(Editable startDate, Editable finalDate) {
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent,
                                            View view, int position, long id) {
                        Ponto pontoSelected = (Ponto)
                                listView.getItemAtPosition(position);
                        startPontoDetail(pontoSelected.getId());
                    }
                });
        
        if (CheckNetworkConnection.isNetworkConnected(this)) {
            PontoTasks pontoTasks = new PontoTasks(this, this);
            pontoTasks.getPontos();
        }
    }

    private void startPontoDetail(int pontoId) {
//        Class fragmentClass;
//        Fragment fragment = null;
//        fragmentClass = MostrarDetalhesEspelhoDePonto.class;
//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//            if (pontoId >= 0) {
//                Bundle args = new Bundle();
//
//                args.putSerializable("ponto", pontosMap.get(pontoId));
//                fragment.setArguments(args);
//            }
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction transaction =
//                    fragmentManager.beginTransaction();
//            transaction.replace(this, fragment,
//                    MostrarDetalhesEspelhoDePonto.class.getCanonicalName());
//            transaction.addToBackStack(
//                    MostrarDetalhesEspelhoDePonto.class.getCanonicalName());
//            transaction.commit();
//
//        } catch (Exception e) {
//            try {
//                Toast.makeText(this,
//                        "Erro ao tentar abrir detalhes do ponto",
//                        Toast.LENGTH_SHORT).show();
//            } catch (Exception e1) {
//                System.out.println(e1.getMessage());
//            }
//        }
    }
    private void filterRegisters(Editable minDate, Editable maxDate) {
        Log.i("min date: ", String.valueOf(minDate));
        Log.i("max date: ", String.valueOf(maxDate));

        adapter = new Adapter(this, generateData());
        if(adapter.getCount() > 1)
            noRegisters.setVisibility(View.GONE);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
//            {
                public void onItemClick(AdapterView<?> a,
                        View v, int position, long id) {
                Toast.makeText(EspelhoDePonto.this, "" + position, Toast.LENGTH_SHORT).show();
//                Model model = (Model) a.getItemAtPosition(position);
                Intent intent = new Intent(v.getContext(), MostrarDetalhesEspelhoDePonto.class);
//                intent.putExtra("com.example.cities.City", (Serializable) model);
//                startActivity(intent);
                //Intent intent = new Intent(instancia, MostrarDetalhesEspelhoDePonto.class);
                startActivity(intent);

            }
        });
    }

    private Calendar convertTextToDate(String dateToConvert){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(dateToConvert);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    private ArrayList<Model> generateData(){
        ArrayList<Model> models = new ArrayList<Model>();
        models.add(new Model("Group Title"));

        Model model1 = new Model(R.drawable.ic_arrow_forward_blue,"01/02/2018 - QUINTA-FEIRA","10:30","2:00");
        Model model2 = new Model(R.drawable.ic_arrow_forward_blue,"02/02/2018 - SEGUNDA-FEIRA","2:40","1:00");
        Model model3 = new Model(R.drawable.ic_arrow_forward_blue,"03/02/2018 - FERIADO","12:00","10:00");

        models.add(model1);
        models.add(model2);
        models.add(model3);

        return models;
    }
    private void callDialog(int option) {
        int title = 0;
        int message = 0;
        int btnOk = 0;
        int icon = 0;
        switch(option) {
            case 0:
                title = R.string.incorrect_max_date;
                message = R.string.dialog_error_max_date;
                btnOk = R.string.ok;
                icon = R.drawable.ic_warning_yellow;
            break;
            case 1:
                title = R.string.incorrect_min_date;
                message = R.string.dialog_error_min_date;
                btnOk = R.string.ok;
                icon = R.drawable.ic_warning_yellow;
            break;
            case 2:
                title = R.string.negative_diff_bet_dates;
                message = R.string.dialog_error_negative_diff_bet_dates;
                btnOk = R.string.ok;
                icon = R.drawable.ic_warning_yellow;
                break;
            case 3:
                title = R.string.forth_diff_bet_dates;
                message = R.string.dialog_error_forth_diff_bet_dates;
                btnOk = R.string.ok;
                icon = R.drawable.ic_warning_yellow;
            break;
        }
        new AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setIcon(icon)
            .setPositiveButton(btnOk, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }}).show();
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(instancia, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void getPontoFinished(List<Ponto> pontos) {
        for (Ponto ponto : pontos) {
            pontosMap.put(ponto.getId(), ponto);
        }

        PontoAdapter pontoAdapter = new PontoAdapter(
                this, pontos);
        listView.setAdapter(pontoAdapter);
    }

    @Override
    public void getPontoFailed(WebServiceResponse webServiceResponse) {
        ToastMessage.Msg(this, "Erro ao tentar exibir a lista de todos os pontos.");
        
    }

    @Override
    public void getPontoByIdFinished(Ponto pontos) {

    }

    @Override
    public void getPontoByIdFailed(WebServiceResponse webServiceResponse) {

    }
}



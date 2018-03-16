package br.com.davidlemos.ezpointmobile.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import br.com.davidlemos.ezpointmobile.util.WSUtil;
import br.com.davidlemos.ezpointmobile.models.Ponto;
import br.com.davidlemos.ezpointmobile.webservice.WebServiceClient;
import br.com.davidlemos.ezpointmobile.webservice.WebServiceResponse;

public class PontoTasks {

    private static final String GET_PONTOS = "/listaPontos";
    private static final String GET_PONTO_BY_ID = "/listaPontos";
    private static final String HASH_CODE = "/AIzaSyCZrVbzG6jxaHmZDb9jkHK8jrohwyEq6aEZPOINTMOBILE";

    private PontoEvents pontoEvents;
    private Context context;
    private String baseAddress;

    public PontoTasks(Context context, PontoEvents pontoEvents) {
        String host;
        int port;
        this.context = context;
        this.pontoEvents = pontoEvents;
        baseAddress = WSUtil.getHostAddress(context);
    }

    public void getPontos() {
        new AsyncTask<Void, Void, WebServiceResponse>() {

            @Override
            protected WebServiceResponse doInBackground(Void... params) {
                return WebServiceClient.get(context,
                        baseAddress + GET_PONTOS + HASH_CODE);
            }

            @Override
            protected void onPostExecute(
                    WebServiceResponse webServiceResponse) {
                if (webServiceResponse.getResponseCode() == 200) {
                    Gson gson = new Gson();
                    try {
                        List<Ponto> pontos = gson.fromJson(
                                webServiceResponse.getResultMessage(),
                                new TypeToken<List<Ponto>>() {
                                }.getType());
                        pontoEvents.getPontoFinished(pontos);
                    } catch (Exception e) {
                        pontoEvents.getPontoFailed(webServiceResponse);
                    }
                } else {
                    pontoEvents.getPontoFailed(webServiceResponse);
                }
            }
        }.execute(null, null, null);
    }

    public void getPontoById(int id) {
        new AsyncTask<Integer, Void, WebServiceResponse>() {
            @Override
            protected WebServiceResponse doInBackground(Integer... id) {
                return WebServiceClient.get(context,
                        baseAddress + GET_PONTO_BY_ID + "/" +
                                Integer.toString(id[0]));
            }

            @Override
            protected void onPostExecute(
                    WebServiceResponse webServiceResponse) {
                if (webServiceResponse.getResponseCode() == 200) {
                    Gson gson = new Gson();
                    try {
                        Ponto ponto = gson.fromJson(
                                webServiceResponse.getResultMessage(),
                                Ponto.class);
                        pontoEvents.getPontoByIdFinished(ponto);
                    } catch (Exception e) {
                        pontoEvents.getPontoByIdFailed(webServiceResponse);
                    }
                } else {
                    pontoEvents.getPontoByIdFailed(webServiceResponse);
                }
            }
        }.execute(id, null, null);
    }
}
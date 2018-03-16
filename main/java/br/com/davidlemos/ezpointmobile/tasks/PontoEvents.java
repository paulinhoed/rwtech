package br.com.davidlemos.ezpointmobile.tasks;

import java.util.List;

import br.com.davidlemos.ezpointmobile.models.Ponto;
import br.com.davidlemos.ezpointmobile.webservice.WebServiceResponse;

public interface PontoEvents {
    void getPontoFinished(List<Ponto> pontos);

    void getPontoFailed(WebServiceResponse webServiceResponse);

    void getPontoByIdFinished(Ponto pontos);

    void getPontoByIdFailed(WebServiceResponse webServiceResponse);
}

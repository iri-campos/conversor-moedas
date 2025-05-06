package br.com.alura.conversormoedas.modelos;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class ConsultaTaxaDeCambio {
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private final HttpClient client;
    private final String apiKey;

    public ConsultaTaxaDeCambio() {
        this.client = HttpClient.newHttpClient();
        this.apiKey = carregarApiKey();
    }

    private String carregarApiKey() {
        Properties props = new Properties();
        try {
            FileInputStream file = new FileInputStream("src/config.properties");
            props.load(file);
            return props.getProperty("api.key");
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar a chave da API: " + e.getMessage());
        }
    }

    public double obterTaxa(String moedaOrigem, String moedaDestino) throws IOException, InterruptedException {
        String url = BASE_URL + apiKey + "/latest/" + moedaOrigem;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Erro na API: código " + response.statusCode());
        }

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        JsonObject rates = json.getAsJsonObject("conversion_rates");

        return rates.get(moedaDestino).getAsDouble();
    }
}

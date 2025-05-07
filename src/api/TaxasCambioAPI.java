package api;

import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Properties;

public class TaxasCambioAPI {
    private final String apiKey;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public TaxasCambioAPI() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("src/config.properties")) {
            properties.load(input);
            this.apiKey = properties.getProperty("apiKey");

            if(this.apiKey == null) {
                throw new IllegalArgumentException("Chave da API não encontrada.");
            }
        }
    }

    public Map<String, Double> getTaxas(String baseCurrency) throws IOException, InterruptedException {
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + baseCurrency;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Map<String, Object> json = gson.fromJson(response.body(), Map.class);
        return (Map<String, Double>) json.get("conversion_rates");
    }

    public double converte(String base, String target, double amount) throws IOException, InterruptedException {
        Map<String, Double> rates = getTaxas(base);
        return amount * rates.get(target);
    }
}


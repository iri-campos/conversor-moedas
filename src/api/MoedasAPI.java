package api;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MoedasAPI {
    private final String apiKey;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public MoedasAPI() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("src/config.properties")) {
            properties.load(input);
            this.apiKey = properties.getProperty("apiKey");
        }
    }

    public Map<String, String> getMoedas() throws IOException, InterruptedException {
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/codes";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Map<String, Object> json = gson.fromJson(response.body(), Map.class);
        List<List<String>> codigo = (List<List<String>>) json.get("supported_codes");

        Map<String, String> moedaMapa = new LinkedHashMap<>();
        for (List<String> codePair : codigo) {
            moedaMapa.put(codePair.get(0),codePair.get(1));
        }
        return moedaMapa;
    }

}

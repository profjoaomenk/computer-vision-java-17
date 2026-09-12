package br.com.profjoao.cognitivevision.service;

import br.com.profjoao.cognitivevision.model.AnalyzeResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class VisionService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public VisionService(ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = objectMapper;
    }

    public AnalyzeResponse analyze(String url) {

        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException(
                    "Informe a URL pública de uma imagem."
            );
        }

        String endpoint = System.getenv("VISION_ENDPOINT");
        String key = System.getenv("VISION_KEY");

        if (endpoint == null || endpoint.isBlank()) {
            throw new IllegalStateException(
                    "VISION_ENDPOINT não foi configurado."
            );
        }

        if (key == null || key.isBlank()) {
            throw new IllegalStateException(
                    "VISION_KEY não foi configurado."
            );
        }

        // Limpa o endpoint
        endpoint = endpoint.trim()
                .replace("\"", "")
                .replaceAll("/+$", "");

        // Limpa a chave
        key = key.trim()
                .replace("\"", "");

        String apiUrl = endpoint
                + "/computervision/imageanalysis:analyze"
                + "?features=caption,tags"
                + "&api-version=2024-02-01";

        String jsonBody = """
                {
                    "url": "%s"
                }
                """.formatted(
                url.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
        );

        System.out.println("======================================");
        System.out.println("Azure Vision - Requisição");
        System.out.println("Endpoint: " + endpoint);
        System.out.println("API URL: " + apiUrl);
        System.out.println("URL da imagem: " + url);
        System.out.println("Tamanho da chave: " + key.length());
        System.out.println("======================================");

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header(
                            "Ocp-Apim-Subscription-Key",
                            key
                    )
                    .header(
                            "Content-Type",
                            "application/json"
                    )
                    .POST(
                            HttpRequest.BodyPublishers.ofString(jsonBody)
                    )
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println(
                    "Azure Vision HTTP Status: "
                            + response.statusCode()
            );

            if (response.statusCode() != 200) {

                System.out.println(
                        "Resposta do Azure: "
                                + response.body()
                );

                throw new IllegalStateException(
                        "Azure Vision retornou HTTP "
                                + response.statusCode()
                                + ": "
                                + response.body()
                );
            }

            JsonNode root =
                    objectMapper.readTree(response.body());

            // ==============================
            // CAPTION
            // ==============================

            String caption =
                    "Descrição não disponível.";

            Double confidence = null;

            JsonNode captionResult =
                    root.get("captionResult");

            if (captionResult != null) {

                if (captionResult.has("text")) {
                    caption =
                            captionResult
                                    .get("text")
                                    .asText();
                }

                if (captionResult.has("confidence")) {
                    confidence =
                            captionResult
                                    .get("confidence")
                                    .asDouble();
                }
            }

            // ==============================
            // TAGS
            // ==============================

            List<String> tags =
                    new ArrayList<>();

            JsonNode tagsResult =
                    root.get("tagsResult");

            if (tagsResult != null
                    && tagsResult.has("values")) {

                for (JsonNode tag :
                        tagsResult.get("values")) {

                    if (tag.has("name")) {
                        tags.add(
                                tag.get("name").asText()
                        );
                    }
                }
            }

            return new AnalyzeResponse(
                    url,
                    caption,
                    confidence,
                    tags
            );

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "A chamada ao Azure Vision foi interrompida.",
                    e
            );

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Erro ao chamar o Azure Vision: "
                            + e.getMessage(),
                    e
            );
        }
    }
}
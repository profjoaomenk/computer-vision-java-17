package br.com.profjoao.cognitivevision.config;

import com.azure.ai.vision.imageanalysis.ImageAnalysisClient;
import com.azure.ai.vision.imageanalysis.ImageAnalysisClientBuilder;
import com.azure.core.credential.KeyCredential;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureVisionConfig {

    @Bean
    public ImageAnalysisClient imageAnalysisClient() {
        String endpoint = System.getenv("VISION_ENDPOINT");
        String key = System.getenv("VISION_KEY");

        if (endpoint == null || endpoint.isBlank() || key == null || key.isBlank()) {
            throw new IllegalStateException(
                "Configure as variáveis de ambiente VISION_ENDPOINT e VISION_KEY."
            );
        }

        return new ImageAnalysisClientBuilder()
                .endpoint(endpoint)
                .credential(new KeyCredential(key))
                .buildClient();
    }
}

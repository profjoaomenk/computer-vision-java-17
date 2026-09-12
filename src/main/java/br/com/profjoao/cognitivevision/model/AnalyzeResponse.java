package br.com.profjoao.cognitivevision.model;

import java.util.List;

public record AnalyzeResponse(
        String url,
        String caption,
        Double confidence,
        List<String> tags
) {
}

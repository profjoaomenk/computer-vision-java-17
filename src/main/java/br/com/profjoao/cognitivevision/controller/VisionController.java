package br.com.profjoao.cognitivevision.controller;

import br.com.profjoao.cognitivevision.model.AnalyzeRequest;
import br.com.profjoao.cognitivevision.model.AnalyzeResponse;
import br.com.profjoao.cognitivevision.service.VisionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class VisionController {

    private final VisionService visionService;

    public VisionController(VisionService visionService) {
        this.visionService = visionService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<AnalyzeResponse> analyze(@RequestBody AnalyzeRequest request) {
        return ResponseEntity.ok(visionService.analyze(request.url()));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}

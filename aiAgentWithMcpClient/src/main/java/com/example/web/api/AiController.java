package com.example.web.api;

import com.example.ai.aisample.AiSampleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ai")
@Slf4j
@Tag(name = "AI Controller", description = "APIs for AI")
public class AiController {

    @Autowired
    private AiSampleService aiSampleService;

    @GetMapping(value = "/generate/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<String> generateAiResponse(@RequestParam(value = "userInput", defaultValue = "Say Hello to me") String userInput) {
        log.info("[generateAiResponse] Processing request with input: {}", userInput);
        Flux<String> response = aiSampleService.generateAiResponse(userInput);
        log.info("[generateAiResponse] Generated response: {}", response);
        return response;
    }

    @GetMapping(value = "/generate/stream/mono", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<String> generateAiResponseStreamMono(@RequestParam(value = "userInput", defaultValue = "Say Hello to me") String userInput) {
        log.info("[generateAiResponseStreamMono] Processing mono stream request: {}", userInput);
        return aiSampleService.generateAiResponseStreamMono(userInput);
    }

}

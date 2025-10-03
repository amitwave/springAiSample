package com.example.web.api;

import com.example.ai.aisample.AiSampleService;
import com.example.model.ResponseModels;
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

import java.util.Map;

@RestController
@RequestMapping("/ai")
@Slf4j
@Tag(name = "AI Controller", description = "APIs for AI")
public class AiController {

    @Autowired
    private AiSampleService aiSampleService;

    @GetMapping("/generate")
    public Map<String,String> generateAiResponse(@RequestParam(value = "userInput", defaultValue = "Say Hello to me") String userInput) {
        log.info("[generateAiResponse] Processing request with input: {}", userInput);
        Map<String, String> response = aiSampleService.generateAiResponse(userInput);
        log.info("[generateAiResponse] Generated response: {}", response);
        return response;
    }

    @GetMapping("/math")
    public String generateAiResponseMath(@RequestParam(value = "userInput", defaultValue = "add 2 and 2") String userInput) {
        log.info("[generateAiResponseMath] Processing math operation: {}", userInput);
        String result = aiSampleService.generateAiResponseMath(userInput);
        log.info("[generateAiResponseMath] Calculation result: {}", result);
        return result;
    }

    @GetMapping(value = "/generate/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<String> generateAiResponseStream(@RequestParam(value = "userInput", defaultValue = "Say Hello to me") String userInput) {
        log.info("[generateAiResponseStream] Starting stream processing for input: {}", userInput);
        return aiSampleService.generateAiResponseStream(userInput);
    }

    @GetMapping(value = "/generate/stream/mono", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<String> generateAiResponseStreamMono(@RequestParam(value = "userInput", defaultValue = "Say Hello to me") String userInput) {
        log.info("[generateAiResponseStreamMono] Processing mono stream request: {}", userInput);
        return aiSampleService.generateAiResponseStreamMono(userInput);
    }

    @GetMapping(value = "/generate/structured")
    public ResponseModels generateAiResponseStructured(@RequestParam(value = "userInput", defaultValue = "name 10 countries with their capital cities") String userInput) {
        log.info("[generateAiResponseStructured] Processing structured request: {}", userInput);
        ResponseModels response = aiSampleService.getAiResultByKeyAndValue(userInput);
        log.info("[generateAiResponseStructured] Generated structured response: {}", response);
        return response;
    }
}

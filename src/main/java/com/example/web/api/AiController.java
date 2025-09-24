package com.example.web.api;

import com.example.ai.aisample.AiSampleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AiController {

    @Autowired
    private AiSampleService aiSampleService;

    @GetMapping("/generate")
    public Map<String,String> generateAiResponse(@RequestParam(value = "userInput", defaultValue = "Say Hello to me") String userInput) {
        log.info(userInput);
        return aiSampleService.generateAiResponse(userInput);
    }

    @GetMapping(value = "/generate/stream")
    public Flux<String> generateAiResponseStream(@RequestParam(value = "userInput", defaultValue = "Say Hello to me") String userInput) {
        return aiSampleService.generateAiResponseStream(userInput);
    }

    @GetMapping(value = "/generate/stream/mono")
    public Mono<String> generateAiResponseStreamMono(@RequestParam(value = "userInput", defaultValue = "Say Hello to me") String userInput) {
        return aiSampleService.generateAiResponseStreamMono(userInput);
    }
}

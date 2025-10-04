package com.example.ai.aisample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AiSampleService {

    @Autowired
    private ChatClient chatClient;

    public Flux<String> generateAiResponse(String message) {
        log.info("Generating AI response for message: {}", message);
            Prompt prompt = getPrompt(message);

            Flux<String> response = this.chatClient.prompt(prompt).user(message).stream().content();
            log.info("Generated response: {}", response);
            return response;

    }

    public Mono<String> generateAiResponseStreamMono(String message) {
        log.info("Starting AI response stream mono for message: {}", message);
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatClient.prompt(prompt)
                .stream()
                .chatClientResponse()
                .map(ChatClientResponse::chatResponse)                   // Get ChatResponse
                .filter(Objects::nonNull)                           // Filter out null ChatResponses
                .map(AiSampleService::getString)
                .filter(text -> !text.isEmpty())                    // Filter out empty strings
                .reduce("", String::concat);                         // Aggregate into single string
    }




    private static Prompt getPrompt(String input) {
        UserMessage userMessage = new UserMessage(input);

        SystemPromptTemplate systemPrompt = new SystemPromptTemplate("You are an information provider. Create Response as provided Entity with key and list of values for each key");

        return new Prompt(List.of(systemPrompt.createMessage(), userMessage));

    }


    private static String getString(ChatResponse chatResponse) {
        var generations = chatResponse.getResults();
        if (generations.isEmpty() || generations.getFirst().getOutput().getText() == null) {
            log.info("No text generated from chat response");
            return "";
        }
        String text = generations.getFirst().getOutput().getText();
        log.info("Extracted text from chat response: {}", text);
        return text;
    }


}

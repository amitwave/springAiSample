package com.example.ai.aisample;
import com.example.model.ResponseModels;
import com.example.tool.MathTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class AiSampleService {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private OpenAiChatModel chatModel;

    @Autowired
    private MathTools mathTools;

    public Map<String,String> generateAiResponse(String message) {
        log.info("Generating AI response for message: {}", message);
        String response = this.chatClient.prompt().user(message).call().content();
        log.info("Generated response: {}", response);
        return Map.of("content", Objects.requireNonNull(response));
    }

    public Flux<ChatClientResponse> generateStreamChatResponse(String message) {
        log.info("Starting stream chat response generation for message: {}", message);
        Prompt prompt = new Prompt(new UserMessage(message));
        return this.chatClient.prompt(prompt).stream().chatClientResponse()
                .doOnComplete(() -> log.info("Completed stream chat response generation"));
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


    public ResponseModels getAiResultByKeyAndValue(String input){
        log.info(input);
        UserMessage userMessage = new UserMessage(input);

        SystemPromptTemplate systemPrompt = new SystemPromptTemplate("You are an information provider. Create Response as provided Entity with key and list of values for each key");

        Prompt prompt = new Prompt(List.of(systemPrompt.createMessage(), userMessage));

        return chatClient.prompt(prompt).call().entity(ResponseModels.class);
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

    public Flux<String> generateAiResponseStream(String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .chatResponse()
                .doOnNext(chatResponse -> {
                    // Extract metadata from the ChatResponse
                    ChatResponseMetadata metadata = chatResponse.getMetadata();
                    RateLimit rateLimit = metadata.getRateLimit();
                    Usage usage = metadata.getUsage();

                    log.info(message);
                })
                // Map the Flux<ChatResponse> to Flux<String> to return only the content
                .map(AiSampleService::getString);


    }

    public String generateAiResponseMath(String userInput) {
        return this.chatClient.prompt()
                .tools(mathTools)
                .user(userInput)
                .call().content();
    }
}

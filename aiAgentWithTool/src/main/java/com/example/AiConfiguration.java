package com.example;

import com.example.tool.MathTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfiguration {

    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel chatModel, MathTools mathTools) {
        return ChatClient.builder(chatModel).defaultTools(mathTools).build();
    }

}

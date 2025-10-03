package com.example;

import com.example.tool.MathTools;
import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfiguration {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chat, ToolCallbackProvider toolCallbackProvider) {
        return chat
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }


}

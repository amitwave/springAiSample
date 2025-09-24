# AI Service API (Spring WebFlux) — README

A lightweight reactive REST API that exposes AI-powered endpoints under the /ai path. The controller provides synchronous, streaming, and structured responses backed by a service layer.

## Tech Stack

- Java 21
- Spring Boot (WebFlux)
- Reactor (Flux, Mono)
- OpenAPI annotations (Swagger)
- SLF4J logging

## Quick Start

Prerequisites:
- Java 21 installed and on PATH
- Maven or Gradle

Build and run (choose one):
- Maven:
    - Run:
```shell script
mvn spring-boot:run
```

- Or package and run:
```shell script
mvn clean package
    java -jar target/<your-app-name>-<version>.jar
```

- Gradle:
    - Run:
```shell script
./gradlew bootRun
```

- Or package and run:
```shell script
./gradlew clean bootJar
    java -jar build/libs/<your-app-name>-<version>.jar
```


By default, the app usually starts on port 8080. To change it, set:
```shell script
export SERVER_PORT=8081
# or via a config file: server.port=8081
```


## API Overview

Base path: /ai

- GET /ai/generate — Synchronous JSON response
- GET /ai/generate/stream — Reactive streaming response (Flux<String>)
- GET /ai/generate/stream/mono — Single reactive item (Mono<String>)
- GET /ai/generate/structured — Structured JSON response mapped to a model

All endpoints accept an optional userInput query parameter.

## Endpoints

1) GET /ai/generate
- Description: Returns a simple key-value JSON with the AI’s response.
- Query params:
    - userInput (optional, default: "Say Hello to me")
- Example:
```shell script
curl -G "http://localhost:8080/ai/generate" --data-urlencode "userInput=Tell me a fun fact"
```

- Sample response:
```json
{
    "message": "..."
  }
```


2) GET /ai/generate/stream
- Description: Streams chunks of the AI response reactively.
- Query params:
    - userInput (optional, default: "Say Hello to me")
- Tips for clients:
    - Use a client that supports streaming and keep-alive.
    - For Server-Sent Events (SSE), set the Accept header to text/event-stream.
- Examples:
```shell script
# Plain chunked stream
  curl -N -G "http://localhost:8080/ai/generate/stream" --data-urlencode "userInput=Stream a short poem"
```
```shell script
# Request as SSE (if configured to produce SSE)
  curl -N -H "Accept: text/event-stream" -G "http://localhost:8080/ai/generate/stream" --data-urlencode "userInput=Stream a short poem"
```


3) GET /ai/generate/stream/mono
- Description: Emits a single string reactively (Mono), useful for consistent reactive handling.
- Query params:
    - userInput (optional, default: "Say Hello to me")
- Example:
```shell script
curl -G "http://localhost:8080/ai/generate/stream/mono" --data-urlencode "userInput=One-liner please"
```

- Sample response:
```json
"..."
```


4) GET /ai/generate/structured
- Description: Returns a structured JSON model (e.g., a list of name/value pairs or richer objects).
- Query params:
    - userInput (optional, default: "name 10 countries with their capital cities")
- Example:
```shell script
curl -G "http://localhost:8080/ai/generate/structured" --data-urlencode "userInput=name 3 countries with their capitals"
```

- Sample response:
```json
{
    "items": [
      { "name": "...", "value": "..." }
    ]
  }
```


## OpenAPI / Swagger

If OpenAPI is enabled in the project, you can typically access:
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

If not present, add the OpenAPI integration dependency and it will pick up the controller annotations automatically.

## Logging

Requests log the userInput for traceability. Adjust logging levels via configuration files or environment variables as needed:
```shell script
export LOGGING_LEVEL_ROOT=INFO
export LOGGING_LEVEL_COM_EXAMPLE=DEBUG
```


## Error Handling

- Standard Spring WebFlux error responses (4xx/5xx) are returned on validation or processing errors.
- Consider adding a global exception handler to return consistent error shapes:
    - Path
    - Timestamp
    - Message
    - Optional correlation ID

## Consuming the API Programmatically

- Java WebClient example:
```java
import org.springframework.web.reactive.function.client.WebClient;
  import reactor.core.publisher.Flux;
  import reactor.core.publisher.Mono;

  public class AiClient {
      private final WebClient webClient = WebClient.create("http://localhost:8080");

      public Mono<String> generate(String userInput) {
          return webClient.get()
                  .uri(uriBuilder -> uriBuilder.path("/ai/generate")
                          .queryParam("userInput", userInput)
                          .build())
                  .retrieve()
                  .bodyToMono(String.class);
      }

      public Flux<String> stream(String userInput) {
          return webClient.get()
                  .uri(uriBuilder -> uriBuilder.path("/ai/generate/stream")
                          .queryParam("userInput", userInput)
                          .build())
                  .retrieve()
                  .bodyToFlux(String.class);
      }

      public Mono<String> streamMono(String userInput) {
          return webClient.get()
                  .uri(uriBuilder -> uriBuilder.path("/ai/generate/stream/mono")
                          .queryParam("userInput", userInput)
                          .build())
                  .retrieve()
                  .bodyToMono(String.class);
      }

      public Mono<String> structured(String userInput) {
          return webClient.get()
                  .uri(uriBuilder -> uriBuilder.path("/ai/generate/structured")
                          .queryParam("userInput", userInput)
                          .build())
                  .retrieve()
                  .bodyToMono(String.class);
      }
  }
```


## Configuration and Environment

- Common properties (application.properties or environment variables):
    - server.port
    - logging.level.root
    - Any AI-related API keys or endpoints:
        - export AI_API_KEY=<your-key>
        - export AI_BASE_URL=<your-ai-endpoint>

Use placeholders for secrets in configuration and deployment manifests.

## Notes and Best Practices

- Streaming endpoint clients should handle partial data and line-delimited chunks.
- If Server-Sent Events are desired, ensure the response is produced with text/event-stream and the client sets the appropriate Accept header.
- For structured outputs, define strong model classes and validate inputs early.
- Add request validation (e.g., max length for userInput) to prevent abuse.
- Consider rate limiting and authentication if exposed publicly.

## Health and Monitoring

- Add a health endpoint via Spring Boot Actuator for readiness/liveness:
```shell script
curl http://localhost:8080/actuator/health
```

- Expose metrics if needed and integrate with your monitoring stack.

## Contributing

- Use Java 21
- Follow conventional commit messages where possible
- Write tests for new features and fixes
- Run the full build locally before PRs

Need help or have questions? I’m happy to assist.
package io.github.icompras.pedidos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class SpringConfig {
 
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Adicionando plugins de serealização para Java 8 e Java Time
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        // Configurando para ignorar valores nulos na serialização
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // Configurando para não falhar em propriedades desconhecidas na desserialização
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNEXPECTED_VIEW_PROPERTIES, false);

        return objectMapper;
    }
}

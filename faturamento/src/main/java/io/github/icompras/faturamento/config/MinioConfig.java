package io.github.icompras.faturamento.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.icompras.faturamento.config.props.MinioProps;
import io.minio.MinioClient;

@Configuration
public class MinioConfig {

    @Autowired
    MinioProps props;

    @Bean
    public MinioClient bucketClient() {
        return MinioClient.builder()
                .endpoint(props.getUrl())
                .credentials(props.getAccessKey(), props.getSecretKey())
                .build();

    }

}

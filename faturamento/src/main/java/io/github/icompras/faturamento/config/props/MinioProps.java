package io.github.icompras.faturamento.config.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration // Server para ser um componente gerenciado pelo Spring
@EnableConfigurationProperties // Aplicando a possibilidade de criar uma classe para representar a propriedades do application.yaml
@ConfigurationProperties(prefix = "miniobucket") // Dofinir propriedade de configurações
@Data // Necessário para acessar e poder setar os dados
public class MinioProps {
    private String url;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}

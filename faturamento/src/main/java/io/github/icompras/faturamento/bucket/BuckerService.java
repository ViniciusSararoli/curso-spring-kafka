package io.github.icompras.faturamento.bucket;

import org.springframework.stereotype.Component;

import io.minio.MinioClient;
import io.github.icompras.faturamento.config.props.MinioProps;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BuckerService {
    private final MinioClient minioClient;
    private final MinioProps MinioProps;

    // Fazer o uploado do arquivo
    public void uplod(BucketFile file) {

    }
    // Obter a URL para acesar o arquivo
    public void getUrl(String fileName) {

    }
}

package io.github.icompras.faturamento.bucket;

import java.io.InputStream;

import com.google.common.net.MediaType;

public record BucketFile(
        String name, InputStream is, MediaType type, long size) {
    
}

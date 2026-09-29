package com.elearning.media.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class LocalMediaStorage implements MediaStorage {

    private final Path storageDir;

    public LocalMediaStorage(@Value("${app.media.storage-dir}") String storageDir) {
        this.storageDir = Paths.get(storageDir).toAbsolutePath().normalize();
    }

    @Override
    public Resource load(String path) {
        Path filePath = storageDir.resolve(path).normalize();
        if (!filePath.startsWith(storageDir)) {
            throw new SecurityException("Invalid path: " + path);
        }
        if (Files.exists(filePath)) {
            return new FileSystemResource(filePath);
        }
        // Fallback to classpath for seed media (path already includes media/ prefix)
        return new org.springframework.core.io.ClassPathResource(path);
    }

    @Override
    public String store(String path, byte[] content) {
        try {
            Path filePath = storageDir.resolve(path).normalize();
            if (!filePath.startsWith(storageDir)) {
                throw new SecurityException("Invalid path: " + path);
            }
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, content);
            return path;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store media file: " + path, e);
        }
    }

    @Override
    public boolean exists(String path) {
        return Files.exists(storageDir.resolve(path).normalize());
    }
}

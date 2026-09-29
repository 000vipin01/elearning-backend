package com.elearning.media.storage;

import org.springframework.core.io.Resource;

public interface MediaStorage {
    Resource load(String path);
    String store(String path, byte[] content);
    boolean exists(String path);
}

package com.rkcreadev.demo.akka.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public final class FileUtils {

    private FileUtils() {
    }

    public static String getJsonFileName(Long clientId) {
        return clientId + ".json";
    }

    public static Long getClientIdFromFileName(String fileName) {
        return Long.valueOf(fileName.split(".json")[0]);
    }

    public static Long moveFile(String src, Path destDir, Long clientId) {
        try {
            Path inputFile = Paths.get(src, FileUtils.getJsonFileName(clientId));

            if (!Files.exists(inputFile)) {
                return null;
            }

            Files.move(inputFile, destDir.resolve(FileUtils.getJsonFileName(clientId)),
                    StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            return clientId;
        } catch (IOException e) {
            return null;
        }
    }
}

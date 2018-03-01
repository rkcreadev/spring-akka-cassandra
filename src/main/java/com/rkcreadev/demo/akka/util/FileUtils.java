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

    public static Long moveFile(String inboxDir, Path destDir, Long clientId) {
        try {
            Files.move(Paths.get(inboxDir, FileUtils.getJsonFileName(clientId)),
                    destDir.resolve(FileUtils.getJsonFileName(clientId)),
                    StandardCopyOption.REPLACE_EXISTING);
            return clientId;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

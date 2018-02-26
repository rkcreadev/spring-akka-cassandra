package com.rkcreadev.demo.akka.util;

public final class FileUtils {

    private FileUtils() {
    }

    public static String getJsonFileName(Long clientId) {
        return clientId + ".json";
    }

    public static Long getClientIdFromFileName(String fileName) {
        return Long.valueOf(fileName.split(".json")[0]);
    }
}

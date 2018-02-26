package com.rkcreadev.demo.akka.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkcreadev.demo.akka.model.json.ClientSubscribersPayments;
import com.rkcreadev.demo.akka.service.InboxLoaderService;
import com.rkcreadev.demo.akka.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class InboxLoaderServiceImpl implements InboxLoaderService {

    private ObjectMapper objectMapper;

    @Value("${inbox.dir}")
    private String inboxDir;

    @Autowired
    public InboxLoaderServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ClientSubscribersPayments load(Long clientId) {
        Path filePath = Paths.get(inboxDir, FileUtils.getJsonFileName(clientId));
        return getFromFile(filePath);
    }

    private ClientSubscribersPayments getFromFile(Path path) {
        try {
            return objectMapper.readValue(Files.readAllBytes(path), ClientSubscribersPayments.class);
        } catch (IOException e) {
            throw new RuntimeException("Error while parsing JSON from file", e);
        }
    }
}

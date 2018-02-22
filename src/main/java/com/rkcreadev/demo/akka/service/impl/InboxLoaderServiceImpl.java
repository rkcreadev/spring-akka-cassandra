package com.rkcreadev.demo.akka.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkcreadev.demo.akka.model.json.ClientSubscribersPayments;
import com.rkcreadev.demo.akka.service.InboxLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public List<ClientSubscribersPayments> load() {
        try (Stream<Path> pathStream = Files.list(Paths.get(inboxDir))) {
            return pathStream
                    .map(this::getFromFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error while loading from inbox directory", e);
        }
    }

    private ClientSubscribersPayments getFromFile(Path path) {
        try {
            return objectMapper.readValue(Files.readAllBytes(path), ClientSubscribersPayments.class);
        } catch (IOException e) {
            throw new RuntimeException("Error while parsing JSON from file", e);
        }
    }
}

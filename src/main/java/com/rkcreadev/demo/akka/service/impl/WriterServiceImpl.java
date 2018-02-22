package com.rkcreadev.demo.akka.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkcreadev.demo.akka.model.db.ClientInfo;
import com.rkcreadev.demo.akka.model.json.ClientInfoDto;
import com.rkcreadev.demo.akka.repository.ClientInfoRepository;
import com.rkcreadev.demo.akka.service.WriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class WriterServiceImpl implements WriterService {

    private ObjectMapper objectMapper;
    private ClientInfoRepository clientInfoRepository;

    @Value("${outbox.dir}")
    private String outboxDir;

    @Autowired
    public WriterServiceImpl(ObjectMapper objectMapper, ClientInfoRepository clientInfoRepository) {
        this.objectMapper = objectMapper;
        this.clientInfoRepository = clientInfoRepository;
    }

    @Override
    public ClientInfo saveToDb(ClientInfo clientInfo) {
        return clientInfoRepository.save(clientInfo);
    }

    @Override
    public void saveToFs(ClientInfoDto clientInfoDto) {
        try {
            String json = objectMapper.writeValueAsString(clientInfoDto);
            Files.write(Paths.get(outboxDir, getFileName(clientInfoDto)), json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error while saving to file system", e);
        }
    }

    private String getFileName(ClientInfoDto clientInfoDto) {
        return clientInfoDto.getClientId() + ".json";
    }
}

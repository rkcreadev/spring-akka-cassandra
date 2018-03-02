package com.rkcreadev.demo.akka.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkcreadev.demo.akka.model.ActiveDbConfiguration;
import com.rkcreadev.demo.akka.model.db.common.ClientInfo;
import com.rkcreadev.demo.akka.model.json.ClientInfoDto;
import com.rkcreadev.demo.akka.service.ClientInfoService;
import com.rkcreadev.demo.akka.service.DbVisitor;
import com.rkcreadev.demo.akka.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ClientInfoServiceImpl implements ClientInfoService {

    private DbVisitor dbVisitor;
    private ObjectMapper objectMapper;

    @Value("${outbox.dir}")
    private String outboxDir;

    @Value("${db.active}")
    private ActiveDbConfiguration activeDbConfiguration;

    @Autowired
    public ClientInfoServiceImpl(DbVisitor dbVisitor, ObjectMapper objectMapper) {
        this.dbVisitor = dbVisitor;
        this.objectMapper = objectMapper;
    }

    @Override
    public void saveToDb(ClientInfo clientInfo) {
        activeDbConfiguration.save(dbVisitor, clientInfo);
    }

    @Override
    public boolean existsById(Long clientId) {
        return activeDbConfiguration.existsById(dbVisitor, clientId);
    }

    @Override
    public void saveToFs(ClientInfoDto clientInfoDto) {
        try {
            String json = objectMapper.writeValueAsString(clientInfoDto);
            Files.write(Paths.get(outboxDir, FileUtils.getJsonFileName(clientInfoDto.getClientId())), json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error while saving to file system", e);
        }
    }

    @Override
    public void truncateAll() {
        activeDbConfiguration.truncateAll(dbVisitor);
    }
}

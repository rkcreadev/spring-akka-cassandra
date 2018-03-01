package com.rkcreadev.demo.akka.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkcreadev.demo.akka.model.db.jpa.ClientInfoJpa;
import com.rkcreadev.demo.akka.model.json.ClientSubscribersPayments;
import com.rkcreadev.demo.akka.repository.jpa.JpaClientInfoRepository;
import com.rkcreadev.demo.akka.service.InboxLoaderService;
import com.rkcreadev.demo.akka.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
public class InboxLoaderServiceImpl implements InboxLoaderService {

    private ObjectMapper objectMapper;
    private JpaClientInfoRepository clientInfoRepository;
    private Path tmpDir;

    @Autowired
    public InboxLoaderServiceImpl(ObjectMapper objectMapper, JpaClientInfoRepository clientInfoRepository,
                                  @Qualifier("tmpDir") Path tmpDir) {
        this.objectMapper = objectMapper;
        this.clientInfoRepository = clientInfoRepository;
        this.tmpDir = tmpDir;
    }

    @Override
    public ClientSubscribersPayments load(Long clientId) {
        Path filePath = tmpDir.resolve(FileUtils.getJsonFileName(clientId));

        try {
            // Need to prevent double processing
            saveEmptyEntityToDb(clientId);
            return objectMapper.readValue(Files.readAllBytes(filePath),
                    ClientSubscribersPayments.class);
        } catch (IOException e) {
            log.error(String.format("Error while reading file %d", clientId), e);
            return null;
        }
    }

    private void saveEmptyEntityToDb(Long clientId) {
        ClientInfoJpa clientInfo = new ClientInfoJpa();
        clientInfo.setClientId(clientId);

        clientInfoRepository.save(clientInfo);
    }
}

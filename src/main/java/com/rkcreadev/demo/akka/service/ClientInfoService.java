package com.rkcreadev.demo.akka.service;

import com.rkcreadev.demo.akka.model.db.common.ClientInfo;
import com.rkcreadev.demo.akka.model.json.ClientInfoDto;

public interface ClientInfoService {
    void saveToDb(ClientInfo clientInfo);
    boolean existsById(Long clientId);
    void saveToFs(ClientInfoDto clientInfoDto);
}

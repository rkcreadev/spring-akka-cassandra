package com.rkcreadev.demo.akka.service;


import com.rkcreadev.demo.akka.model.db.ClientInfo;
import com.rkcreadev.demo.akka.model.json.ClientInfoDto;

public interface WriterService {

    ClientInfo saveToDb(ClientInfo clientInfo);

    void saveToFs(ClientInfoDto clientInfoDto);
}

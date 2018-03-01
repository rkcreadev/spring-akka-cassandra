package com.rkcreadev.demo.akka.actor;

import akka.actor.AbstractActor;
import com.rkcreadev.demo.akka.mapping.ClientInfoMapper;
import com.rkcreadev.demo.akka.model.db.common.ClientInfo;
import com.rkcreadev.demo.akka.service.ClientInfoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WriterActor extends AbstractActor {

    private ClientInfoService clientInfoService;
    private ClientInfoMapper clientInfoMapper;


    @Autowired
    public WriterActor(ClientInfoService clientInfoService, ClientInfoMapper clientInfoMapper) {
        this.clientInfoService = clientInfoService;
        this.clientInfoMapper = clientInfoMapper;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(IncomingData.class, incomingData -> {
                    clientInfoService.saveToDb(incomingData.clientInfo);
                    clientInfoService.saveToFs(clientInfoMapper.mapToDto(incomingData.clientInfo));
                })
                .build();
    }

    @AllArgsConstructor
    static class IncomingData {
        private ClientInfo clientInfo;
    }
}

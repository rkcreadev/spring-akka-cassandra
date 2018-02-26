package com.rkcreadev.demo.akka.actor;

import akka.actor.AbstractActor;
import com.rkcreadev.demo.akka.mapping.ClientInfoMapper;
import com.rkcreadev.demo.akka.model.db.ClientInfo;
import com.rkcreadev.demo.akka.service.WriterService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WriterActor extends AbstractActor {

    private WriterService writerService;
    private ClientInfoMapper clientInfoMapper;

    @Autowired
    public WriterActor(WriterService writerService, ClientInfoMapper clientInfoMapper) {
        this.writerService = writerService;
        this.clientInfoMapper = clientInfoMapper;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(IncomingData.class, incomingData -> {
                    ClientInfo clientInfo = writerService.saveToDb(incomingData.clientInfo);
                    writerService.saveToFs(clientInfoMapper.mapToDto(clientInfo));
                })
                .build();
    }

    @AllArgsConstructor
    static class IncomingData {
        private ClientInfo clientInfo;
    }
}

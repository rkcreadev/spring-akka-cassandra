package com.rkcreadev.demo.akka.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.rkcreadev.demo.akka.model.db.common.ClientInfo;
import com.rkcreadev.demo.akka.model.json.ClientSubscribersPayments;
import com.rkcreadev.demo.akka.model.json.SubscriberPayment;
import com.rkcreadev.demo.akka.service.ActorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProcessorActor extends AbstractActor {

    private ActorService actorService;

    @Autowired
    public ProcessorActor(ActorService actorService) {
        this.actorService = actorService;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(IncomingData.class, incomingData -> process(incomingData.payments))
                .build();
    }

    private void process(ClientSubscribersPayments payments) {
        long spentTotal = payments.getSubscribers()
                .stream()
                .map(SubscriberPayment::getSpent)
                .reduce((a, b) -> a + b)
                .orElse(0L);

        sendToWriteActor(payments.getClientId(), spentTotal, payments.getSubscribers());
    }

    private void sendToWriteActor(Long clientId, long spentTotal, List<SubscriberPayment> subscriberPayments) {
        actorService.get(WriterActor.class)
                .tell(new WriterActor.IncomingData(new ClientInfo(clientId, spentTotal, subscriberPayments)),
                        ActorRef.noSender());
    }

    @AllArgsConstructor
    public static class IncomingData {
        private ClientSubscribersPayments payments;
    }
}

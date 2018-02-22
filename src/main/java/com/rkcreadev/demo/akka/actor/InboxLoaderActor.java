package com.rkcreadev.demo.akka.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.rkcreadev.demo.akka.service.ActorService;
import com.rkcreadev.demo.akka.service.InboxLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InboxLoaderActor extends AbstractActor {

    private InboxLoaderService inboxLoaderService;
    private ActorService actorService;

    @Autowired
    public InboxLoaderActor(InboxLoaderService inboxLoaderService, ActorService actorService) {
        this.inboxLoaderService = inboxLoaderService;
        this.actorService = actorService;
    }

    public Receive createReceive() {
        return receiveBuilder()
                .matchAny(obj -> {
                    ActorRef processorActor = actorService.get(ProcessorActor.class);
                    inboxLoaderService.load()
                            .forEach(clientSubscribersPayments -> processorActor.tell(
                                    new ProcessorActor.IncomingData(clientSubscribersPayments), ActorRef.noSender()));
                })
                .build();
    }
}

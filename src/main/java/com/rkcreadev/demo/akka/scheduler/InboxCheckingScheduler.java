package com.rkcreadev.demo.akka.scheduler;

import akka.actor.ActorRef;
import com.rkcreadev.demo.akka.actor.ProcessorActor;
import com.rkcreadev.demo.akka.model.db.ClientInfo;
import com.rkcreadev.demo.akka.model.json.ClientSubscribersPayments;
import com.rkcreadev.demo.akka.repository.ClientInfoRepository;
import com.rkcreadev.demo.akka.service.ActorService;
import com.rkcreadev.demo.akka.service.InboxLoaderService;
import com.rkcreadev.demo.akka.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class InboxCheckingScheduler {

    private ClientInfoRepository clientInfoRepository;
    private ActorService actorService;
    private InboxLoaderService inboxLoaderService;

    @Value("${inbox.dir}")
    private String inboxPath;

    @Autowired
    public InboxCheckingScheduler(ClientInfoRepository clientInfoRepository, ActorService actorService,
                                  InboxLoaderService inboxLoaderService) {
        this.clientInfoRepository = clientInfoRepository;
        this.actorService = actorService;
        this.inboxLoaderService = inboxLoaderService;
    }

    @Scheduled(fixedRate = 10 * 1000)
    public void checkInbox() throws IOException {
        try (Stream<Path> files = Files.list(Paths.get(inboxPath))) {
            ActorRef processorActors = actorService.getWithRoundRobin(ProcessorActor.class, 1);

            getNewClientSubscriberPayments(files)
                    .forEach(clientSubscribersPayments -> sendToActor(processorActors, clientSubscribersPayments));
        }
    }

    private Stream<ClientSubscribersPayments> getNewClientSubscriberPayments(Stream<Path> files) {

        Set<Long> processedClientIds = clientInfoRepository.findAll()
                .stream()
                .map(ClientInfo::getClientId)
                .collect(Collectors.toSet());

        return files.map(path -> FileUtils.getClientIdFromFileName(path.getFileName().toString()))
                .filter(clientId -> !processedClientIds.contains(clientId))
                .map(clientId -> inboxLoaderService.load(clientId));
    }

    private void sendToActor(ActorRef actorRef, ClientSubscribersPayments clientSubscribersPayments) {
        actorRef.tell(new ProcessorActor.IncomingData(clientSubscribersPayments),
                ActorRef.noSender());
    }
}

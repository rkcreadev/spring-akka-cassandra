package com.rkcreadev.demo.akka.scheduler;

import akka.actor.ActorRef;
import com.rkcreadev.demo.akka.actor.ProcessorActor;
import com.rkcreadev.demo.akka.model.json.ClientSubscribersPayments;
import com.rkcreadev.demo.akka.service.ActorService;
import com.rkcreadev.demo.akka.service.ClientInfoService;
import com.rkcreadev.demo.akka.service.InboxLoaderService;
import com.rkcreadev.demo.akka.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

@Component
@Slf4j
public class InboxCheckingScheduler {

    private ClientInfoService clientInfoService;
    private ActorService actorService;
    private InboxLoaderService inboxLoaderService;
    private Path tmpDir;

    @Value("${inbox.dir}")
    private String inboxPath;
    @Value("${scheduling.enable}")
    private boolean enableScheduling;
    @Value("${akka.actor.processor.count}")
    private int countProcessorActors;

    @Autowired
    public InboxCheckingScheduler(ActorService actorService, InboxLoaderService inboxLoaderService,
                                  @Qualifier("tmpDir") Path tmpDir, ClientInfoService clientInfoService) {
        this.actorService = actorService;
        this.inboxLoaderService = inboxLoaderService;
        this.tmpDir = tmpDir;
        this.clientInfoService = clientInfoService;
    }

    @Scheduled(fixedRate = 30 * 1000)
    public void checkInbox() throws IOException {
        if (!enableScheduling) {
            return;
        }

        long before = System.currentTimeMillis();
        final long[] newFilesCount = {0};

        try (Stream<Path> files = Files.list(Paths.get(inboxPath))) {
            ActorRef processorActors = actorService.getWithRoundRobin(ProcessorActor.class, countProcessorActors);

            getNewClientSubscriberPayments(files)
                    .peek(csp -> newFilesCount[0]++)
                    .forEach(clientSubscribersPayments -> sendToActor(processorActors, clientSubscribersPayments));
        }
        long after = System.currentTimeMillis();

        if (newFilesCount[0] == 0) {
            return;
        }

        log.warn("------------------------------------");
        log.warn("Method execution time: " + (after - before) + " milliseconds");
        log.warn("------------------------------------");
    }

    private Stream<ClientSubscribersPayments> getNewClientSubscriberPayments(Stream<Path> files) {
        return files
                .map(path -> FileUtils.getClientIdFromFileName(path.getFileName().toString()))
                .filter(id -> !clientInfoService.existsById(id))
                .map(clientId -> FileUtils.moveFile(inboxPath, tmpDir, clientId))
                .filter(Objects::nonNull)
                .map(clientId -> inboxLoaderService.load(clientId));
    }

    private void sendToActor(ActorRef actorRef, ClientSubscribersPayments clientSubscribersPayments) {
        actorRef.tell(new ProcessorActor.IncomingData(clientSubscribersPayments),
                ActorRef.noSender());
    }
}

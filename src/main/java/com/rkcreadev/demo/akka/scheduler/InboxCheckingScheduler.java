package com.rkcreadev.demo.akka.scheduler;

import akka.actor.ActorRef;
import com.rkcreadev.demo.akka.actor.ProcessorActor;
import com.rkcreadev.demo.akka.model.db.common.Benchmark;
import com.rkcreadev.demo.akka.model.json.ClientSubscribersPayments;
import com.rkcreadev.demo.akka.service.ActorService;
import com.rkcreadev.demo.akka.service.BenchmarkService;
import com.rkcreadev.demo.akka.service.ClientInfoService;
import com.rkcreadev.demo.akka.service.InboxLoaderService;
import com.rkcreadev.demo.akka.util.FileUtils;
import com.rkcreadev.demo.akka.util.InboxLoaderFilesGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class InboxCheckingScheduler {

    private ClientInfoService clientInfoService;
    private ActorService actorService;
    private InboxLoaderService inboxLoaderService;
    private BenchmarkService benchmarkService;
    private InboxLoaderFilesGenerator inboxLoaderFilesGenerator;
    private Path tmpDir;

    @Value("${inbox.dir}")
    private String inboxPath;
    @Value("${scheduling.enable}")
    private boolean enableScheduling;
    @Value("${akka.actor.processor.count}")
    private int countProcessorActors;

    @Autowired
    public InboxCheckingScheduler(ActorService actorService, InboxLoaderService inboxLoaderService,
                                  @Qualifier("tmpDir") Path tmpDir, ClientInfoService clientInfoService,
                                  BenchmarkService benchmarkService,
                                  InboxLoaderFilesGenerator inboxLoaderFilesGenerator) {
        this.actorService = actorService;
        this.inboxLoaderService = inboxLoaderService;
        this.tmpDir = tmpDir;
        this.clientInfoService = clientInfoService;
        this.benchmarkService = benchmarkService;
        this.inboxLoaderFilesGenerator = inboxLoaderFilesGenerator;
    }

    @Scheduled(cron = "0/30 * * * * *")
    public void checkInbox() throws IOException {
        if (!enableScheduling) {
            return;
        }

        Date startDate = new Date();
        long before = System.currentTimeMillis();
        final long[] newFilesCount = {0};

        ActorRef processorActors = actorService.getWithRoundRobin(ProcessorActor.class, countProcessorActors);
        try (Stream<Path> fileStream = Files.list(Paths.get(inboxPath))) {

            getNewClientSubscriberPayments(fileStream)
                    .peek(csp -> newFilesCount[0]++)
                    .forEach(clientSubscribersPayments -> sendToActor(processorActors, clientSubscribersPayments));
        }

        long after = System.currentTimeMillis();

        if (newFilesCount[0] == 0) {
            return;
        }

        saveBenchmark(after - before, newFilesCount[0], startDate);

//        copyFromInboxReserved();

        inboxLoaderFilesGenerator.run();

        clientInfoService.truncateAll();
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

    private void saveBenchmark(long milliseconds, long filesCount, Date startDate) {
        Benchmark benchmark = new Benchmark();
        benchmark.setRunId(getRunId(startDate));
        benchmark.setFilesCount(filesCount);
        benchmark.setMilliseconds(milliseconds);
        benchmark.setStartDate(startDate);
        benchmarkService.save(benchmark);

        System.out.println("------------------------------------");
        System.out.println("Method execution time: " + milliseconds + " milliseconds");
        System.out.println("FILES PROCESSED: " + filesCount);
        System.out.println("------------------------------------");
    }

    private void copyFromInboxReserved() throws IOException {
        Files.list(Paths.get("/tmp/akka/inbox-copy"))
                .forEach(path -> {
                    try {
                        Files.copy(path, Paths.get(inboxPath).resolve(path.getFileName()),
                                StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                    }
                });
    }

    private String getRunId(Date startDate) {
        return DigestUtils.md5DigestAsHex(startDate.toString().getBytes());
    }
}

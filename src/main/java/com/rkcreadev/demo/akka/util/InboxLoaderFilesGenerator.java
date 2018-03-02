package com.rkcreadev.demo.akka.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkcreadev.demo.akka.model.json.ClientSubscribersPayments;
import com.rkcreadev.demo.akka.model.json.SubscriberPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Component
public class InboxLoaderFilesGenerator {

    private ObjectMapper objectMapper;

    @Value("${inbox.dir}")
    private String inboxDir;
    @Value("${outbox.dir}")
    private String outboxDir;
    @Value("${generator.files.count}")
    private Long filesCount;
    @Value("${generator.subscribers.count}")
    private Long subscribersCount;

    @Autowired
    public InboxLoaderFilesGenerator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void run() {
        try {
            prepare();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while preparing  to generate");
        }

        List<ClientSubscribersPayments> clients = new ArrayList<>();

        LongStream.range(1, filesCount)
                .limit(filesCount)
                .forEach(id -> {
                    ClientSubscribersPayments csp = new ClientSubscribersPayments();
                    csp.setClientId(id);
                    csp.setSubscribers(subscriberPayments());
                    clients.add(csp);
                });

        clients.forEach(client -> {
            try {
                Files.write(Paths.get(inboxDir, FileUtils.getJsonFileName(client.getClientId())),
                        objectMapper.writeValueAsString(client).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void prepare() throws IOException {
        cleanPath(inboxDir);
        cleanPath(outboxDir);
    }

    private void cleanPath(String dir) throws IOException {
        try (Stream<Path> files = Files.list(Paths.get(dir))) {
                files.forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    throw new RuntimeException("Clear directory exception!", e);
                }
            });
        }

    }

    private List<SubscriberPayment> subscriberPayments() {
        List<SubscriberPayment> subscriberPaymentList = new ArrayList<>();
        Random random = new Random();

        random.longs(0, 200)
                .limit(subscribersCount)
                .forEach(id -> {
                    SubscriberPayment sb = new SubscriberPayment();
                    sb.setId(id);
                    sb.setSpent(id * 100);
                    subscriberPaymentList.add(sb);
                });

        return subscriberPaymentList;
    }
}
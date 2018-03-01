package com.rkcreadev.demo.akka.actor;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkcreadev.demo.akka.model.json.ClientSubscribersPayments;
import com.rkcreadev.demo.akka.model.json.SubscriberPayment;
import com.rkcreadev.demo.akka.util.FileUtils;
import org.junit.Test;
import org.mockito.internal.util.io.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class InboxLoaderFilesGenerator {

    private static ObjectMapper objectMapper;
    private static String inboxDir;
    private static Long filesCount;
    private static Long subscribersCount;

    private static void prepare() throws IOException {
        objectMapper = new ObjectMapper();
        InputStream propsFile = InboxLoaderFilesGenerator.class.getClassLoader()
                .getResourceAsStream("application.properties");
        Properties properties = new Properties();
        properties.load(propsFile);

        inboxDir = properties.getProperty("inbox.dir");
        filesCount = Long.valueOf(properties.getProperty("generator.files.count"));
        subscribersCount = Long.valueOf(properties.getProperty("generator.subscribers.count"));

        Files.list(Paths.get(inboxDir))
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new RuntimeException("Clear directory exception!", e);
                    }
                });
    }

    public static void main(String... args) {
        try {
            prepare();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while preparing  to generate");
        }

        List<ClientSubscribersPayments> clients = new ArrayList<>();

        Random random = new Random();

        random.longs(1000, 2000)
                .limit(filesCount)
                .forEach(id -> {
                    ClientSubscribersPayments csp = new ClientSubscribersPayments();
                    csp.setClientId(id);
                    csp.setSubscribers(subscriberPayments());
                    clients.add(csp);
                });

        clients.forEach(client -> {
            try {
                IOUtil.writeText(objectMapper.writeValueAsString(client),
                        Paths.get(inboxDir, FileUtils.getJsonFileName(client.getClientId())).toFile());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    private static List<SubscriberPayment> subscriberPayments() {
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
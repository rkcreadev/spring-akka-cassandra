package com.rkcreadev.demo.akka.actor;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkcreadev.demo.akka.model.json.ClientSubscribersPayments;
import com.rkcreadev.demo.akka.model.json.SubscriberPayment;
import com.rkcreadev.demo.akka.util.FileUtils;
import org.mockito.internal.util.io.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.LongStream;

public class InboxLoaderFilesGenerator {

    private static ObjectMapper objectMapper;
    private static String inboxDir;
    private static String outboxDir;
    private static Long filesCount;
    private static Long subscribersCount;

    public static void main(String... args) {
        try {
            prepare();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while preparing  to generate");
        }

        LongStream.range(1, filesCount)
                .limit(filesCount)
                .forEach(id -> {
                    ClientSubscribersPayments csp = new ClientSubscribersPayments();
                    csp.setClientId(id);
                    csp.setSubscribers(subscriberPayments());
                    writeToFile(csp);
                });
    }

    private static void writeToFile(ClientSubscribersPayments csp) {
        try {
            IOUtil.writeText(objectMapper.writeValueAsString(csp),
                    Paths.get(inboxDir, FileUtils.getJsonFileName(csp.getClientId())).toFile());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static void prepare() throws IOException {
        objectMapper = new ObjectMapper();
        InputStream propsFile = InboxLoaderFilesGenerator.class.getClassLoader()
                .getResourceAsStream("application.properties");
        Properties properties = new Properties();
        properties.load(propsFile);

        inboxDir = properties.getProperty("inbox.dir");
        outboxDir = properties.getProperty("outbox.dir");
        filesCount = Long.valueOf(properties.getProperty("generator.files.count"));
        subscribersCount = Long.valueOf(properties.getProperty("generator.subscribers.count"));

        cleanPath(inboxDir);
        cleanPath(outboxDir);
    }

    private static void cleanPath(String dir) throws IOException {
        Files.list(Paths.get(dir))
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new RuntimeException("Clear directory exception!", e);
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
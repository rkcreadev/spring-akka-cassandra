package com.rkcreadev.demo.akka.actor;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkcreadev.demo.akka.AkkaApplication;
import com.rkcreadev.demo.akka.model.json.ClientSubscribersPayments;
import com.rkcreadev.demo.akka.model.json.SubscriberPayment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.io.IOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AkkaApplication.class)
public class InboxLoaderActorTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void clear() throws IOException {
        Files.list(Paths.get("/tmp/akka/inbox/"))
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new RuntimeException("Clear directory exception!", e);
                    }
                });
    }

    @Test
    public void generate() {
        List<ClientSubscribersPayments> clients = new ArrayList<>();

        Random random = new Random();

        random.longs(1000, 2000)
                .limit(50)
                .forEach(id -> {
                    ClientSubscribersPayments csp = new ClientSubscribersPayments();
                    csp.setClientId(123L);
                    csp.setSubscribers(subscriberPayments());
                    clients.add(csp);
                });

        clients.forEach(client -> {
            try {
                IOUtil.writeText(objectMapper.writeValueAsString(client),
                        new File("/tmp/akka/inbox/" + client.getClientId() + ".json"));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    private List<SubscriberPayment> subscriberPayments() {
        List<SubscriberPayment> subscriberPaymentList = new ArrayList<>();
        Random random = new Random();

        random.longs(0, 200)
                .limit(200)
                .forEach(id -> {
                    SubscriberPayment sb = new SubscriberPayment();
                    sb.setId(id);
                    sb.setSpent(id * 100);
                    subscriberPaymentList.add(sb);
                });

        return subscriberPaymentList;
    }
}
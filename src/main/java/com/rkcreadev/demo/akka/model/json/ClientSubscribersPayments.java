package com.rkcreadev.demo.akka.model.json;

import lombok.Data;

import java.util.List;

@Data
public class ClientSubscribersPayments {
    private Long clientId;
    private List<SubscriberPayment> subscribers;
}

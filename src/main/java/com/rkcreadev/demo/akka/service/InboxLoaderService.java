package com.rkcreadev.demo.akka.service;


import com.rkcreadev.demo.akka.model.json.ClientSubscribersPayments;

public interface InboxLoaderService {

    ClientSubscribersPayments load(Long clientIds);
}

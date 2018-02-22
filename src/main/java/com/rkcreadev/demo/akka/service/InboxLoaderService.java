package com.rkcreadev.demo.akka.service;


import com.rkcreadev.demo.akka.model.json.ClientSubscribersPayments;

import java.util.List;

public interface InboxLoaderService {

    List<ClientSubscribersPayments> load();
}

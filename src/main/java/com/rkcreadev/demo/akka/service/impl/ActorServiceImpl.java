package com.rkcreadev.demo.akka.service.impl;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.rkcreadev.demo.akka.actor.extension.SpringExtension;
import com.rkcreadev.demo.akka.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActorServiceImpl implements ActorService {

    private ActorSystem actorSystem;
    private SpringExtension springExtension;

    @Autowired
    public ActorServiceImpl(ActorSystem actorSystem, SpringExtension springExtension) {
        this.actorSystem = actorSystem;
        this.springExtension = springExtension;
    }

    @Override
    public ActorRef get(Class clazz) {
        return actorSystem.actorOf(springExtension.props(clazz));
    }
}

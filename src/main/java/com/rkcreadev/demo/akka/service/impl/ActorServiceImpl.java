package com.rkcreadev.demo.akka.service.impl;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;
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

    @Override
    public ActorRef getWithRoundRobin(Class clazz, int countActors) {
        return actorSystem.actorOf(springExtension.props(clazz).withRouter(new RoundRobinPool(countActors)));
    }
}

package com.rkcreadev.demo.akka.service;

import akka.actor.ActorRef;

public interface ActorService {

    ActorRef get(Class clazz);

    ActorRef getWithRoundRobin(Class clazz, int countActors);
}

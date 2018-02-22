package com.rkcreadev.demo.akka.config;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import org.springframework.context.ApplicationContext;


public class SpringActorProducer implements IndirectActorProducer {

    private ApplicationContext ctx;
    private Class actorBeanClass;

    public SpringActorProducer(ApplicationContext ctx, Class actorBeanClass) {
        this.ctx = ctx;
        this.actorBeanClass = actorBeanClass;
    }

    @Override
    public Actor produce() {
        return (Actor) ctx.getBean(actorBeanClass);
    }

    @Override
    public Class<? extends Actor> actorClass() {
        return (Class<? extends Actor>) actorBeanClass;
    }
}

package com.rkcreadev.demo.akka.actor.extension;

import akka.actor.Extension;
import akka.actor.Props;
import com.rkcreadev.demo.akka.config.SpringActorProducer;
import org.springframework.context.ApplicationContext;

public class SpringExtension implements Extension {

    private ApplicationContext applicationContext;

    public void initialize(ApplicationContext ctx) {
        this.applicationContext = ctx;
    }

    public Props props(Class actorBeanClass) {
        return Props.create(SpringActorProducer.class, applicationContext, actorBeanClass);
    }
}

package com.rkcreadev.demo.akka;


import akka.actor.ActorRef;
import com.rkcreadev.demo.akka.actor.InboxLoaderActor;
import com.rkcreadev.demo.akka.service.ActorService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/application.properties")
@ComponentScan
public class AkkaApplication {

    public static void main(String[] args) {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AkkaApplication.class);

        // TODO: Вынести в scheduler
        ActorService actorService = context.getBean(ActorService.class);
        ActorRef inboxLoaderActor = actorService.get(InboxLoaderActor.class);
        inboxLoaderActor.tell(new Object(), ActorRef.noSender());
    }
}

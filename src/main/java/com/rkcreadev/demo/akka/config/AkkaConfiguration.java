package com.rkcreadev.demo.akka.config;

import akka.actor.ActorSystem;
import com.rkcreadev.demo.akka.actor.extension.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AkkaConfiguration {

    private ApplicationContext applicationContext;

    @Autowired
    public AkkaConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public ActorSystem actorSystem() {
        return ActorSystem.create("testSystem");
    }

    @Bean
    public SpringExtension springExt() {
        SpringExtension springExtension = new SpringExtension();
        springExtension.initialize(applicationContext);

        return springExtension;
    }

}

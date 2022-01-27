package com.example.integrlinebyline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessageChannel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableAutoConfiguration
@IntegrationComponentScan
public class IntegrLineByLineApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(IntegrLineByLineApplication.class, args);
        List<String> strings = Arrays.asList("asfkh", "akhdewwqah");
        Collection<String> upcase = ctx.getBean(UpcaseClass.class).upcaseMethod(strings);
        System.out.println(upcase);
        ctx.close();
    }

    @MessagingGateway
    public interface UpcaseClass {
        @Gateway(requestChannel = "inChannel")
        Collection<String> upcaseMethod(Collection<String> strings);
    }

    @Bean
    public IntegrationFlow upcase () {
        return IntegrationFlows.from(inChannel())
                .split()
                .<String, String>transform(String::toUpperCase)
                .aggregate()
                .get();
    }

    @Bean
    public MessageChannel inChannel() {
        return new DirectChannel();
    }

}

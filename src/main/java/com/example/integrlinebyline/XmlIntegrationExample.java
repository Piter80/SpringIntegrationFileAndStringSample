package com.example.integrlinebyline;

import com.example.integrlinebyline.dto.ExampleBody;
import com.example.integrlinebyline.dto.ExampleEntity;
import com.example.integrlinebyline.dto.ExampleHeader;
import com.example.integrlinebyline.envelope.ExampleEnvelope;
import com.example.integrlinebyline.transformer.ExampleEntityToEnvelopTransformer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static javax.xml.bind.Marshaller.*;

@SpringBootApplication
@EnableIntegration
@Configuration
@Profile("XmlIntegrationExample")
public class XmlIntegrationExample {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(XmlIntegrationExample.class);
        application.setAdditionalProfiles("XmlIntegrationExample");
        ConfigurableApplicationContext ctx = application.run(args);

        List<ExampleEntity> entities = new ArrayList<>();

        entities.add(new ExampleEntity(
                new ExampleHeader(),
                ExampleBody
                        .builder()
                        .id("111")
                        .docNumber("111111")
                        .fileDest("c:/111")
                        .build())
        );
        entities.add(new ExampleEntity(
                new ExampleHeader(),
                ExampleBody
                        .builder()
                        .id("222")
                        .docNumber("22222222")
                        .fileDest("c:/2222")
                        .build())
        );

        InXmlGateway inGateway = ctx.getBean(InXmlGateway.class);
        inGateway.sentToFlow(entities);
    }

    @MessagingGateway
    public interface InXmlGateway {
        @Gateway(requestChannel = "exampleInChannel")
        Collection<ExampleEntity> sentToFlow(Collection<ExampleEntity> entities);
    }

    @Bean
    public IntegrationFlow xmlExample() {
        return IntegrationFlows.from("exampleInChannel")
                .handle(new PrintObjects(), "handle") //receive Entities
                .split()
                .handle(new PrintObjects(), "handle") //after splitting
                .transform(new ExampleEntityToEnvelopTransformer())
                .handle(new PrintObjects(), "handle") //after transform to xml
                .handle(new XmlToStringConverter(), "convert") //try to convert in XML String
                .get();

    }

    @Bean
    public MessageChannel exampleInChannel() {
        return new DirectChannel();
    }

    @Service
    public static class PrintObjects {
        public Object handle(Object o) {
            System.out.println(o.toString());
            return o;
        }
    }

    @Service
    public static class XmlToStringConverter {

        public Message<?> convert(Message<?> entity) {
            Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            marshaller.setMarshallerProperties(Map.of(
                    JAXB_FORMATTED_OUTPUT, true,
                    JAXB_FRAGMENT, true,
                    JAXB_ENCODING, "UTF-8"
            ));
            marshaller.setClassesToBeBound(ExampleEnvelope.class);
            //marshaller.setPackagesToScan("com.example.integrlinebyline.envelope");
            StringWriter writer = new StringWriter();
            Result result = new StreamResult(writer);
            marshaller.marshal(entity.getPayload(), result);
            StringBuffer sb = writer.getBuffer();
            String finalString = sb.toString();
            System.out.println(finalString);
            return entity;
        }


    }
}

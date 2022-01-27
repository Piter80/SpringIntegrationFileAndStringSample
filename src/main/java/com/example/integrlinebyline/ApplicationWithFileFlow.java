package com.example.integrlinebyline;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.File;

@SpringBootApplication
@IntegrationComponentScan
@Configuration
@Profile("ApplicationWithFileFlow")
public class ApplicationWithFileFlow {

    private final String FILE_SOURCE = "c:/mydir/";
    private static final String FILE_DEST = "d:/mydir/";

    @Bean
    //@Profile("ApplicationWithFileFlow")
    public MessageChannel fileFlowChannel() {
        return new DirectChannel();
    }

    @Bean
    //@Profile("ApplicationWithFileFlow")
    @InboundChannelAdapter(value = "fileFlowChannel", poller = @Poller(fixedDelay = "1000"))
    public MessageSource<File> fileReadingMessageSource() {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File(FILE_SOURCE));
        source.setFilter(new SimplePatternFileListFilter("*.txt"));
        return source;
    }

    @Bean
    //@Profile("ApplicationWithFileFlow")
    public IntegrationFlow moveIncomingFile() {
        return IntegrationFlows.from(fileFlowChannel())
                .handle(new HeaderEnricher(), "enrichMessage")
                .handle(new HandleFile(), "handle")
                .get();

    }

    @Service
    //@Profile("ApplicationWithFileFlow")
    public static class HandleFile {
        @SneakyThrows
        public void handle(Message<File> fileMessage) {
            System.out.println(fileMessage);
            File file = fileMessage.getPayload();
            File newFile = new File(fileMessage.getHeaders().get("fileDest").toString() + "/" + file.getName());
            FileUtils.copyFile(file, newFile);
            FileUtils.delete(file);
        }
    }

    @Service
    //@Profile("ApplicationWithFileFlow")
    public static class HeaderEnricher {
        public Message<File> enrichMessage(Message<File> message) {
            Message<File> fileDest = MessageBuilder.<File>fromMessage(message)
                    .copyHeaders(message.getHeaders())
                    .setHeader("fileDest", FILE_DEST)
                    .build();
            return fileDest;
        }
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ApplicationWithFileFlow.class);
        application.setAdditionalProfiles("ApplicationWithFileFlow");
        application.run(args);
    }
}

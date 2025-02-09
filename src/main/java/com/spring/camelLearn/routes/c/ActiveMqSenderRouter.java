package com.spring.camelLearn.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ActiveMqSenderRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        //TIMER
//        from("timer:active-mq-timer?period=10000")
//                .transform().constant("My message for ActiveMq")
//                .log("${body}")
//                .to("activemq:my-activemq-queue");
        //QUEUE

//        LISTENING FROM FILE
//                from("file:files/json")
//                .log("${body}")
//                .to("activemq:my-activemq-queue");
        from("file:files/xml")
                .log("${body}")
                .to("activemq:my-activemq-xml-queue");
    }
    }


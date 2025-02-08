package com.spring.camelLearn.routes.a;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public class FristRouteNew extends RouteBuilder {
    @Autowired
    private GetCurrentTime getCurrentTime;

//    @Override
//    public void configure() throws Exception {
//        //STEPS
//        //WANT TO LISTEN TO  A QUEUE
//
//        // PERFORM SOME TRANSFORMATION IN IT
//
//        //FINALLY WANT TO SAVE IN DATABASE
//    }

    //TEMPORARY STEPS
    @Override
    public void configure() throws Exception {
        //STEPS
        //WILL DO A TIMER (timer endpoint)
        // PERFORM SOME TRANSFORMATION IN IT
        //LOG IT

        from("timer:first-timer")
//                .transform().constant("Time " + LocalDateTime.now())
//                .bean("getCurrentTimeBean")
                //USING AUTOWIRED BEAN
                .bean(getCurrentTime)
                .process(new SimpleLoggingProcessor())
                .to("log:first-timer");
    }
}

//USING BEAN
@Component
class GetCurrentTime {
    public String getCurrentTime() {
        return "Time now " + LocalDateTime.now();
    }
}

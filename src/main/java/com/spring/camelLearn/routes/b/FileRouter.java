package com.spring.camelLearn.routes.b;

import org.apache.camel.Body;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FileRouter extends RouteBuilder {

    @Autowired
    private DeciderBean deciderBean;

    @Override
    public void configure() throws Exception {
        //Patterns
        //Default pattern - Pipeline Pattern
        //below is the implementation on Default Pattern i.e Pipeline

//        from("file:files/input")
//                .routeId("Files-Input-Route")
//                .transform().body(String.class )
//                .choice()
//                     .when(simple("${file:ext} ends with 'xml'"))
//                        .log("XML FILE")
//                     .when(simple("${body} contains 'USD'"))
//                        .log("Not and XML FILE BUT Contains USD")
//                    .otherwise()
//                        .log("Not an XML FILE")
//                .end()
//                .log("${body}")
//                .to("file:files/output");

        //Direct Route
        //Direct Route can be used to create reusable route
        //       from("file:files/input")
//                .routeId("Files-Input-Route")
//                .transform().body(String.class )
//                .choice()
//                    .when(simple("${file:ext} ends with 'xml'"))
//                        .log("XML FILE")
//                    .when(simple("${body} contains 'USD'"))
//                        .log("Not and XML FILE BUT Contains USD")
//                    .otherwise()
//                        .log("Not an XML FILE")
//                .end()
//                //below direct route can be used form anyhwere in the application
//                .to("direct://log-file-values")
//                .to("file:files/output");
//
//                from("direct:log-file-values")
//                        .log("${messageHistory} ${file:absolute.path}")
//                        .log("${file:name} ${file:name.ext} ${file:name.noext} ${file:onlyname}")
//                        .log("${routeId} ${camelId} ${body}");

        //complex choice
        //content based routing - choice() (pattern)

                from("file:files/input")
                        .routeId("Files-Input-Route")
                .transform().body(String.class )
                .choice()
                    .when(simple("${file:ext} ends with 'xml'"))
                        .log("XML FILE")
                    .when(method(deciderBean))
                        .log("Not and XML FILE BUT Contains USD")
                    .otherwise()
                        .log("Not an XML FILE")
                .end()
                //below direct route can be used form anyhwere in the application
//                .to("direct://log-file-values")
                .to("file:files/output");

        from("direct:log-file-values")
                .log("${messageHistory} ${file:absolute.path}")
                .log("${file:name} ${file:name.ext} ${file:name.noext} ${file:onlyname}")
                .log("${routeId} ${camelId} ${body}");
    }
}

//bean for working in complex choice

//@Component
//class DeciderBean {
//    Logger logger = LoggerFactory.getLogger(DeciderBean.class);
//
//    public boolean isThisConditionMet(String body) {
//        logger.info("Decider Bean {}", body);
//        return true;
//    }
//}

//adding body and header

@Component
class DeciderBean {
    Logger logger = LoggerFactory.getLogger(DeciderBean.class);

    public boolean isThisConditionMet(@Body String body, @Headers Map<String, String> headers) {
        logger.info("Decider Bean {} {}", body, headers);
        return true;
    }
}

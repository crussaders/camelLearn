package com.spring.camelLearn.routes.patterns;

import com.spring.camelLearn.CurrencyExchange;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EipPatternsRouter extends RouteBuilder {

    @Autowired
    SplitterComponet splitterComponet;

    @Override
    public void configure() throws Exception {

        //multicast pattern
//        from("timer:multicast?period=10000")
//                .multicast()
//                .to("log:something1", "log:something2","log:something3", "log:something4");
//
//        Splitter pattern
//        from("file:files/csv")
//                .unmarshal().csv()
//                .split(body())
//                .to("activemq:split-queue");

//      Exploring more features of splitter pattern
//        For output like Message, Message2, Message3
//        from("file:files/csv")
//                .convertBodyTo(String.class)
//                .split(body(),",")
//                .to("activemq:split-queue");
//        Using component Bean
//        from("file:files/csv")
//                .convertBodyTo(String.class)
//                .split(method(splitterComponet))
//                .to("activemq:split-queue");

        from("file:files/aggregate-json")
                .unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
                .aggregate(simple("${body.to}"), new ArrayListAggregationStrategy())
                .completionSize(3)
                .to("log:aggregate-json");
    }

    private class ArrayListAggregationStrategy implements AggregationStrategy {
        //1,2,3
        //null,1
        //result,2
        //result,3
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            Object newBody = newExchange.getIn().getBody();
            ArrayList<Object> list = null;
            if (oldExchange == null) {
                list = new ArrayList<Object>();
                list.add(newBody);
                newExchange.getIn().setBody(list);
                return newExchange;
            } else {
                list = oldExchange.getIn().getBody(ArrayList.class);
                list.add(newBody);
                return oldExchange;
            }
        }
    }
}

@Component
class SplitterComponet {
    public List<String> splitInput(String body) {
        return List.of("ABC","DEF","GHI");
    }
}
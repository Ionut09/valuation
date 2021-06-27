package co.arbelos.gtm.valuation.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.Queue;

@Configuration
@EnableJms
public class QueueConfiguration {

    @Bean(name="inmemory.autovit")
    public Queue csvAutovitQueue(){
        return new ActiveMQQueue("inmemory.autovit");
    }

    @Bean(name="inmemory.tradein")
    public Queue csvTradeInQueue(){
        return new ActiveMQQueue("inmemory.tradein");
    }


    @Bean(name="inmemory.observationmarket")
    public Queue observationMarketQueue(){
        return new ActiveMQQueue("inmemory.observationmarket");
    }

    @Bean(name="inmemory.mastertype")
    public Queue masterTypeQueue(){
        return new ActiveMQQueue("inmemory.mastertype");
    }
}

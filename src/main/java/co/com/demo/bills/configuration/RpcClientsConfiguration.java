package co.com.demo.bills.configuration;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcClientsConfiguration {

    @Value("${events.rpc.clients.exchange}")
    private String rpcExchange;

    @Value("${events.rpc.clients.queue}")
    private String rpcQueue;

    @Bean
    public DirectExchange directExchangeClients() {
        return new DirectExchange(rpcExchange);
    }

    @Bean
    public Queue responseClients(){
        return new Queue(rpcQueue);
    }

}
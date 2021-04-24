package co.com.demo.bills.configuration;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcProductsConfiguration {

    /*@Value("${events.rpc.products.exchange}")
    private String rpcExchange;

    @Value("${events.rpc.products.queue}")
    private String rpcQueue;

    @Bean
    public DirectExchange directExchangeProducts() {
        return new DirectExchange(rpcExchange);
    }

    @Bean
    public Queue responseProducts(){
        return new Queue(rpcQueue);
    }*/

}
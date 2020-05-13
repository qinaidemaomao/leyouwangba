package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lizichen
 * @create 2020-04-01 21:23
 */
@SpringBootApplication
@EnableDiscoveryClient
public class LeyouUpLoad1Application {
    public static void main(String[] args) {
        SpringApplication.run(LeyouUpLoad1Application.class);
    }
}

package com.zpark.book;

import com.zpark.book.utils.OrderTaskHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
        OrderTaskHandler.run();
    }

}

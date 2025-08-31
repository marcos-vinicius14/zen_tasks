package com.marcos.dev.zentasks.zen_task_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.marcos.dev.zentasks.zen_task_api.modules", "com.marcos.dev.zentasks.zen_task_api.common"})
public class ZenTaskApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZenTaskApiApplication.class, args);

	}

}

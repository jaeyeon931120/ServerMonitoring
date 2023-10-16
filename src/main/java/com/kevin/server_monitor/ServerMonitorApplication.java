package com.kevin.server_monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 스케줄러 사용
@EnableAspectJAutoProxy
@SpringBootApplication
public class ServerMonitorApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServerMonitorApplication.class, args);
	}
}
package com.kevin.server_monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class ServerMonitorApplication {

	public static void main(String[] args) {
		System.setProperty("logging.file.name",
				"./logs/monitoring." + new SimpleDateFormat("yyyy-MM-dd").format(new Date())+ ".log");

		SpringApplication.run(ServerMonitorApplication.class, args);
	}

}
package com.kevin.server_monitor.scheduled;

import com.kevin.server_monitor.controller.MonitorController;
import com.kevin.server_monitor.service.server.ServerInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@SpringBootApplication
public class MonitorScheduled {

    private static final Logger logger = LoggerFactory.getLogger(MonitorController.class);
    private final ServerInfoService serverInfoService;
    public MonitorScheduled(ServerInfoService serverInfoService) {
        this.serverInfoService = serverInfoService;
    }

    /*
    	-초 0-59 , - * /
    	-분 0-59 , - * /
    	-시 0-23 , - * /
    	-일 1-31 , - * ? / L W
    	-월 1-12 or JAN-DEC , - * /
    	-요일 1-7 or SUN-SAT , - * ? / L #
    	-년(옵션) 1970-2099 , - * /

        1 2 3 4 5 6 7
    	* * * * * * *
    	초,분,시,일,월,요일,년도(생략가능)

     */

    /**
     * 테스트용 스케줄러
     */
    @Scheduled(cron = "0 0/1 * * * ?") // 매 1분마다 한번씩 실행
    public void insertServerInfoScheduled() {
        serverInfoService.serverInfo();
        long heapSize = Runtime.getRuntime().totalMemory();
        String heapSize_M = (heapSize / (1024 * 1024)) + " MB";
        logger.error("Heap Size : {}", heapSize);
        logger.error("Heap Size(M) : {}", heapSize_M);
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 0시 0분 0초마다 한번씩 실행
    public void deleteServerLogScheduled() {
        serverInfoService.deleteServerLog();
        serverInfoService.deleteServerRaw();
    }
}

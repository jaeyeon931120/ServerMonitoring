package com.kevin.server_monitor.utill;

import java.util.List;
import java.util.Map;

public class RankingUtils {

    public List<Map<String, Object>> getRankValues(List<Map<String, Object>> datalist) {
        try {
            int[] cpu_rankArr = new int[datalist.size()];
            int[] memory_rankArr = new int[datalist.size()];
            int[] disk_rankArr = new int[datalist.size()];

            for(int i=0; i<datalist.size(); i++) {
                cpu_rankArr[i] = 1;
                memory_rankArr[i] = 1;
                disk_rankArr[i] = 1;
                for (Map<String, Object> stringObjectMap : datalist) {
                    String cpu1 = datalist.get(i).get("cpu").toString();
                    String memory1 = datalist.get(i).get("memory").toString();
                    String disk1 = datalist.get(i).get("disk").toString();
                    String cpu2 = stringObjectMap.get("cpu").toString();
                    String memory2 = stringObjectMap.get("memory").toString();
                    String disk2 = stringObjectMap.get("disk").toString();
                    double cpuDouble1 = Double.parseDouble(cpu1.substring(0, cpu1.length() - 1));
                    double memoryDouble1 = Double.parseDouble(memory1.substring(0, memory1.length() - 1));
                    double diskDouble1 = Double.parseDouble(disk1.substring(0, disk1.length() - 1));
                    double cpuDouble2 = Double.parseDouble(cpu2.substring(0, cpu2.length() - 1));
                    double memoryDouble2 = Double.parseDouble(memory2.substring(0, memory2.length() - 1));
                    double diskDouble2 = Double.parseDouble(disk2.substring(0, disk2.length() - 1));
                    if (cpuDouble1 < cpuDouble2) {
                        cpu_rankArr[i] = cpu_rankArr[i] + 1;
                    }
                    if (memoryDouble1 < memoryDouble2) {
                        memory_rankArr[i] = memory_rankArr[i] + 1;
                    }
                    if (diskDouble1 < diskDouble2) {
                        disk_rankArr[i] = disk_rankArr[i] + 1;
                    }
                }
                datalist.get(i).put("cpu_rank", cpu_rankArr[i]);
                datalist.get(i).put("memory_rank", memory_rankArr[i]);
                datalist.get(i).put("disk_rank", disk_rankArr[i]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return datalist;
    }
}

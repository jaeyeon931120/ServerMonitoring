package com.kevin.server_monitor.service.mybatis;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BatchService {

    private static final Logger logger = LoggerFactory.getLogger(BatchService.class);

    // Sql SessionFactory ( 배치 처리 )
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * Instantiates a new Batch service.
     *
     * @param sqlSessionFactory the sql session factory
     */
    public BatchService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * 분할 집합
     *
     * @param resList               꼭 분할 집합
     * @param count                 모든 집합 요소 개수
     * @return 복귀 분할 후 각 집합
     **/
    public static List<List<Map<String, Object>>> split(List<Map<String, Object>> resList, int count) {
        List<Map<String, Object>> itemList;

        if (resList == null || count < 1) {
            return Collections.emptyList();
        }

        List<List<Map<String, Object>>> ret = new ArrayList<>();
        int size = resList.size();
        if (size <= count) {
            // 데이터 부족 count 지정 크기
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            // 앞 pre 개 집합, 모든 크기 다 count 가지 요소
            for (int i = 0; i < pre; i++) {
                itemList = new ArrayList<>();
                for (int j = 0; j < count; j++) {
                    itemList.add(resList.get(i * count + j));
                }
                ret.add(itemList);
            }
            // last 진행이 처리
            if (last > 0) {
                itemList = new ArrayList<>();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * count + i));
                }
                ret.add(itemList);
            }
        }
        return ret;
    }

    /**
     * sqlSession 배치 처리
     */
    @Transactional
    public void sqlSessionBatch(List<Map<String, Object>> listMap, String process, String type) {

        String val_date;
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = Calendar.getInstance(Locale.KOREA);
        val_date = sdf.format(cal.getTime());

        long startTime = System.currentTimeMillis();
        try {
            List<List<Map<String, Object>>> ret = split(listMap, 30);
            for (List<Map<String, Object>> listmap : ret) {
                for(Map<String, Object> map : listmap) {
                    Map<String, Object> params;
                    params = map;
                    if(type.equals("data")) {
                        if (process.equals("insert")) {
                            sqlSession.insert("com.kevin.server_monitor.mapper.ServerDBMapper.insertServerInfo", params);
                        } else if (process.equals("update")) {
                            sqlSession.update("com.kevin.server_monitor.mapper.ServerDBMapper.updateServerSensor", params);
                        }
                    } else if(type.equals("log")) {
                        sqlSession.insert("com.kevin.server_monitor.mapper.ServerDBMapper.insertServerLog", params);
                    }
                }
            }
        } catch (Exception e) {
            if(process.equals("insert")) {
                logger.error("서버 정보 insert 도중 오류가 발생했습니다. (실패한 시간 : {})", val_date);
            } else if(process.equals("update")) {
                logger.error("서버 정보 update 도중 오류가 발생했습니다. (실패한 시간 : {})", val_date);
            }
            e.printStackTrace();
        } finally {
            sqlSession.flushStatements();
            sqlSession.close();
        }
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;

        if (process.equals("insert")) {
            logger.info("서버 정보 insert에 걸린 시간 : {}", resultTime);
        } else if (process.equals("update")) {
            logger.info("서버 정보 update에 걸린 시간 : {}", resultTime);
        }
    }
}

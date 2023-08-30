package com.kevin.server_monitor.service;

import com.kevin.server_monitor.common.paging.Pagination;
import com.kevin.server_monitor.common.paging.PagingResponse;
import com.kevin.server_monitor.dto.SearchDto;
import com.kevin.server_monitor.dto.ServerLogDto;
import com.kevin.server_monitor.mapper.ServerDBMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PagingService {
    private final ServerDBMapper serverDBMapper;

    public PagingService(ServerDBMapper serverDBMapper) {
        this.serverDBMapper = serverDBMapper;
    }

    /**
     * 게시글 리스트 조회
     * @param params - search conditions
     * @return list & pagination information
     */
    public PagingResponse<ServerLogDto> findAllLog(final ServerLogDto params) {

        SearchDto searchDto = params.getSearchDto();

        // 조건에 해당하는 데이터가 없는 경우, 응답 데이터에 비어있는 리스트와 null을 담아 반환
        int count = serverDBMapper.logCount(params);
        if (count < 1) {
            return new PagingResponse<>(Collections.emptyList(), null);
        }

        // Pagination 객체를 생성해서 페이지 정보 계산 후 SearchDto 타입의 객체인 params에 계산된 페이지 정보 저장
        Pagination pagination = new Pagination(count, searchDto);
        searchDto.setPagination(pagination);

        params.setSearchDto(searchDto);

        // 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로 리스트 데이터 조회 후 응답 데이터 반환
        List<ServerLogDto> list = serverDBMapper.detectServerLogList(params);
        return new PagingResponse<>(list, pagination);
    }
}

package com.school.project.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public interface PageService {
    static Pageable getPageable (int pageNumber, int pageSize, String sort){
        List<Sort.Order> sortByList = new ArrayList<>();
        for (String item : sort.split(",")) {
            String[] srt = item.split(":");
            if (srt.length != 2) continue;

            String direction = srt[1].toLowerCase();
            String field = srt[0];

            sortByList.add(new Sort.Order(direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, field));
        }
        return PageRequest.of(pageNumber-1, pageSize,Sort.by(sortByList));
    }
}

package com.kt.component.search.es.service;

import com.kt.component.search.es.repository.EsRepository;
import com.kt.component.search.es.repository.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EsService {

    @Autowired
    private EsRepository esRepository;

    public List<Hotel> getHotelFromTitle(String keyword) {
        return esRepository.findByTitleLike(keyword);
    }
}

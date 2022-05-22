package com.kt.component.search.es.controller;

import com.kt.component.search.es.repository.Hotel;
import com.kt.component.search.es.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class EsController {

    @Autowired
    private EsService esService;

    @GetMapping
    public String getRec(@RequestParam String keyword) {
        List<Hotel> hotelFromTitle = esService.getHotelFromTitle(keyword);
        if (hotelFromTitle != null && !hotelFromTitle.isEmpty()) {
            return hotelFromTitle.toString();
        }
        return "no data";
    }
}

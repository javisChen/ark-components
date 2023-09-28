package com.ark.component.search.es.repository;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "hotel1")
@Data
public class Hotel {

    @Id
    private String id;
    private String title;
    private String city;
    private String price;

}

package com.kt.component.search.es.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EsRepository extends CrudRepository<Hotel, String> {

    List<Hotel> findByTitleLike(String title);
}

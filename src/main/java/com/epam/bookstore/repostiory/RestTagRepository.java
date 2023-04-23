package com.epam.bookstore.repostiory;

import com.epam.bookstore.dto.Tag;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

//https://docs.spring.io/spring-data/rest/docs/current/reference/html/#reference
@Api
@RepositoryRestResource(collectionResourceRel = "tags", path = "tags")
public interface RestTagRepository extends CrudRepository<Tag, Integer> {

    Iterable<Tag> findAll(Sort sort);

}

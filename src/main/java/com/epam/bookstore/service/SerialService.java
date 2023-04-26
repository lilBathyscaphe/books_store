package com.epam.bookstore.service;


import com.epam.bookstore.dto.Serial;
import com.epam.bookstore.dto.Tag;
import com.epam.bookstore.dto.filter.FilterType;
import com.epam.bookstore.dto.filter.UserFilter;
import com.epam.bookstore.repostiory.EntitySpecification;
import com.epam.bookstore.repostiory.SerialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SerialService {

    @Autowired
    private SerialRepository serialRepository;

    private EntitySpecification<Serial> specification = new EntitySpecification();

    public List<Serial> getSerials(UserFilter userFilter) {
        Specification<Serial> baseSpecification = Specification
                .where(specification.attributeLikeValue("name", userFilter.getSearchQuery()));
        baseSpecification = addContainsTagCondition(userFilter, baseSpecification);
        baseSpecification = addFilterConditions(userFilter, baseSpecification);
        return serialRepository.findAll(baseSpecification);
    }

    private Specification<Serial> addFilterConditions(UserFilter userFilter, Specification<Serial> baseSpecification) {
        if(FilterType.MOST_POPULAR.equals(userFilter.getFilterType())){
            baseSpecification = baseSpecification.and(specification.attributeEquals("rate", 5));
        }
        return baseSpecification;
    }

    private Specification<Serial> addContainsTagCondition(UserFilter userFilter, Specification<Serial> baseSpecification) {
        Integer tagId = userFilter.getTagId();
        if(tagId != null) {
            baseSpecification = baseSpecification.and(specification.attributeContainTag("tags", new Tag(tagId)));
        }
        return baseSpecification;
    }


}

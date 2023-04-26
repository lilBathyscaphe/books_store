package com.epam.bookstore.repostiory;

import com.epam.bookstore.dto.Tag;
import org.springframework.data.jpa.domain.Specification;

import java.text.MessageFormat;
import java.util.List;

public class EntitySpecification<T> {

    public Specification<T> attributeLikeValue(String attributeName, String value) {
        return (root, query, builder) -> builder.like(root.get(attributeName), likeFormat(value));
    }

    public Specification<T> attributeContainTag(String attributeName, Tag tag) {
        return (root, query, builder) -> builder.in(root.join(attributeName)).value(List.of(tag));
    }

    public Specification<T> attributeEquals(String attributeName, int value) {
        return (root, query, builder) -> builder.equal(root.get(attributeName), value);
    }

    private String likeFormat(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }
}

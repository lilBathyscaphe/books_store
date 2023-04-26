package com.epam.bookstore.repostiory;

import com.epam.bookstore.dto.Serial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SerialRepository extends JpaRepository<Serial, Integer>, JpaSpecificationExecutor<Serial> {

}

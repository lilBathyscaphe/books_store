package com.epam.bookstore.repostiory;

import com.epam.bookstore.dto.History;
import org.springframework.data.jpa.repository.JpaRepository;


public interface HistoryRepository extends JpaRepository<History, Integer> {

}

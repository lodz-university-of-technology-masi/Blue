package com.masiblue.backend.repository;

import com.masiblue.backend.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findAllByPosition_Id(long id);
    List<Test> findAllByAuthor_Id(long id);
    List<Test> findAllByLanguage_Id(long id);
}
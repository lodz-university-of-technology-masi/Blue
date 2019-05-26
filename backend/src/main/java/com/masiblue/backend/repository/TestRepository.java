package com.masiblue.backend.repository;

import com.masiblue.backend.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findAllByPosition_Id(long id);
    List<Test> findAllByPosition_IdAndAuthor_Id(long positionId, long authorId);
    List<Test> findAllByAuthor_Id(long id);
    List<Test> findAllByLanguage_Id(long id);
    List<Test> findAllByLanguage_IdAndPosition_Id(long language_id, long position_id);
    List<Test> findAllByLanguage_IdAndAuthor_Id(long positionId, long authorId);
}

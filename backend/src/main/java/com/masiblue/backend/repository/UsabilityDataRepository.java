package com.masiblue.backend.repository;

import com.masiblue.backend.model.UsabilityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsabilityDataRepository extends JpaRepository<UsabilityData, Long> {
    List<UsabilityData> findAllByUsername(String username);
}

package com.library.repository;

import com.library.entity.FinePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FinePolicyRepository extends JpaRepository<FinePolicy, Long> {
    Optional<FinePolicy> findByCategory(String category);
}
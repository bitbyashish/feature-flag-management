package com.bitbyashish.featureflag.repository;

import com.bitbyashish.featureflag.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByFeatureFlagIdOrderByTimestampDesc(Long featureFlagId);
}


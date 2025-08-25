package com.bitbyashish.featureflag.repository;

import com.bitbyashish.featureflag.entity.FlagRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlagRuleRepository extends JpaRepository<FlagRule, Long> {

    List<FlagRule> findByUserGroup(String userGroup);

    List<FlagRule> findByFeatureFlagId(Long featureFlagId);
}


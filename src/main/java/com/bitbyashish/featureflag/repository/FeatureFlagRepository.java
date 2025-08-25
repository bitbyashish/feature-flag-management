package com.bitbyashish.featureflag.repository;

import com.bitbyashish.featureflag.entity.FeatureFlag;
import com.bitbyashish.featureflag.entity.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {

    Optional<FeatureFlag> findByNameAndEnvironment(String name, Environment environment);

    List<FeatureFlag> findAllByEnvironment(Environment environment);
}


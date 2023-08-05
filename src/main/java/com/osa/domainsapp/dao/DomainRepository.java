package com.osa.domainsapp.dao;

import com.osa.domainsapp.entity.DomainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DomainRepository extends JpaRepository<DomainEntity, Long> {

  Optional<DomainEntity> findByDomain(String domain);
}

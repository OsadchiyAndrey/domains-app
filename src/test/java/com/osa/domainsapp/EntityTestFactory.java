package com.osa.domainsapp;

import com.osa.domainsapp.entity.DomainEntity;
import org.apache.commons.lang3.RandomUtils;

public class EntityTestFactory {

  public static DomainEntity buildDomainEntity() {
    DomainEntity domainEntity = new DomainEntity();
    domainEntity.setDomain("http://test.com");
    domainEntity.setId(RandomUtils.nextLong());
    return domainEntity;
  }

}

package com.bezkoder.springjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bezkoder.springjwt.models.FCMuserTokenMobile;

@RepositoryRestResource
public interface FCMuserTokenMobileRepository extends JpaRepository<FCMuserTokenMobile,Long>{

}

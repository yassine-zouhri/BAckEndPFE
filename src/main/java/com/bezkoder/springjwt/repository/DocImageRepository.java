package com.bezkoder.springjwt.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.DocImage;
import java.lang.String;
import java.util.List;

@Repository
@Transactional
public interface DocImageRepository extends JpaRepository<DocImage,Long> {
	Optional<DocImage> findByUuidImage(String uuidimage);
}

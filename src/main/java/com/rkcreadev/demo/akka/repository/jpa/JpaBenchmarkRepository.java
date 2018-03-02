package com.rkcreadev.demo.akka.repository.jpa;

import com.rkcreadev.demo.akka.model.db.jpa.JpaBenchmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBenchmarkRepository extends JpaRepository<JpaBenchmark, Long> {
}

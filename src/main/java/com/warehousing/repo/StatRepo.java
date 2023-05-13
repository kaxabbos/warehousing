package com.warehousing.repo;

import com.warehousing.model.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatRepo extends JpaRepository<Stat, Long> {

}

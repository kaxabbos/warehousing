package com.warehousing.repo;

import com.warehousing.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepo extends JpaRepository<Warehouse, Long> {

}

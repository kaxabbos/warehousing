package com.warehousing.repo;

import com.warehousing.model.Product;
import com.warehousing.model.enums.Category;
import com.warehousing.model.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findAllByCategoryAndStatus(Category category, ProductStatus status);

    List<Product> findAllByStatus(ProductStatus productStatus);
}

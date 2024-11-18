package com.tale.repository;

import com.tale.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Product entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Additional custom queries can be added here if needed
}

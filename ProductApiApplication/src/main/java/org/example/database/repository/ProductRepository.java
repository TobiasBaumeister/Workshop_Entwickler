package org.example.database.repository;

import java.util.List;
import org.example.database.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findByCategoryId(Long categoryId);
}
package com.autoflex.inventory.repository;

import com.autoflex.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface ProductRepository extends JpaRepository<Product, UUID>{
}

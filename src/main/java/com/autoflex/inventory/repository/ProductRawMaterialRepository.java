package com.autoflex.inventory.repository;

import com.autoflex.inventory.entity.ProductRawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProductRawMaterialRepository extends JpaRepository<ProductRawMaterial, UUID>{
    List<ProductRawMaterial> findByProductId(UUID productId);
}

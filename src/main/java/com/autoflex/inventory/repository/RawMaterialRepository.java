package com.autoflex.inventory.repository;

import com.autoflex.inventory.entity.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface RawMaterialRepository extends JpaRepository<RawMaterial, UUID>{
}

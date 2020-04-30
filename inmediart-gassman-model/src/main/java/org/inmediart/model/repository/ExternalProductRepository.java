package org.inmediart.model.repository;

import org.inmediart.model.entity.ExternalProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalProductRepository extends JpaRepository<ExternalProduct, Long> {
}

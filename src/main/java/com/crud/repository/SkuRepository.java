package com.crud.repository;

import com.crud.persistence.Sku;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface para acesso a banco de Skus
 */
public interface SkuRepository extends JpaRepository<Sku, Integer>{
}

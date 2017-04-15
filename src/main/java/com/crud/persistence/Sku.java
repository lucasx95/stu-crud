package com.crud.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entidade para gerenciamento de produtos
 */
@Entity
public class Sku {

    /**
     * Id do Sku
     */
    @Id
    @Column(name = "ID")
    private Integer id;

    /**
     * Id do produto associado
     */
    @Column(name = "PRODUCT_ID")
    private Integer productId;


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return this.productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

}

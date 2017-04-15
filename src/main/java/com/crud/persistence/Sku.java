package com.crud.persistence;

import javax.persistence.*;

/**
 * Entidade para gerenciamento de produtos
 */
@Entity
public class Sku {

    /**
     * Id do Sku
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Integer id;

    /**
     * Id externo do Sku
     */
    @Column(name = "SKU_ID")
    private Integer skuId;

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

    public Integer getSkuId() {
        return this.skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getProductId() {
        return this.productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

}

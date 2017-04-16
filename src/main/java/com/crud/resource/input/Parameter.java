package com.crud.resource.input;

import com.crud.persistence.Sku;

/**
 * Dto de mapeamento para parâmetro
 */
public class Parameter {

    private Integer idSku;

    private Integer idProduto;

    public Integer getIdSku() {
        return this.idSku;
    }

    public void setIdSku(Integer idSku) {
        this.idSku = idSku;
    }

    public Integer getIdProduto() {
        return this.idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    /**
     * Retorna sku equivalente
     */
    public Sku toSku() {
        final Sku sku = new Sku();
        sku.setProductId(this.getIdProduto());
        sku.setSkuId(this.getIdSku());
        return sku;
    }
}

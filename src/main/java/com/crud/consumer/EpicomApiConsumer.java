package com.crud.consumer;

import com.crud.persistence.Sku;
import com.crud.utils.RequestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.util.Pair;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe paar consumir api da Epicom
 */
@Component
public class EpicomApiConsumer {

    private RestTemplate template;

    @Autowired
    public EpicomApiConsumer() {
        this.template = new RestTemplate();
    }

    /**
     * Pega os detalhes de um SKu
     *
     * @param sku sku para pegar os detalhes
     * @return SkuDetail os detalhes do Sku caso consiga pelo request, caso não
     * retorna detalhes nulos
     **/
    private SkuDetail skuDetail(Sku sku) {
        try {
            final ResponseEntity<SkuDetail> response = template.exchange(filterRequestForSku(sku), HttpMethod.GET,
                    new HttpEntity<String>(basicAuthenticationHeader()), SkuDetail.class);
            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retorna os skus filtrados e ordenados em ordem ascendente de preco
     * @param skus os skus a serem filtradors e ordenados
     * @return os skus filtrados e ordenados
     */
    public List<Sku> getFilteredSkus(List<Sku> skus) {
        return skus.stream().map(sku -> new Pair<>(skuDetail(sku), sku))
                .filter(pair -> pair.getKey() != null && pair.getKey().valid())
                .sorted(Comparator.comparing(sku -> sku.getKey().getPreco()))
                .map(Pair::getValue).collect(Collectors.toList());
    }

    private String filterRequestForSku(Sku sku) {
        return "https://sandboxmhubapi.epicom.com.br/v1/marketplace/produtos/" + sku.getProductId() + "/skus/" + sku.getSkuId();
    }

    /**
     * Gera header com autenticação
     *
     * @return header com BasiAuthentication setado
     */
    private HttpHeaders basicAuthenticationHeader() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic "
                + new Base64()
                .encodeToString(("897F8D21A9F5A:Ip15q6u7X15EP22GS36XoNLrX2Jz0vqq")
                        .getBytes()));
        return headers;
    }

    /**
     * Dto de retorno para detalhe de Sku
     */
    public class SkuDetail {
        private boolean disponivel;

        private BigDecimal preco;

        public boolean getDisponivel() {
            return this.disponivel;
        }

        public void setDisponivel(boolean disponivel) {
            this.disponivel = disponivel;
        }

        public BigDecimal getPreco() {
            return this.preco;
        }

        public void setPreco(BigDecimal preco) {
            this.preco = preco;
        }

        /**
         * Retorna se é um sku valido
         *
         * @return true se disponivel, preco > 10 e preco < 40
         */
        public boolean valid() {
            return disponivel
                    && preco.compareTo(BigDecimal.TEN) > 0
                    && preco.compareTo(BigDecimal.valueOf(40)) < 0;
        }
    }
}

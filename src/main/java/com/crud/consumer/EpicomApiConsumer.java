package com.crud.consumer;

import com.crud.persistence.Sku;
import com.crud.utils.RequestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

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
     * Filtra um Skus disponíveis e com preço entre 10 e 40
     *
     * @param sku sku a ser validado
     * @return True se atende aos critérios, false caso não
     */
    public Boolean filterSku(Sku sku) {
        try {
            final ResponseEntity<SkuDetail> response = template.exchange(filterRequestForSku(sku), HttpMethod.GET,
                    new HttpEntity<String>(basicAuthenticationHeader()), SkuDetail.class);
            return response.getStatusCode().equals(HttpStatus.OK) && response.hasBody() &&
                    response.getBody().valid();
        } catch (Exception e) {
            return false;
        }
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

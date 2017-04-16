package com.crud.resource;

import com.crud.SkuRestApplication;
import com.crud.consumer.EpicomApiConsumer;
import com.crud.exception.ExceptionMessages;
import com.crud.interceptor.ExceptionInterceptor;
import com.crud.persistence.Sku;
import com.crud.repository.SkuRepository;
import com.crud.utils.RequestUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Teste para requests do SkuResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SkuRestApplication.class)
@Transactional
public class SkuResourceTest {

    private MockMvc skuRestMock;

    private Sku sku;

    @Autowired
    private SkuRepository skuRepository;

    @Mock
    private EpicomApiConsumer epicomApiConsumer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SkuResource skuResource = new SkuResource(skuRepository, epicomApiConsumer);
        sku = createSku();
        this.skuRestMock = MockMvcBuilders.standaloneSetup(skuResource)
                .setControllerAdvice(new ExceptionInterceptor())
                .build();
    }

    @Test
    public void createSku_IdNull_OkNewSkuBody() throws Exception {
        this.skuRestMock
                .perform(post("/skus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(RequestUtils.toJsonString(sku)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.skuId").value(sku.getSkuId()))
                .andExpect(jsonPath("$.productId").value(sku.getProductId()));

    }


    @Test
    public void createSku_IdNotNull_BadRequest() throws Exception {
        this.sku.setId(1);
        this.skuRestMock
                .perform(post("/skus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(RequestUtils.toJsonString(sku)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.message").value(ExceptionMessages.ID_SHOULD_BE_NULL.name()));
    }

    @Test
    public void updateSku_IdNotExistent_OkNewSku() throws Exception {
        this.skuRestMock.perform(put("/skus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(RequestUtils.toJsonString(this.sku)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Matchers.notNullValue()));
    }

    @Test
    public void updateSku_IdExists_OkUpdatedSku() throws Exception {
        this.sku = this.skuRepository.save(sku);
        this.sku.setSkuId(2);
        this.sku.setProductId(2);
        this.skuRestMock.perform(put("/skus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(RequestUtils.toJsonString(this.sku)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(this.sku.getId()))
                .andExpect(jsonPath("$.skuId").value(this.sku.getSkuId()))
                .andExpect(jsonPath("$.productId").value(this.sku.getProductId()));
    }

    @Test
    public void getAllSku_AllSkuIncluded() throws Exception {
        this.sku = this.skuRepository.save(sku);
        final MvcResult result = this.skuRestMock.perform(get("/skus"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(RequestUtils.toJsonString(this.sku)));
    }

    @Test
    public void getSku_IdInvalid_NotFound() throws Exception {
        this.skuRestMock.perform(get("/skus/{id}", Integer.MAX_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ExceptionMessages.INVALID_ID.name()));
    }

    @Test
    public void getSku_ValidId_OkSkuBody() throws Exception {
        // adiciona sku ao banco
        this.sku = skuRepository.save(this.sku);
        this.skuRestMock.perform(get("/skus/{id}", sku.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(sku.getId()))
                .andExpect(jsonPath("$.skuId").value(sku.getSkuId()))
                .andExpect(jsonPath("$.productId").value(sku.getProductId()));
    }

    @Test
    public void deleteSku_IdInvalid_Ok() throws Exception {
        // adiciona sku ao banco
        this.sku = skuRepository.save(this.sku);
        this.skuRestMock.perform(delete("/skus/{id}", sku.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteSku_Id_Ok() throws Exception {
        // adiciona sku ao banco
        this.skuRestMock.perform(delete("/skus/{id}", Integer.MAX_VALUE))
                .andExpect(status().isBadRequest());
    }

    /**
     * Cria sku com id vazio
     *
     * @return Sku com skuId e productId setados
     */
    private Sku createSku() {
        final Sku sku = new Sku();
        sku.setSkuId(1);
        sku.setProductId(1);
        return sku;
    }

}

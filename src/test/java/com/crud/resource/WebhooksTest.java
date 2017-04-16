package com.crud.resource;

import com.crud.SkuRestApplication;
import com.crud.consumer.EpicomActions;
import com.crud.exception.ExceptionMessages;
import com.crud.interceptor.ExceptionInterceptor;
import com.crud.persistence.Sku;
import com.crud.repository.SkuRepository;
import com.crud.resource.input.Notification;
import com.crud.resource.input.Parameter;
import com.crud.resource.utils.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes para Webhooks
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SkuRestApplication.class)
@Transactional
public class WebhooksTest {

    private MockMvc skuRestMock;

    private Notification notification;

    @Autowired
    private SkuRepository skuRepository;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final Webhooks webhooks = new Webhooks(skuRepository);
        this.notification = buildNotification();
        this.skuRestMock = MockMvcBuilders.standaloneSetup(webhooks)
                .setControllerAdvice(new ExceptionInterceptor())
                .build();
    }

    @Test
    public void handleNotification_createSku_Ok() throws Exception {
        final Long total = skuRepository.count();
        this.skuRestMock.perform(post("/webhooks").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.toJsonString(this.notification)))
                .andExpect(status().isOk());
        assertEquals(total + 1L, skuRepository.count());
    }

    @Test
    public void handleNotification_Invalid_BadRequest() throws Exception {
        final Notification invalid = buildNotification();
        invalid.setTipo("INVALID");
        this.skuRestMock.perform(post("/webhooks").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.toJsonString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ExceptionMessages.INVALID_ACTION.name()));
    }

    /**
     * Monta notificação válida
     */
    private Notification buildNotification() {
        final Parameter parameter = new Parameter();
        parameter.setIdProduto(1);
        parameter.setIdSku(1);

        final Notification notification = new Notification();
        notification.setTipo(EpicomActions.CREATE_SKU.getAction());
        notification.setParametros(parameter);
        return notification;
    }
}

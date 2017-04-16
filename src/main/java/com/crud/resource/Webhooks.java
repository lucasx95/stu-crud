package com.crud.resource;

import com.crud.consumer.EpicomActions;
import com.crud.exception.ValidationException;
import com.crud.repository.SkuRepository;
import com.crud.resource.input.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller para tratar integrações
 */
@Controller
@RequestMapping(value = "/webhooks")
public class Webhooks {

    private final SkuRepository skuRepository;

    @Autowired
    public Webhooks(SkuRepository skuRepository) {
        this.skuRepository = skuRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> handleNotification(@RequestBody Notification notification) throws ValidationException {
        final EpicomActions action = EpicomActions.fromActionString(notification.getTipo());
        switch (action) {
            case CREATE_SKU:
                this.skuRepository.save(notification.getParametros().toSku());
                break;
        }
        return ResponseEntity.ok(null);
    }

}

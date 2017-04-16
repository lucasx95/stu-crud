package com.crud.resource;

import com.crud.consumer.EpicomApiConsumer;
import com.crud.exception.ExceptionMessages;
import com.crud.exception.ServiceException;
import com.crud.persistence.Sku;
import com.crud.repository.SkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller para requisições Sku
 */
@Controller
@RequestMapping(value = "/skus")
public class SkuResource {

    private final SkuRepository skuRepository;

    private final EpicomApiConsumer epicomApi;

    @Autowired
    public SkuResource(SkuRepository skuRepository, EpicomApiConsumer epicomApi) {
        this.skuRepository = skuRepository;
        this.epicomApi = epicomApi;
    }

    /**
     * Cria um Sku
     *
     * @param sku o sku a ser criado
     * @return Body com objeto criado e código 200
     * @throws ServiceException caso o Sku já exista
     */
    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sku> createSku(@RequestBody Sku sku)
            throws ServiceException {
        if (sku.getId() != null) {
            throw new ServiceException(ExceptionMessages.ID_SHOULD_BE_NULL);
        }
        final Sku result = skuRepository.save(sku);
        return ResponseEntity.ok(result);
    }

    /**
     * Atualiza um Sku existente
     *
     * @param sku o sku a ser atualizado
     * @return sku atualizado no body com código 200
     */
    @RequestMapping(method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sku> updateSku(@RequestBody Sku sku) throws ServiceException {
        if (sku.getId() == null) {
            return createSku(sku);
        }
        final Sku result = skuRepository.save(sku);
        return ResponseEntity.ok(result);
    }

    /**
     * Retorna todos os Skus
     *
     * @return lista de Skus com código 200
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sku>> getAllSkus() {
        return ResponseEntity.ok(skuRepository.findAll());
    }

    /**
     * Retorn dados de um Sku
     *
     * @param id o id do Sku
     * @return o sku com id passado no body e código 200
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Sku> getSku(@PathVariable Integer id) throws ServiceException {
        return Optional.ofNullable(skuRepository.findOne(id))
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElseThrow(() -> new ServiceException(ExceptionMessages.INVALID_ID));
    }

    /**
     * Deleta um sku
     *
     * @param id id do sku a ser deletado
     * @return código 200 em caso de sucesso
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteSku(@PathVariable Integer id) {
        skuRepository.delete(id);
        return ResponseEntity.ok(null);
    }

    /**
     * Pega todos Skus filtrados por disponíveis e preço entre 10 e 40
     * @return os Skus que se adequam a condição
     */
    @RequestMapping(value = "/filtered",
            method= RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sku>> getFiltered() {
        return ResponseEntity.ok(
                epicomApi.getFilteredSkus(this.skuRepository.findAll()));
    }
}

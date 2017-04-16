package com.crud.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Classe para auxílio em requests
 */
public class RequestUtils {


    /**
     * Converte um objeto para json
     * @param object o objeto a ser convertido
     * @return o Json em formato de string
     * @throws JsonProcessingException em caso de falha na Serialização
     */
    public static String toJsonString(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

}

package com.crud.resource.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Classe para auxílio nos testes
 */
public class TestUtils {


    /**
     * Converte um objeto para json
     * @param object o objeto a ser convertido
     * @return o Json em formato de string
     * @throws JsonProcessingException em caso de falha na Serialização
     */
    public static String toJsonString(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    /**
     * Converte um json para objeto java
     * @param json o json em string
     * @param clazz a classe de saida desejada
     * @param <T> o tipo da classe
     * @return o objeto da classe T
     * @throws IOException caso de falha ao criar o objeto
     */
    public static <T> T toObjejct(String json, Class<T> clazz) throws IOException {
        return new ObjectMapper().readValue(json,clazz);
    }

}

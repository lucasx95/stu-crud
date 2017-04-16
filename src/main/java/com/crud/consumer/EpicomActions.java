package com.crud.consumer;

import com.crud.exception.ExceptionMessages;
import com.crud.exception.ValidationException;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Ações possíveis via webhook
 */
public enum EpicomActions {

    CREATE_SKU("criacao_sku");

    /**
     * Ação a ser realizada
     */
    private String action;

    /**
     * Mantém mapa estatico para mapeamento inverso
     */
    private static final Map<String, EpicomActions> lookup =
            Arrays.stream(EpicomActions.values())
                    .collect(Collectors.toMap(EpicomActions::getAction, Function.identity()));

    EpicomActions(String acition) {
        this.action = acition;
    }

    /**
     * Valida e retorna enum a partir da string action
     * @param action a ação como recebida
     * @return o enum equivalente
     * @throws ValidationException caso o o enum equivalente não exista
     */
    public static EpicomActions fromActionString(String action) throws ValidationException {
        return Optional.ofNullable(lookup.get(action))
                .orElseThrow(() -> new ValidationException(ExceptionMessages.INVALID_ACTION));
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

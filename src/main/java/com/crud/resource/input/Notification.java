package com.crud.resource.input;

import java.util.Date;
import java.util.List;

/**
 * Dto de entrada de notificações
 */
public class Notification {

    /**
     * Tipo do request
     */
    private String tipo;

    /**
     * Data de envio
     */
    private Date dataEnvio;

    /**
     * Lista de parâmetros
     */
    private Parameter parametros;

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Date getDataEnvio() {
        return this.dataEnvio;
    }

    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public Parameter getParametros() {
        return this.parametros;
    }

    public void setParametros(Parameter parametros) {
        this.parametros = parametros;
    }
}

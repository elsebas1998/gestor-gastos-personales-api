package com.jsca.gestor_gastos_personales_api.persistence.service;

import com.jsca.gestor_gastos_personales_api.persistence.entities.FinancialTipEntity;

import java.util.List;

public interface FinancialTipService {

        /**
         * Obtener un consejo aleatorio
         */
        FinancialTipEntity getRandomTip() throws Exception;

        /**
         * Obtener N consejos aleatorios
         */
        List<FinancialTipEntity> getRandomTips(int limit) throws Exception;

        /**
         * Obtener consejo aleatorio de un tipo específico
         */
        FinancialTipEntity getRandomTipByType(String tipType) throws Exception;

        /**
         * Obtener todos los consejos activos
         */
        List<FinancialTipEntity> getAllActiveTips() throws Exception;

}

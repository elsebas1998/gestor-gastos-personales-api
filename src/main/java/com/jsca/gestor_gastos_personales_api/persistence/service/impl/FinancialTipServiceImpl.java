package com.jsca.gestor_gastos_personales_api.persistence.service.impl;

import com.jsca.gestor_gastos_personales_api.persistence.entities.FinancialTipEntity;
import com.jsca.gestor_gastos_personales_api.persistence.repository.FinancialTipRepository;
import com.jsca.gestor_gastos_personales_api.persistence.service.FinancialTipService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FinancialTipServiceImpl implements FinancialTipService {

    private final FinancialTipRepository financialTipRepository;

    @Override
    public FinancialTipEntity getRandomTip() throws Exception {
        return financialTipRepository.findRandomTip()
                .orElseThrow(() -> new Exception("No hay consejos financieros disponibles"));
    }

    @Override
    public List<FinancialTipEntity> getRandomTips(final int limit) throws Exception {
        if (limit <= 0) {
            throw new Exception("El límite debe ser mayor a cero");
        }

        List<FinancialTipEntity> tips = financialTipRepository.findRandomTips(limit);

        if (tips.isEmpty()) {
            throw new Exception("No hay consejos financieros disponibles");
        }

        return tips;
    }

    @Override
    public FinancialTipEntity getRandomTipByType(final String tipType) throws Exception {
        return financialTipRepository.findRandomTipByType(tipType)
                .orElseThrow(() -> new Exception("No hay consejos financieros del tipo especificado"));
    }

    @Override
    public List<FinancialTipEntity> getAllActiveTips() throws Exception {
        List<FinancialTipEntity> tips = financialTipRepository.findByIsActiveTrue();

        if (tips.isEmpty()) {
            throw new Exception("No hay consejos financieros disponibles");
        }

        return tips;
    }
}

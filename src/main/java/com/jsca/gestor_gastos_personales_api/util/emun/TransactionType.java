package com.jsca.gestor_gastos_personales_api.util.emun;

public enum TransactionType {

    INGRESO("Ingreso"),
    EGRESO("Egreso");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}


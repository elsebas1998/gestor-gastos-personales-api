package com.jsca.gestor_gastos_personales_api.util.emun;

public enum TipType {
    AHORRO("Ahorro"),
    INVERSION("Inversión"),
    DEUDA("Deuda"),
    PRESUPUESTO("Presupuesto"),
    GENERAL("General");

    private final String displayName;

    TipType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

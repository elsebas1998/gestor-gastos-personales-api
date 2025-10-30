package com.jsca.gestor_gastos_personales_api.util.emun;

public enum PlanStatus {
    ACTIVE("Activo"),
    COMPLETED("Completado"),
    CANCELLED("Cancelado"),
    PAUSED("Pausado");

    private final String displayName;

    PlanStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

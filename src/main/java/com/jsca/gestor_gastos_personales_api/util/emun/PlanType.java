package com.jsca.gestor_gastos_personales_api.util.emun;

public enum PlanType {

    EMERGENCY_FUND("Fondo de Emergencia"),
    SHORT_TERM_GOAL("Meta a Corto Plazo"),
    LONG_TERM_GOAL("Meta a Largo Plazo");

    private final String displayName;

    PlanType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

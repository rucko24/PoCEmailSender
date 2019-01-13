package com.example.javamail.executarnextmonth;

public enum Tiempos {

    SEGUNDOS("SEGUNDOS"),
    MINUTOS("MINUTOS"),
    HORAS("HORAS"),
    MESES("MESES");

    private String tiposDeTiempos;

    Tiempos(final String tiposDeTiempos) {
        this.tiposDeTiempos = tiposDeTiempos;
    }
    public String getTiposDeTiempos() {
        return tiposDeTiempos;
    }

}
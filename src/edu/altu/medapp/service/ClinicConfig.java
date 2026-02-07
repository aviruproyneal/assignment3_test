package edu.altu.medapp.service;

import java.time.LocalTime;

public class ClinicConfig {
    private static ClinicConfig instance;

    private final LocalTime open = LocalTime.of(9, 0);
    private final LocalTime close = LocalTime.of(17, 0);

    private ClinicConfig() {}

    public static ClinicConfig getInstance() {
        if (instance == null) {
            instance = new ClinicConfig();
        }
        return instance;
    }

    public boolean isOpen(LocalTime time) {
        return !time.isBefore(open) && !time.isAfter(close);
    }
}
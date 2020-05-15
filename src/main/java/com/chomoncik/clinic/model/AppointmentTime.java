package com.chomoncik.clinic.model;

public enum AppointmentTime {
    FIFTEEN(15),
    THIRTY(30),
    FORTY_FIVE(45),
    SIXTY(60);

    private final int value;

    AppointmentTime(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

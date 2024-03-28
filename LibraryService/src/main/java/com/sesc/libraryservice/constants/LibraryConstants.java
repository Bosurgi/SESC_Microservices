package com.sesc.libraryservice.constants;

import lombok.Getter;

@Getter
public enum LibraryConstants {
    FINE_PER_DAY(50),
    MAX_DAYS(14),
    FINE_NAME("LIBRARY_FINE"),
    DEFAULT_PIN("0000"),
    STUDENT_ROLE("STUDENT"),
    REGISTERED_ROLE("REGISTERED"),
    ADMIN_ROLE("ADMIN");

    private final Object value;

    LibraryConstants(Object value) {
        this.value = value;
    }

    public String getStringValue() {
        return value.toString();
    }

    public long getLongValue() {
        return Long.parseLong(value.toString());
    }

    /**
     * It returns the value of the constant as a double.
     *
     * @return the value of the constant as a double
     */
    public double getDoubleValue() {
        return Double.parseDouble(value.toString()) / 100;
    }

}

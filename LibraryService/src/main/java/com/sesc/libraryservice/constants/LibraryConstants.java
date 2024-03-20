package com.sesc.libraryservice.constants;

import lombok.Getter;

@Getter
public enum LibraryConstants {
    FINE_PER_DAY(50),
    MAX_DAYS(14),
    FINE_NAME("LIBRARY_FINE");

    private final Object value;

    LibraryConstants(Object value) {
        this.value = value;
    }

    public String getStringValue() {
        return value.toString();
    }

    public long getLongValue() {
        return (long) value;
    }

    /**
     * It returns the value of the constant as a double.
     * @return the value of the constant as a double
     */
    public double getDoubleValue() {
        return (double) value/100.0;
    }

}

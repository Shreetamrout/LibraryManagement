package com.library.entity;

public enum MembershipType {
    BASIC(2),
    PREMIUM(5);

    private final int defaultLimit;

    MembershipType(int defaultLimit) {
        this.defaultLimit = defaultLimit;
    }

    public int getDefaultLimit() {
        return defaultLimit;
    }
}
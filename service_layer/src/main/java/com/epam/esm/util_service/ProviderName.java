package com.epam.esm.util_service;

public enum ProviderName {
    LOCAL,
    GOOGLE,
    GITHUB;

    @Override
    public String toString() {

        return this.name().toLowerCase();
    }
}

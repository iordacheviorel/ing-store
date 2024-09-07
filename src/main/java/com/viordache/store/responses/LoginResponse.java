package com.viordache.store.responses;

public record LoginResponse(String token, long expiresIn) {
    public LoginResponse() {
        this(null, 0);
    }
}

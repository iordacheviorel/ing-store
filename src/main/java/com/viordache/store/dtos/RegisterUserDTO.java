package com.viordache.store.dtos;

public record RegisterUserDTO(
    String email,
    String password,
    String fullName
) {}
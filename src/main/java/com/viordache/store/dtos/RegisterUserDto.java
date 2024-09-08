package com.viordache.store.dtos;

public record RegisterUserDto(
    String email,
    String password,
    String fullName
) {}
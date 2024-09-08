package com.viordache.store.dtos;

import java.math.BigDecimal;

public record ItemDTO (
         String name,
         String description,
         Integer quantity,
         BigDecimal price,
         String sku
) {}


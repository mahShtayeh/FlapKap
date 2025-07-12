package com.flapkap.vendingmachine.service.impl;

import com.flapkap.vendingmachine.repository.ProductRepository;
import com.flapkap.vendingmachine.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("productService")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
}

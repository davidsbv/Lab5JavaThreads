package com.dperez.CarRegistry.service.impl;

import com.dperez.CarRegistry.repository.BrandRepository;
import com.dperez.CarRegistry.service.BrandService;
import com.dperez.CarRegistry.service.model.Brand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class BrandServiceImpl implements BrandService {

    private BrandRepository brandRepository;

    @Override
    public Brand addBrand(Brand brand) throws IllegalArgumentException {

        return null;
    }

    @Override
    public CompletableFuture<List<Brand>> addBunchBrands(List<Brand> brands) {
        return null;
    }

    @Override
    public Brand getBrandById(Integer id) {
        return null;
    }

    @Override
    public Brand updateBrandById(Integer id, Brand brand) {
        return null;
    }

    @Override
    public CompletableFuture<List<Brand>> updateBunchBrands(List<Brand> brands) {
        return null;
    }

    @Override
    public void deleteCarById(Integer id) {

    }

    @Override
    public CompletableFuture<List<Brand>> getAllBrands() {
        return null;
    }
}

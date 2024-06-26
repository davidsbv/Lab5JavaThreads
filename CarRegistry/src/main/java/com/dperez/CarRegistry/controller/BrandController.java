package com.dperez.CarRegistry.controller;

import com.dperez.CarRegistry.service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

}

package com.dperez.CarRegistry.service;

import com.dperez.CarRegistry.service.model.Car;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public interface CarService {

    Car addCar(Car car) throws DataIntegrityViolationException;
    CompletableFuture<List<Car>> addCars(List<Car> cars);
    Car getCarById(Integer id);
    Car updateCarById(Integer id, Car car);
    CompletableFuture<List<Car>> updateCars(List<Car> cars);
    void deleteCarById(Integer id);
    List<Car> getAllCars();
}

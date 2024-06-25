package com.dperez.CarRegistry.controller;

import com.dperez.CarRegistry.controller.dtos.CarDTO;
import com.dperez.CarRegistry.controller.dtos.CarDTOAndBrand;
import com.dperez.CarRegistry.controller.mapper.CarDTOAndBrandMapper;
import com.dperez.CarRegistry.controller.mapper.CarDTOMapper;
import com.dperez.CarRegistry.service.CarService;
import com.dperez.CarRegistry.service.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping("add-car")
    public ResponseEntity<?> addCar(@RequestBody CarDTO carDTO){

        try {
            // Se convierte carDTO a Car y se utiliza en la llmada al método addCar.
            // Cuando se guarda se devuelve en newCarDTO  y se muestra la respuesta
            Car car = CarDTOMapper.INSTANCE.carDTOToCar(carDTO);
            Car newCar = carService.addCar(car);
            CarDTOAndBrand newCarDTOAndBrand = CarDTOAndBrandMapper.INSTANCE.carToCarDTOAndBrand(newCar);
            log.info("New Car added");
            return ResponseEntity.ok(newCarDTOAndBrand);

        } catch (IllegalArgumentException e) {
            // Error por Id ya existente.
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        } catch (Exception e){
            log.error("Error while adding new car");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @PostMapping("add-cars")
//    public CompletableFuture<List<CarDTO>> addCars(@RequestBody List<CarDTO> carDTOs){
//
//        // Convierte la lista de CarDTO a Car y llama al método addCars del servicio
//        List<Car> cars = carDTOs.stream().map(CarDTOMapper.INSTANCE::carDTOToCar).collect(Collectors.toList());
//        return carService.addCars(cars).thenApply(addedCars -> {
//            List<CarDTO> addedCarDTOs =
//        })
//    }

    @GetMapping("get-car/{id}")
    public ResponseEntity<?> getCarById(@PathVariable Integer id){

        // Se busca la id solicitada. Si existe se devuelve la información del coche y la marca.
        // Si no devuelve mensaje de error.
        Car car = carService.getCarById(id);
        if (car != null){
            log.info("Car info loaded");
            CarDTOAndBrand carDTOAndBrand = CarDTOAndBrandMapper.INSTANCE.carToCarDTOAndBrand(car);
            return ResponseEntity.ok(carDTOAndBrand);
        }
        else {
            log.error("Id does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found");
        }
    }

    @PutMapping("update-car/{id}")
        public ResponseEntity<?> updateCarById(@PathVariable Integer id, @RequestBody CarDTO carDto){

        try {
            // Mapear carDTO a Car y llamada al método updateCarById
            Car car = CarDTOMapper.INSTANCE.carDTOToCar(carDto);
            Car carToUpdate = carService.updateCarById(id, car);

            // Mapear Car a CarDTO y devolver CarDTO actualizado
            CarDTOAndBrand carUpdated = CarDTOAndBrandMapper.INSTANCE.carToCarDTOAndBrand(carToUpdate);
            log.info("Car updated");
            return ResponseEntity.ok(carUpdated);

        } catch (IllegalArgumentException e) {  // Error en la id pasada
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        } catch (Exception e){
            log.error("Error while updating car");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DeleteMapping("delete-car/{id}")
    public ResponseEntity<?> deleteCarById(@PathVariable Integer id){

        try {
            carService.deleteCarById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Deleted Car with Id: " + id);

        } catch (IllegalArgumentException e) { // Error en la id pasada
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

        } catch (Exception e){
            log.error("Deleting car error");
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("get-all")
    public ResponseEntity<List<CarDTOAndBrand>> getAllCars(){

        // Mapea la lista con objetos Car a una lista con objetos carDTO y muestra su resultado.
        List<Car> cars = carService.getAllCars();
        List<CarDTOAndBrand> carDTOsAndBrand = cars.stream().map(CarDTOAndBrandMapper.INSTANCE::carToCarDTOAndBrand).toList();
        log.info("Rcovering all cars");
        return ResponseEntity.status(HttpStatus.OK).body(carDTOsAndBrand);
    }
}

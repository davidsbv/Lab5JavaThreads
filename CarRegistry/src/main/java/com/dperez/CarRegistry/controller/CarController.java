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
    public CompletableFuture<?> addCar(@RequestBody CarDTO carDTO){

        // Se ejecuta de manera asíncrónica la conversión de carDTO a Car (en hilo separado)
        return CompletableFuture.supplyAsync(() -> CarDTOMapper.INSTANCE.carDTOToCar(carDTO))

                // Toma el Car convertido y llama al carService.addCar(car). Devolverá un CompletableFuture<Car>
                .thenCompose(car -> carService.addCar(car))

                // Se transforma el Car añadido a CarDTOAndBrand
                .thenApply(CarDTOAndBrandMapper.INSTANCE::carToCarDTOAndBrand)

                // Se crea una respuesta ResponseEntity con el cuerpo de newCarDTOAndBran
                .thenApply(newCarDTOAndBran -> {
                    log.info("New Car added");
                    return ResponseEntity.ok(newCarDTOAndBran);
                })

                // Manejo de excepciones
                .exceptionally(throwable -> {
                    if(throwable.getCause() instanceof IllegalArgumentException){
                        log.error(throwable.getMessage(), throwable);
                        return ResponseEntity.status(HttpStatus.CONFLICT).build();
                    } else {
                        log.error("Error while adding new car", throwable);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                    }
                });
//        try {
//            // Se convierte carDTO a Car y se utiliza en la llmada al método addCar.
//            // Cuando se guarda se devuelve en newCarDTO  y se muestra la respuesta
//            Car car = CarDTOMapper.INSTANCE.carDTOToCar(carDTO);
//
//            CompletableFuture<Car> newCar = carService.addCar(car);
//            CarDTOAndBrand newCarDTOAndBrand = CarDTOAndBrandMapper.INSTANCE.carToCarDTOAndBrand(newCar.get());
//            log.info("New Car added");
//            return CompletableFuture.completedFuture(ResponseEntity.ok(newCarDTOAndBrand));
//
//        } catch (IllegalArgumentException e) {
//            // Error por Id ya existente.
//            log.error(e.getMessage());
//            return CompletableFuture.completedFuture(ResponseEntity
//                    .status(HttpStatus.CONFLICT).body(e.getMessage()));
//
//        } catch (Exception e){
//            log.error("Error while adding new car");
//            return CompletableFuture.completedFuture(ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR).build());
//        }
    }

    @PostMapping("add-bunch")
    public CompletableFuture<ResponseEntity<?>> addBunchCars(@RequestBody List<CarDTO> carDTOs){

        try {
            // Convierte la lista de CarDTO a Car y llama al método addCars del servicio
            // Después pasa cada objeto car del stream un objeto CarDTOAndBrand y lo devuelve
            List<Car> cars = carDTOs.stream().map(CarDTOMapper.INSTANCE::carDTOToCar).collect(Collectors.toList());
            return carService.addBunchCars(cars).thenApply(addedCars -> {
                List<CarDTOAndBrand> addedCarDTOsAndBrand = addedCars.stream()
                        .map(CarDTOAndBrandMapper.INSTANCE::carToCarDTOAndBrand).toList();

                log.info("Adding several cars");
                return ResponseEntity.ok(addedCarDTOsAndBrand);
            });
        }  catch (IllegalArgumentException e) {
            // Error en la id o marca
            log.error(e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()));

        } catch (Exception e){
            log.error("Error while adding new car");
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }

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
        public CompletableFuture<?> updateCarById(@PathVariable Integer id, @RequestBody CarDTO carDto){

//        return CompletableFuture.supplyAsync(() -> CarDTOMapper.INSTANCE.carDTOToCar(carDto))
//                .thenCompose(car -> carService.updateCarById(id, car))
//                .thenApply(CarDTOAndBrandMapper.INSTANCE::carToCarDTOAndBrand)
//                .thenApply(updatedCar -> {
//                    log.info("Car updated");
//                    return ResponseEntity.status(HttpStatus.OK).body(updatedCar);
//                })
//                .exceptionally(throwable -> {
//                    if(throwable instanceof IllegalArgumentException){
//                        log.error(throwable.getMessage());
//                        return ResponseEntity.status(HttpStatus.CONFLICT).build();
//                    } else {
//                        log.error(throwable.getMessage());
//                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//                    }
//                });

        try {
            // Mapear carDTO a Car y llamada al método updateCarById que devuelve un CompletableFuture<Car>
            Car car = CarDTOMapper.INSTANCE.carDTOToCar(carDto);
            CompletableFuture<Car> carToUpdate = carService.updateCarById(id, car);

            // Mapear Car a CarDTO y devolver CarDTO actualizado
            CarDTOAndBrand carUpdated = CarDTOAndBrandMapper.INSTANCE.carToCarDTOAndBrand(carToUpdate.get());
            log.info("Car updated");
            return CompletableFuture.completedFuture(ResponseEntity.ok(carUpdated));

        } catch (IllegalArgumentException e) {  // Error en la id pasada
            log.error(e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity
                    .status(HttpStatus.NOT_FOUND).body(e.getMessage()));

        } catch (Exception e){
            log.error("Error while updating car");
            return CompletableFuture.completedFuture(ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }

    }


    @PutMapping("update-bunch")
        public CompletableFuture<ResponseEntity<?>> updateBunch(@RequestBody List<CarDTO> carDTOs){

        try {
            // Mapeo de carDTOs a Car
            List<Car> cars = carDTOs.stream().map(CarDTOMapper.INSTANCE::carDTOToCar).toList();
            // Llamada al método para actualizar un grupo de coches
            // Cuando se completa la anterior instrucción, thenApply transforma car en carDTOAndBrand
            return carService.updateBunchCars(cars).thenApply(updatedCars -> {
                List<CarDTOAndBrand> updatedCarDTOAndBrand = updatedCars.stream()
                        .map(CarDTOAndBrandMapper.INSTANCE::carToCarDTOAndBrand).toList();

                // Se devuelven los CarDTOAndBrand Actualizados
                log.info("Updating several cars");
                return ResponseEntity.ok(updatedCarDTOAndBrand);
            });
        }catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()));

        } catch (Exception e){
            log.error("Error while updating car");
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
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
    public CompletableFuture<ResponseEntity<?>> getAllCars(){

        // Mapea la lista con objetos Car a una lista con objetos carDTOAndBrand y muestra su resultado.
        CompletableFuture<List<Car>> cars =  carService.getAllCars();
        return cars.thenApply(carRecovered -> {
            List<CarDTOAndBrand> carDTOsAndBrand = carRecovered.stream()
                .map(CarDTOAndBrandMapper.INSTANCE::carToCarDTOAndBrand).toList();

            log.info("Rcovering all cars");
            return ResponseEntity.ok(carDTOsAndBrand);
        });
    }
}

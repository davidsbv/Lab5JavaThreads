package com.dperez.CarRegistry.service.impl;

import com.dperez.CarRegistry.repository.BrandRepository;
import com.dperez.CarRegistry.repository.CarRepository;
import com.dperez.CarRegistry.repository.entity.BrandEntity;
import com.dperez.CarRegistry.repository.entity.CarEntity;
import com.dperez.CarRegistry.repository.mapper.BrandEntityMapper;
import com.dperez.CarRegistry.repository.mapper.CarEntityMapper;
import com.dperez.CarRegistry.service.CarService;
import com.dperez.CarRegistry.service.model.Brand;
import com.dperez.CarRegistry.service.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
 public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BrandRepository brandRepository;


    @Override
    public Car addCar(Car car) throws IllegalArgumentException {

        // Verifica si la Id ya existe. Lanza una excepción en caso afirmativo.
        if(car.getId() != null && carRepository.existsById(car.getId())){

            throw new IllegalArgumentException("The Id already" + car.getId() +" exists");
        }

        // Verificar si la marca existe
        Optional<BrandEntity> brandEntityOptional = brandRepository.findByNameIgnoreCase(car.getBrand().getName());

        if(brandEntityOptional.isEmpty()){
            throw new IllegalArgumentException("Brand " + car.getBrand().getName() + " does not exist");
        }

        // Obtener la BrandEntity existente y pasar a Brand
        BrandEntity brandEntity = brandEntityOptional.get();
        Brand brand = BrandEntityMapper.INSTANCE.brandEntityToBrand(brandEntity);

        // Asociar la BrandEntitiy existente al car
        car.setBrand(brand);

        // Se pasa car a carEntity para guardar
        CarEntity carEntity = CarEntityMapper.INSTANCE.carToCarEntity(car);
       // carEntity.setBrand((brand)); // Se asocia la brand existente

        // Se guarda la CarEntity en la base de datos
        CarEntity savedCarEntity = carRepository.save(carEntity);

        // Se devuelve el coche guardado como modelo de dominio
        return CarEntityMapper.INSTANCE.carEntityToCar(savedCarEntity);
    }

    @Async("taskExecutor")
    @Override
    public CompletableFuture<List<Car>> addCars(List<Car> cars) throws IllegalArgumentException {

        // Verificar si la id existe y si la marca está en la base de datos
        List<Car> addedCars = cars.stream().map(car -> {
            if ((car.getId() != null) && carRepository.existsById(car.getId())){
               throw new IllegalArgumentException("The Id " + car.getId() + " already exists");
            }

            Optional<BrandEntity> brandEntityOptional = brandRepository.findByNameIgnoreCase(car.getBrand().getName());

            if (brandEntityOptional.isEmpty()){
                throw new IllegalArgumentException("Brand " + car.getBrand().getName() + " does not exist");
            }

            // Toma la BrandEntity, se le asigna a cada CarEntity que se ha transformado previamente de Car
            BrandEntity brandEntity = brandEntityOptional.get();
            CarEntity carEntity = CarEntityMapper.INSTANCE.carToCarEntity(car);
            carEntity.setBrand(brandEntity);

            // Se guarda el CarEntity con los datos validados, en savedCar para retornarlos al stream como Car
            CarEntity savedCarEntity = carRepository.save(carEntity);

            return CarEntityMapper.INSTANCE.carEntityToCar(savedCarEntity);
        }).toList();

        // Se devulven los coches guardados
        return CompletableFuture.completedFuture(addedCars);
    }


    @Override
    public Car getCarById(Integer id) {

        // Búsqueda de car por id
        Optional<CarEntity> carEntityOptional = carRepository.findById(id);

        log.info("Searching id: " + id);
        // Si se encuentra devuelve el objeto car. En caso contrario devuelve null.
        return carEntityOptional.map(CarEntityMapper.INSTANCE::carEntityToCar).orElse(null);
    }


    @Override
    public Car updateCarById(Integer id, Car car) throws IllegalArgumentException {

        // Verifica si la Marca del objeto Car existe
        Optional<BrandEntity> brandEntityOptional = brandRepository.findByNameIgnoreCase(car.getBrand().getName());

        if (brandEntityOptional.isEmpty()){
            log.error("Unknown id");
            throw new IllegalArgumentException("Brand with name " + car.getBrand() + " does not exist.");
        }

        // Verifica si la Id existe. Lanza excepción en caso negativo. En caso afirmativo actualiza los datos
        if(id == null || !carRepository.existsById(id)){
            log.error("Unknown id");
           throw new IllegalArgumentException("Id " + id + " does not exist.");
        }
        else {
            // Se obtiene la BrandEntity existente y se asocia a la carEntity a actualizar
            BrandEntity brandEntity = brandEntityOptional.get();
            log.info("Marca " + brandEntity.toString() + " encontrada");
            CarEntity carEntity = CarEntityMapper.INSTANCE.carToCarEntity(car);

            // Seteo de la id y la marca
            carEntity.setId(id);
            carEntity.setBrand(brandEntity);

            // Actualiza los datos y devuelve el objeto actualizado.
            CarEntity updatedCarEntity = carRepository.save(carEntity);
            return CarEntityMapper.INSTANCE.carEntityToCar(updatedCarEntity);
        }

    }


    @Override
    public CompletableFuture<List<Car>> updateCars(List<Car> cars) {
        return null;
    }


    @Override
    public void deleteCarById(Integer id) throws IllegalArgumentException {

        // Si la id existe borra el coche. En caso contrario lanza error.
        if(id != null && carRepository.existsById(id)){
            carRepository.deleteById(id);
        }
        else {
            throw new IllegalArgumentException("Car not found with Id: " + id);
        }
    }


    @Override
    public List<Car> getAllCars() {

        // Se obtienen en una lista todos los objetos de tipo CarEntity y se mapean a tipo Car
        return carRepository.findAll().stream().map(CarEntityMapper.INSTANCE::carEntityToCar)
                .collect(Collectors.toList());
    }
}

package com.dperez.CarRegistry.controller.mapper;

import com.dperez.CarRegistry.controller.dtos.CarDTO;
import com.dperez.CarRegistry.controller.dtos.CarDTO.CarDTOBuilder;
import com.dperez.CarRegistry.service.model.Car;
import com.dperez.CarRegistry.service.model.Car.CarBuilder;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-20T11:46:34+0200",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
public class CarDTOMapperImpl implements CarDTOMapper {

    @Override
    public CarDTO carToCarDTO(Car car) {
        if ( car == null ) {
            return null;
        }

        CarDTOBuilder carDTO = CarDTO.builder();

        carDTO.id( car.getId() );
        carDTO.brand( car.getBrand() );
        carDTO.model( car.getModel() );
        carDTO.mileage( car.getMileage() );
        carDTO.price( car.getPrice() );
        carDTO.year( car.getYear() );
        carDTO.description( car.getDescription() );
        carDTO.colour( car.getColour() );
        carDTO.fuelType( car.getFuelType() );
        carDTO.numDoors( car.getNumDoors() );

        return carDTO.build();
    }

    @Override
    public Car carDTOToCar(CarDTO carDTO) {
        if ( carDTO == null ) {
            return null;
        }

        CarBuilder car = Car.builder();

        car.id( carDTO.getId() );
        car.brand( carDTO.getBrand() );
        car.model( carDTO.getModel() );
        car.mileage( carDTO.getMileage() );
        car.price( carDTO.getPrice() );
        car.year( carDTO.getYear() );
        car.description( carDTO.getDescription() );
        car.colour( carDTO.getColour() );
        car.fuelType( carDTO.getFuelType() );
        car.numDoors( carDTO.getNumDoors() );

        return car.build();
    }
}

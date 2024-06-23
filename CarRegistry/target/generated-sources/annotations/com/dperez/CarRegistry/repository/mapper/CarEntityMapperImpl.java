package com.dperez.CarRegistry.repository.mapper;

import com.dperez.CarRegistry.repository.entity.BrandEntity;
import com.dperez.CarRegistry.repository.entity.CarEntity;
import com.dperez.CarRegistry.service.model.Car;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-20T10:19:47+0200",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
public class CarEntityMapperImpl implements CarEntityMapper {

    @Override
    public Car carEntityToCar(CarEntity carEntity) {
        if ( carEntity == null ) {
            return null;
        }

        Car car = new Car();

        car.setBrand( carEntityBrandName( carEntity ) );
        car.setId( carEntity.getId() );
        car.setModel( carEntity.getModel() );
        car.setMileage( carEntity.getMileage() );
        car.setPrice( carEntity.getPrice() );
        car.setYear( carEntity.getYear() );
        car.setDescription( carEntity.getDescription() );
        car.setColour( carEntity.getColour() );
        car.setFuelType( carEntity.getFuelType() );
        car.setNumDoors( carEntity.getNumDoors() );

        return car;
    }

    @Override
    public CarEntity carToCarEntity(Car car) {
        if ( car == null ) {
            return null;
        }

        CarEntity carEntity = new CarEntity();

        carEntity.setBrand( carToBrandEntity( car ) );
        carEntity.setId( car.getId() );
        carEntity.setModel( car.getModel() );
        carEntity.setMileage( car.getMileage() );
        carEntity.setPrice( car.getPrice() );
        carEntity.setYear( car.getYear() );
        carEntity.setDescription( car.getDescription() );
        carEntity.setColour( car.getColour() );
        carEntity.setFuelType( car.getFuelType() );
        carEntity.setNumDoors( car.getNumDoors() );

        return carEntity;
    }

    private String carEntityBrandName(CarEntity carEntity) {
        if ( carEntity == null ) {
            return null;
        }
        BrandEntity brand = carEntity.getBrand();
        if ( brand == null ) {
            return null;
        }
        String name = brand.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    protected BrandEntity carToBrandEntity(Car car) {
        if ( car == null ) {
            return null;
        }

        BrandEntity brandEntity = new BrandEntity();

        brandEntity.setName( car.getBrand() );

        return brandEntity;
    }
}

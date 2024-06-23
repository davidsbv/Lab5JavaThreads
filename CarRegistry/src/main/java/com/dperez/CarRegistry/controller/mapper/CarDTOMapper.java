package com.dperez.CarRegistry.controller.mapper;

import com.dperez.CarRegistry.controller.dtos.CarDTO;
import com.dperez.CarRegistry.service.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarDTOMapper {

    CarDTOMapper INSTANCE = Mappers.getMapper(CarDTOMapper.class);

    CarDTO carToCarDTO(Car car);
    Car carDTOToCar(CarDTO carDTO);
}

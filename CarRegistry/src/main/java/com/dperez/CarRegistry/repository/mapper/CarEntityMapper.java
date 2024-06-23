package com.dperez.CarRegistry.repository.mapper;

import com.dperez.CarRegistry.repository.entity.CarEntity;
import com.dperez.CarRegistry.service.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CarEntityMapper {

    CarEntityMapper INSTANCE = Mappers.getMapper(CarEntityMapper.class);

    @Mapping(source = "brand.name", target = "brand")
    Car carEntityToCar(CarEntity carEntity);
    @Mapping(source = "brand", target = "brand.name")
    CarEntity carToCarEntity(Car car);
}

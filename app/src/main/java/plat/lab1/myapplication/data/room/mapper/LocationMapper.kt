package plat.lab1.myapplication.data.room.mapper

import plat.lab1.myapplication.data.Location
import plat.lab1.myapplication.data.room.entity.LocationEntity

fun LocationEntity.toModel(): Location {
    return Location(
        id = id,
        name = name,
        type = type,
        dimension = dimension
    )
}

fun Location.toEntity(): LocationEntity {
    return LocationEntity(
        id = id,
        name = name,
        type = type,
        dimension = dimension
    )
}
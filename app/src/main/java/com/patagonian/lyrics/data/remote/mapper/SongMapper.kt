package com.patagonian.lyrics.data.remote.mapper

import com.patagonian.lyrics.data.remote.dto.Song as Dto
import com.patagonian.lyrics.domain.entity.Song
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.mapstruct.factory.Mappers

@Mapper
interface SongMapper {

    companion object {
        var INSTANCE: SongMapper = Mappers.getMapper(SongMapper::class.java)
    }

    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(target = "title", constant = ""),
        Mapping(target = "author", constant = ""),
        Mapping(target = "searchAt", expression = "java(new java.util.Date())")
    )
    fun toSong(dto: Dto): Song
}
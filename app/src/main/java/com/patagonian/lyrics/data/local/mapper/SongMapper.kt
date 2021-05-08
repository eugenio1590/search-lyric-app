package com.patagonian.lyrics.data.local.mapper

import com.patagonian.lyrics.data.local.entity.Song as Entity
import com.patagonian.lyrics.domain.entity.Song
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers


@Mapper
interface SongMapper {

    companion object {
        var INSTANCE: SongMapper = Mappers.getMapper(SongMapper::class.java)
    }

    fun toEntity(song: Song): Entity

    fun toSong(entity: Entity): Song
}
package io.ramani.ramaniStationary.app.common.di.pagination

import io.ramani.ramaniStationary.data.common.source.remote.mappers.PaginationMetaRemoteModelMapper
import io.ramani.ramaniStationary.data.entities.PaginationMetaRemote
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.entities.PaginationMeta
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

val paginationModule = Kodein.Module("paginationModule") {
    bind<ModelMapper<PaginationMetaRemote, PaginationMeta>>() with provider {
        PaginationMetaRemoteModelMapper()
    }
}
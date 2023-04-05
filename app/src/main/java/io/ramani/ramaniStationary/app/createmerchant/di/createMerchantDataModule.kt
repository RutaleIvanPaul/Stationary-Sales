package io.ramani.ramaniStationary.app.createmerchant.di

import io.ramani.ramaniStationary.data.common.network.ServiceHelper
import io.ramani.ramaniStationary.data.createmerchant.CreateMerchantApi
import io.ramani.ramaniStationary.data.createmerchant.CreateMerchantLocalDataSource
import io.ramani.ramaniStationary.data.createmerchant.CreateMerchantRemoteDataSource
import io.ramani.ramaniStationary.data.createmerchant.CreateMerchantRepository
import io.ramani.ramaniStationary.data.createmerchant.mappers.NameValueRemoteMapper
import io.ramani.ramaniStationary.data.createmerchant.mappers.TopPerformersRemoteMapper
import io.ramani.ramaniStationary.data.createmerchant.models.response.NameValueRemoteModel
import io.ramani.ramaniStationary.data.createmerchant.models.response.TopPerformersRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.createmerchant.CreateMerchantDataSource
import io.ramani.ramaniStationary.domain.createmerchant.model.NameValueModel
import io.ramani.ramaniStationary.domain.createmerchant.model.TopPerformersModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val createMerchantDataModule = Kodein.Module("createMerchantDataModule") {

    bind<CreateMerchantApi>() with provider {
        ServiceHelper.createService<CreateMerchantApi>(instance())
    }

    bind<CreateMerchantDataSource>("createMerchantDataSource") with singleton {
        CreateMerchantRepository(
            instance("remoteCreateMerchantDataSource"),
            instance("localCreateMerchantDataSource")
        )
    }

    bind<CreateMerchantDataSource>("remoteCreateMerchantDataSource") with singleton {
        CreateMerchantRemoteDataSource(
            instance(),
            instance(),
            instance()
        )
    }

    bind<CreateMerchantDataSource>("localCreateMerchantDataSource") with singleton {
        CreateMerchantLocalDataSource(
            instance(),
            instance()
        )
    }

    // data mapper
    bind<ModelMapper<TopPerformersRemoteModel, TopPerformersModel>>() with provider {
        TopPerformersRemoteMapper(
            instance()
        )
    }

    bind<ModelMapper<NameValueRemoteModel, NameValueModel>>() with provider {
        NameValueRemoteMapper()
    }

}
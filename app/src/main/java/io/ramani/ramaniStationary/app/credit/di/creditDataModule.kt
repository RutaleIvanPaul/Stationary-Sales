package io.ramani.ramaniStationary.app.credit.di

import io.ramani.ramaniStationary.data.common.network.ServiceHelper
import io.ramani.ramaniStationary.data.createmerchant.models.request.MerchantRouteModel
import io.ramani.ramaniStationary.data.createmerchant.models.request.MetaDataItem
import io.ramani.ramaniStationary.data.credit.CreditApi
import io.ramani.ramaniStationary.data.credit.CreditLocalDataSource
import io.ramani.ramaniStationary.data.credit.CreditRemoteDataSource
import io.ramani.ramaniStationary.data.credit.CreditRepository
import io.ramani.ramaniStationary.data.credit.mappers.LocationRemoteMapper
import io.ramani.ramaniStationary.data.credit.mappers.MerchantRouteRemoteMapper
import io.ramani.ramaniStationary.data.credit.mappers.MetaDataItemRemoteMapper
import io.ramani.ramaniStationary.data.credit.models.response.LocationRemoteModel
import io.ramani.ramaniStationary.data.credit.models.response.MerchantRouteRemoteModel
import io.ramani.ramaniStationary.data.credit.models.response.MetaDataItemRemoteModel
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.credit.CreditDataSource
import io.ramani.ramaniStationary.domain.credit.model.LocationModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val creditDataModule = Kodein.Module("creditDataModule") {

    bind<CreditApi>() with provider {
        ServiceHelper.createService<CreditApi>(instance())
    }

    bind<CreditDataSource>("creditDataSource") with singleton {
        CreditRepository(
            instance("remoteCreditDataSource"),
            instance("localCreditDataSource")
        )
    }

    bind<CreditDataSource>("remoteCreditDataSource") with singleton {
        CreditRemoteDataSource(
            instance(),
            instance(),
            instance(),
        )
    }

    bind<CreditDataSource>("localCreditDataSource") with singleton {
        CreditLocalDataSource(
            instance(),
            instance()
        )
    }

    // Location Remote mapper
    bind<ModelMapper<LocationRemoteModel, LocationModel>>() with provider {
        LocationRemoteMapper(
            instance(),
            instance()
        )
    }

    bind<ModelMapper<MerchantRouteRemoteModel, MerchantRouteModel>>() with provider {
        MerchantRouteRemoteMapper()
    }

    bind<ModelMapper<MetaDataItemRemoteModel, MetaDataItem>>() with provider {
        MetaDataItemRemoteMapper()
    }
}
package io.ramani.ramaniStationary.app.home.di

import io.ramani.ramaniStationary.data.common.network.ServiceHelper
import io.ramani.ramaniStationary.data.home.HomeApi
import io.ramani.ramaniStationary.data.home.HomeLocalDataSource
import io.ramani.ramaniStationary.data.home.HomeRemoteDataSource
import io.ramani.ramaniStationary.data.home.HomeRepository
import io.ramani.ramaniStationary.data.home.mappers.*
import io.ramani.ramaniStationary.data.home.models.response.*
import io.ramani.ramaniStationary.domain.base.mappers.ModelMapper
import io.ramani.ramaniStationary.domain.home.HomeDataSource
import io.ramani.ramaniStationary.domain.home.model.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val homeDataModule = Kodein.Module("homeDataModule") {

    bind<HomeApi>() with provider {
        ServiceHelper.createService<HomeApi>(instance())
    }

    bind<HomeDataSource>("homeDataSource") with singleton {
        HomeRepository(
            instance("remoteHomeDataSource"),
            instance("localHomeDataSource")
        )
    }

    bind<HomeDataSource>("remoteHomeDataSource") with singleton {
        HomeRemoteDataSource(
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }

    bind<HomeDataSource>("localHomeDataSource") with singleton {
        HomeLocalDataSource(
            instance(),
            instance()
        )
    }

    // Daily Sales
    bind<ModelMapper<DailySalesStatsRemoteModel, DailySalesStatsModel>>() with provider {
        DailySalesStatsRemoteMapper()
    }

    // Tax
    bind<ModelMapper<TaxRemoteModel, TaxModel>>() with provider {
        TaxRemoteMapper()
    }

    // Product
    bind<ModelMapper<ProductRemoteModel, ProductModel>>() with provider {
        ProductRemoteMapper(
            instance()
        )
    }

    bind<ModelMapper<ProductCategoryRemoteModel, ProductCategoryModel>>() with provider {
        ProductCategoryRemoteMapper()
    }

    // Merchant
    bind<ModelMapper<MerchantRemoteModel, MerchantModel>>() with provider {
        MerchantRemoteMapper(
            instance()
        )
    }

    bind<ModelMapper<MerchantMemberRemoteModel, MerchantMemberModel>>() with provider {
        MerchantMemberRemoteMapper()
    }

    // Get tax information per user id
    bind<ModelMapper<TaxInformationRemoteModel, TaxInformationModel>>() with provider {
        TaxInformationRemoteMapper()
    }

    // Get user details
    bind<ModelMapper<UserAccountDetailsRemoteModel, UserAccountDetailsModel>>() with provider {
        UserAccountDetailsRemoteMapper(
            instance()
        )
    }

    bind<ModelMapper<UserDiscountRemoteModel, UserDiscountModel>>() with provider {
        UserDiscountRemoteMapper()
    }

}
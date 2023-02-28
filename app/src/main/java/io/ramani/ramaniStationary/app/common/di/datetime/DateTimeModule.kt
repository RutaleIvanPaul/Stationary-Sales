package io.ramani.ramaniStationary.app.common.di.datetime


import io.ramani.ramaniStationary.domain.datetime.DateFormatter
import io.ramani.ramaniStationary.domain.datetime.DateTimeManager
import io.ramani.ramaniStationary.domain.datetime.DefaultDateTimeParser
import io.ramani.ramaniStationary.domain.datetime.DefaultFromServerDateTimeParser
import io.ramani.ramaniStationary.domainCore.datetime.DateTimeParser
import io.ramani.ramaniStationary.domainCore.datetime.FromServerDateTimeParser
import io.ramani.ramaniStationary.domainCore.datetime.IDateTimeManager
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

/**
 * Created by Raed on 9/22/17.
 */
val dateTimeModule = Kodein.Module("dateTimeModule") {
    bind<DateTimeParser>() with provider {
        DefaultDateTimeParser(instance())
    }

    bind<FromServerDateTimeParser>() with provider {
        DefaultFromServerDateTimeParser()
    }

    bind<DateFormatter>() with provider {
        DateFormatter(instance())
    }

    bind<IDateTimeManager>() with singleton {
        DateTimeManager()
    }
}
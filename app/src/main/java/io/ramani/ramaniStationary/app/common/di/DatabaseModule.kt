package io.ramani.ramaniStationary.app.common.di

import androidx.room.Room
import io.ramani.ramaniStationary.data.database.DATABASE_NAME
import io.ramani.ramaniStationary.data.database.RamaniDatabase
import org.kodein.di.Kodein
import org.kodein.di.generic.*

/**
 * Created by Adrian on 3/14/2023.
 */
val databaseModule = Kodein.Module("databaseModule") {

    bind<RamaniDatabase>() with singleton {
        Room.databaseBuilder(instance(), RamaniDatabase::class.java, DATABASE_NAME).allowMainThreadQueries().build()
    }

}
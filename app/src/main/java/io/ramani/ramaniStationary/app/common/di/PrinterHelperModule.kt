package io.ramani.ramaniStationary.app.common.di

import io.ramani.ramaniStationary.domainCore.printer.PrinterHelper
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val printerHelperModule = Kodein.Module("printerHelperModule"){
    bind<PrinterHelper>() with singleton {
        PrinterHelper(instance())
    }

//    bind<ThermalPrinter>() with singleton {
//        ThermalPrinter(instance("bluetoothOutputStream"))
//    }
//
//    bind<OutputStream>("bluetoothOutputStream") with singleton {
//        BluetoothConnection.instanceBluetoothSocket!!.outputStream
//    }
}
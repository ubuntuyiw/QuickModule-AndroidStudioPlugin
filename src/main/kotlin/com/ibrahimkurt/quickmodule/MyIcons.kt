package com.ibrahimkurt.quickmodule

import com.intellij.openapi.util.IconLoader

object MyIcons {
    @JvmField val ApplicationModule = IconLoader.getIcon("/icons/applicationModule.svg", javaClass)
    @JvmField val AndroidModule = IconLoader.getIcon("/icons/androidModule.svg", javaClass)
    @JvmField val KotlinModule = IconLoader.getIcon("/icons/kotlinModule.svg", javaClass)
    @JvmField val JavaModule = IconLoader.getIcon("/icons/javaModule.svg", javaClass)
    @JvmField val BenchmarkModule = IconLoader.getIcon("/icons/benchmarkModule.svg", javaClass)
    @JvmField val OtherModules = IconLoader.getIcon("/icons/otherModules.svg", javaClass)
}
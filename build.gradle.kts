plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

group = "net.labymod"
version = providers.environmentVariable("VERSION").getOrElse("1.0.0")

labyMod {
    defaultPackageName = "net.labymod.addons.skinlayer3d"

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                getByName("client") {
                     devLogin = true
                }
            }
        }
    }

    addonInfo {
        namespace = "skinlayer3d"
        displayName = "SkinLayer 3D"
        author = "LabyMedia GmbH"
        minecraftVersion = "1.8.9<1.21.10"
        version = rootProject.version.toString()
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version
}
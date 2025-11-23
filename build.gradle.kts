plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

group = "cc.mohamed"
version = providers.environmentVariable("VERSION").getOrElse("1.0.0")

labyMod {
    defaultPackageName = "cc.mohamed.skinlayer3d"

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
        author = "mxha39"
        description = "Example Description"
        minecraftVersion = "1.8.9,1.12.2,1.16.5,1.17.1,1.19.4,1.20.1,1.20.6,1.21.1,1.21.4,1.21.8,1.21.10"
        version = rootProject.version.toString()
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version
}
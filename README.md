CustomGamerules API  
[![Maven Repository](https://img.shields.io/maven-metadata/v/https/maven.misterpemodder.com/list/libs-release/com/misterpemodder/customgamerules/custom-gamerules/maven-metadata.xml.svg)](https://maven.misterpemodder.com/libs-release/com/misterpemodder/customgamerules/custom-gamerules)
=========================

A modding API that provides a way to register custom gamerules
and adds a gamerules menu.

## Usage

To compile against the API, add the following to your `build.gradle`:  
(replace `[VERSION]` with the API version)

```groovy
repositories {
    maven {url "https://maven.misterpemodder.com/libs-release/"}
}

dependencies {
    modCompile "com.misterpemodder.customgamerules:custom-gamerules:[VERSION]"
}
```

This mod requires Fabric (https://minecraft.curseforge.com/projects/fabric).

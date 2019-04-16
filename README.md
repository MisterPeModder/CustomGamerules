CustomGamerules API  
[![Maven Repository](https://img.shields.io/maven-metadata/v/https/maven.misterpemodder.com/libs-release/com/misterpemodder/custom-gamerules/maven-metadata.xml.svg)](https://maven.misterpemodder.com/libs-release/com/misterpemodder/custom-gamerules)
[![](http://cf.way2muchnoise.eu/full_314538_downloads.svg)](https://minecraft.curseforge.com/projects/customgamerules-api)
[![](http://cf.way2muchnoise.eu/versions/For%20MC_314538_all.svg)](https://minecraft.curseforge.com/projects/customgamerules-api)
========================= 

A modding API that provides a way to register custom gamerules
and adds a gamerules menu.

## Features
- Faster rule value access
- A game rule menu
- Default game rules, used at world creation
- Namespaced game rules
- and more... 

## Maven

```groovy
repositories {
    maven {url "https://maven.misterpemodder.com/libs-release/"}
}

dependencies {
    modCompile "com.misterpemodder.customgamerules:custom-gamerules:<VERSION>"
}
```

## API
#### Better GameRule access

Instances of `GameRules` can be converted to `CustomGameRules` using `CustomGameRules.of`  
The `CustomGameRules` class provides access to extended game rules and access to rule values directly without needing to parse the value using `CustomGameRules.getValue`.  
Also, `CustomGameRules.get` returns an instance of `GameRuleValue`, its value is updated dynamically, with no need to use `GameRules.get` each time you want to get a value.

#### Registering game rules

Builtin rule types:
- Boolean: `GameRuleTypes.BOOLEAN`
- String: `GameRuleTypes.STRING`
- Integer: `GameRuleTypes.INTEGER`
- Long: `GameRuleTypes.LONG`
- Float: `GameRuleTypes.FLOAT`
- Double: `GameRuleTypes.DOUBLE`

You can add you own rule type by extending `GameRuleType` and registering it with `GameRuleRegistry.registerType`

To register a custom game rule, use `GameRuleRegistry.register` 
and you can supply a `ValueUpdateHandler` and/or a `ValueValidator` to have better control over your rule value.

Example of a custom game rule:
```java
GameRuleRegistry.INSTANCE.register("my_mod_id", "myGameRule", GameRuleTypes.BOOLEAN, true);
```

#### Menu API
The menu can be opened with `GameRulesMenu.INSTANCE.open`

Custom Rule widgets:  
(WIP)

This mod requires Fabric (https://minecraft.curseforge.com/projects/fabric).

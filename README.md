> DEPENDENCIES: [WorldGuard](https://dev.bukkit.org/projects/worldguard)

### › Features

- Show or hide global region
- Shows if this is your region _(owner or member)_
- Shows whether this region is occupied _(if you are not the owner or member)_
- Blacklist of regions
- Renaming regions _(for example the spawn region will be output as &6SPAWN)_
- Full support for WorldGuard

---

### › Review

[![](https://i.ytimg.com/vi/dmJR8MlWudw/maxresdefault.jpg)](https://www.youtube.com/watch?v=dmJR8MlWudw)

### › Commands and Permissions

| Command                                            | Description                                         | Permission                       |
|----------------------------------------------------|-----------------------------------------------------|----------------------------------|
| lightshowregion / lsr                              | Help by commands                                    | lightshowregion.help             |
| lightshowregion toggle (\<actionbar>/\<bossbar>)   | Switch the region display                           | lightshowregion.toggle           |
| lightshowregion add \<region> \<custom name>       | Add a custom name to a region                       | lightshowregion.add              |
| lightshowregion remove \<region>                   | Remove custom name                                  | lightshowregion.remove           |
| lightshowregion blacklist add \<region>            | Add a region to the blacklist                       | lightshowregion.blacklist.add    |
| lightshowregion blacklist remove \<region>         | Remove a region to the blacklist                    | lightshowregion.blacklist.remove |
| lightshowregion global                             | Switch the global region display                    | lightshowregion.global           |
| lightshowregion reload actionbar                   | Reload actionbar for all online players             | lightshowregion.reload.actionbar |
| lightshowregion reload bossbar                     | Reload bossbar for all online players               | lightshowregion.reload.bossbar   |
| lightshowregion reload bars                        | Reload actionbar and bossbar for all online players | lightshowregion.reload.bars      |
| lightshowregion reload config                      | Reload all configurations                           | lightshowregion.reload.config    |

| Permissions without commands | Description                                      |
|------------------------------|--------------------------------------------------|
| lightshowregion.blacklist.*  | All rights to manage the blacklist               |
| lightshowregion.reload.*     | Access for reload actionbar, bossbar and configs |
| lightshowregion.*            | Full access to the plugin                        |

| Placeholder              | Description                                                       |
|--------------------------|-------------------------------------------------------------------|
| %lightshowregion_custom% | Displays information about free, <br/> occupied or your territory |

---

### › [API](https://github.com/kainlighty/LightShowRegion/tree/master/API/src/main/java/ru/kainlight/lightshowregion/API)

#### Maven:
```
<dependency>
  <groupId>ru.kainlight.lightshowregion</groupId>
  <artifactId>api</artifactId>
  <version>1.4.2</version>
  <scope>provided</scope>
</dependency>

> $ mvn install
```

#### Gradle — Groovy DSL:
```groovy
repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = "https://maven.pkg.github.com/kainlighty/LightShowRegion"
    }
}

dependencies {
    compileOnly 'ru.kainlight.lightshowregion:api:1.4.2'
}
```
#### Gradle — Kotlin DSL:
```kotlin
repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/kainlighty/LightShowRegion")
    }
}

dependencies {
    compileOnly("ru.kainlight.lightshowregion:api:1.4.2")
}
```

---

#### Methods:

> Get provider: `LightShowRegionAPI.getProvider()`

| API                                     | Description                                    |
|-----------------------------------------|------------------------------------------------|
| createShowedPlayer(Player)              | Create a player to display the region          |
| getOrCreateShowedPlayer(Player)         | Get or create a player to display the region   |
| deleteShowedPlayer(Player/ShowedPlayer) | Delete a showed player                         | 
| getShowedPlayer(Player)                 | Get the player to whom the region is displayed |
| getShowedPlayers()                      | Get list of the players                        |
| isShowedPlayer(Player)                  | Check if the player is a ShowedPlayer          |
| reloadActionbar(Player)                 | Reload the actionbar to the player             |
| reloadActionbars()                      | Reload the actionbar for all players           |
| unloadActionbars()                      | Unload the actionbar for all players           |
| reloadBossbar(Player)                   | Reload the bossbar to the player               |
| reloadBossbars()                        | Reload the bossbar for all players             |
| unloadBossbars()                        | Unload the bossbar for all players             |
| addDisabledWorld(name)                  | Add a region to the blacklist                  |
| getDisabledWorlds()                     | Get a list of disabled worlds                  |
| removeDisabledWorld(name)               | Remove a region to the blacklist               |
| getRegionHandler()                      | Get players who are being checked              |

| RegionHandler                         | Description                                        |
|---------------------------------------|----------------------------------------------------|
| addCustomRegion(regionId, regionName) | Add a custom region                                |
| getCustomRegionName(Player)           | Get a custom region where the player is located    |
| removeCustomRegion(regionId)          | Delete a custom region                             |
| getCustomRegionIds()                  | Get the custom region IDs                          |
| isCustomRegion(id)                    | Check if the region is custom                      |
| addBlacklist(regionId)                | Add a region to the blacklist                      |
| getBlacklist()                        | Get the blacklist                                  |
| removeBlacklist(regionId)             | Remove a region to the blacklist                   |
| isGlobalRegion()                      | Check whether the global region display is enabled |
| setGlobalRegion ()                    | Set the global region display                      |

| ShowedPlayer   | Description                                                     |
|----------------|-----------------------------------------------------------------|
| getPlayer()    | Get a bukkit player                                             |
| getActionbar() | Get the parameters for the actionbar                            |
| getBossbar()   | Get the parameters for the bossbar                              |
| toggleAll()    | Switch the region display in the actionbar and bossbar for all  |
| showAll()      | Show actionbar and bossbar                                      |
| hideAll()      | Hide actionbar and bossbar                                      |

| Actionbar \| Bossbar | Description                              |
|----------------------|------------------------------------------|
| toggle()             | Toggle the display                       |
| show()               | Show the display                         |
| hide()               | Hide the display                         |
| isActive()           | Check if the display is active           |
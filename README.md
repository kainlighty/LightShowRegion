# [LightShowRegion](https://www.spigotmc.org/resources/105689/)

# DEPENDENCIES: [PLACEHOLDERAPI](https://github.com/PlaceholderAPI/PlaceholderAPI/releases)

## › Features

- #### Show or hide global region
- #### Shows if this is your region _(owner or member)_
- #### Shows whether this region is occupied _(if you are not the owner or member)_
- #### Blacklist of regions
- #### Renaming regions _(for example the spawn region will be output as &6SPAWN)_

## › REVIEW: https://www.youtube.com/watch?v=dmJR8MlWudw

| Command                          | Description                             | Permission                       |
|----------------------------------|-----------------------------------------|----------------------------------|
| lsr                              | Help by commands                        | lightshowregion.help             |
| lsr toggle                       | enable or disable                       | lightshowregion.toggle           |
| lsr add \<region> \<custom name> | Add a custom name to a region           | lightshowregion.add              |
| lsr remove \<region>             | Remove custom name                      | lightshowregion.remove           |
| lsr blacklist add \<region>      | Add a region to the blacklist           | lightshowregion.blacklist.add    |
| lsr blacklist remove \<region>   | Remove a region to the blacklist        | lightshowregion.blacklist.remove |
| lsr global                       | Hide actionbar in _global_ region       | lightshowregion.global           |
| lsr reload bar                   | Reload actionbar for all online players | lightshowregion.reload.bar       |
| lsr reload config                | Reload all configurations               | lightshowregion.reload.config    |

| Permissions without commands | Description                        |
|------------------------------|------------------------------------|
| lightshowregion.blacklist.*  | All rights to manage the blacklist |
| lightshowregion.*            | Full access to the plugin          |

| Placeholder              | Description                                                       |
|--------------------------|-------------------------------------------------------------------|
| %lightshowregion_custom% | Displays information about free, <br/> occupied or your territory |

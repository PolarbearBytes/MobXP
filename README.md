
# Mob XP

Change the amount of xp any mob drops when being killed. Includes being able to change the amount of xp the Ender Dragon drops on the first kill and every kill after that.




## Configuration

### config/mobxp.json

| Parameter             | Type       | Description                                                                 | Default          |
| :-------------------- | :--------- | :-------------------------------------------------------------------------- |:-----------------|
| `dragonXP`        | Integer     | Amount of xp dropped by the Ender Dragon after the first kill                      | 500 |
| `firstDragonXP` | Integer      | Amount of xp dropped by the Ender Dragon on first kill | 12000             |
| `xp`   | List    | List of xp and options for each mob             |                 |
|  ⤷ Identifier  | String    | The identifer string for the mob, best not to change this value             |                 |
|      ⤷ enabled  | Boolean    | Wither or not to use the custom value, true to use false to ignore        |  false  |
|      ⤷ experiencePoints  | Integer    | Amount of xp for the mob to drop        |  Minecraft's default  |
|      ⤷ random | Boolean    | Wither or not to randomize the amount of xp dropped from 1 to experiencePoints       |  false  |
# Fourteen Games Platform

## Useful Information for Developers
### HTTP Endpoints
#### Platform Users
`/api/user`
##### Get Another Users info
**GET** `/:userId`
###### Description
Finds the info of the user that matches the given id.

###### Possible Response
```json
{
  "id": "2114199d-bc8b-454b-8785-ab4e3005bfaf",
  "userName": "Steve",
  "biography": "Likes Minecraft",
  "profilePictureUrl": "Steve.jpg"
}
```

### RabbitMQ
Exchange name: `xivgames_exchange`
#### Achievements
##### Achievement Unlock

Name: `achievement_unlock`

Binding: `achievement.unlock`

###### Description
This queue handles messages received from games when an achievement is unlocked.

###### Body
```json
{
  "userId": "2114199d-bc8b-454b-8785-ab4e3005bfaf",
  "achievement": "29bc2b73-34ec-4cdf-995c-9cc62afa07b2"
}
```

#### Game
##### Register Game

Name: `register_game`

Binding: `game.register`

###### Description
This queue handles messages received from games when they launch and need to register on the platform.

###### Body
```json
{
  "id": "2114199d-bc8b-454b-8785-ab4e3005bfaf",
  "name": "Minecraft",
  "description": "First we mine, than we craft.",
  "price": 29.98,
  "image": "minecraft.jpg",
  "icon": "minecraft_icon.jpg",
  "genre": "sandbox",
  "url": "https://minecraft.net",
  "achievements": [
    {
      "id": "6914199d-bc8b-454b-8785-ab4e3005bfaf",
      "name": "We need to go deeper",
      "description": "Go to the nether"
    },
    ...
  ],
  "aiStartGameEndpoint": "https://minecraft.net/ai/start",
  "startGameEndpoint": "https://minecraft.net/start",
  "configurableSettings": {
    "doFireTick": [
      true,
      false
    ],
    ...
  }
}
```
###### Notes
- `startGameEndpoint` has to return an object that contains an id
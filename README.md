# Rocket :rocket:

**A 1.21.4 Paper plugin for Lua scripting**

## Overview

Rocket is a powerful and lightweight [Paper](https://papermc.io/) plugin that enables Lua scripting for Minecraft 1.21.4.

With Rocket, you can write Lua scripts to interact with your server, listen to events, and register commands. 
The plugin provides a simple and flexible way to customize your server without the need for Java.

## Features

- **Lua scripting** – Run Lua scripts within your Paper server
- **Hot reloading** – Modify and reload scripts without restarting your server
- **Game interaction** – Access Minecraft events, commands, and player actions with Lua

## Installation

1. Download the latest Rocket plugin JAR from the [Releases](#) page.
2. Place the JAR file in your server's `plugins` folder.
3. Restart your Paper server to generate the necessary configuration files.

## Usage

1. Navigate to the `plugins/rocket/scripts` directory.
2. Create a new `.lua` script file.
3. Write your Lua script, for example:
   ```lua
   events.on("PlayerJoinEvent", function(event)
    local player = event.player
    
    player.send("&eWelcome to the server!")
   end)
   ```
4. Save the file and reload the Rocket plugin with:
   ```
   /rocket reload file.lua
   ```
5. Enjoy scripting with Lua!

## Configuration

Rocket generates a `config.yml` file in the `plugins/Rocket` directory.
You can tweak various settings like locales.

## Contributing

Contributions are welcome! Please feel free to submit issues, feature requests, or pull requests on the [GitHub repository](#).

## License

Rocket is open-source and available under the [Apache License 2.0](LICENSE).
---

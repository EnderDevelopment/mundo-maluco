# Minecraft Plugin: Mundo Maluco

Mundo Maluco is a playful Minecraft plugin and random-events game mode for Paper servers. While active, it gives online players unexpected gifts, temporary powers, visual effects, friendly creatures, weather changes, and occasional protection from fall damage.

## Install

1. Build the plugin with Java 21 and Maven:

   ```bash
   mvn package
   ```

2. Copy the generated `target/plugin.jar` file to your server's `plugins` directory.

3. Start or restart a Paper Minecraft server running version 1.21.11 or a compatible 1.21 server.

4. Check the server console for the Mundo Maluco enable message.

The plugin starts enabled by default. The administrative command is available to server operators through the `mundomal.admin` permission.

## Use

1. Join the server. When Mundo Maluco is active, players receive a welcome message.

2. Wait for the automatic events. Every 10 seconds, each online player receives one random event:

   - A gift of cooked beef, golden apples, firework rockets, or ender pearls.
   - A silent firework particle show.
   - Fifteen seconds of Speed, Jump Boost, Haste, or Night Vision.
   - A nearby chicken, rabbit, or frog.
   - A switch between rainy and clear weather.

3. Players have a 25% chance to avoid fall damage while the mode is active. A cloud effect, sound, and message indicate when this happens.

4. Use the administrative commands:

   ```text
   /mundomal on
   /mundomal off
   /mundomal status
   ```

   `on` activates the mode, `off` pauses random events and fall protection, and `status` reports whether the mode is active. The `on` and `off` actions broadcast a message to the server.

5. Grant access to trusted administrators with the `mundomal.admin` permission. It is assigned to operators by default.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/1ead5cc2-3b34-4409-8476-158aa03cf0c8?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=1ead5cc2-3b34-4409-8476-158aa03cf0c8

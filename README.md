# Target HUD (Fabric, Minecraft 1.21.4)

A client-side HUD card that appears next to your crosshair when you look at a living entity.
It shows the name, distance, a health bar, HP and armor. It only uses information the game
already gives you. There is no ESP, no tracking through walls and no combat automation.

## Get the .jar from GitHub

1. Unzip this file. GitHub does not unpack zips, so a zip uploaded as is will not build.
   Create a new repository and upload all files from this folder, including the hidden `.github` folder.
   Many browsers skip hidden folders such as `.github` when you drag and drop. If it did not upload, on GitHub choose
   Add file > Create new file, type `.github/workflows/build.yml` as the file name (typing the slashes creates the folders),
   paste the contents of `build-workflow.yml` from this folder and commit.
2. Open the **Actions** tab. The `build` workflow starts by itself after the upload.
3. When it turns green, open **Releases** on the right side of the repository page and download `target-hud-1.4.0.jar` from "Latest build".
   (You can also use the run's Artifacts, but that is a zip that contains the jar.)

## Settings

Press **Right Shift** in game to open the module panels (you can change the key under Options > Controls).

- **Left click** a row to switch it on or off.
- **Right click** a row with an arrow to open its slider (Fog strength, number of Crystals).
- **Drag** a panel by its title to move it.

| Panel | Module | What it does |
| --- | --- | --- |
| HUD | Target HUD | Card with name, distance, health and armor of what you look at |
| HUD | Coordinates | Your XYZ in the top left corner |
| HUD | Facing | The direction you look (north, south, east, west) |
| Effects | Fog | Soft light fog at the screen edges. Slider: strength 0 to 100% |
| Effects | Crystals | Twinkling white squares over the whole screen. Slider: 0 to 30 |

Fog and Crystals are off by default. Switch them on, then right click the row to move the slider.

Your choices are saved in `config/targethud.properties`.

## Install

1. Install Fabric Loader for Minecraft 1.21.4 and put Fabric API in your `mods` folder.
2. Put `target-hud-1.4.0.jar` in the same `mods` folder.
3. Check the rules of any server you join. Most allow client-side HUD mods, but some do not.

## Other Minecraft versions

Edit `gradle.properties` with the values from https://fabricmc.net/develop and also the
`minecraft` version in `src/main/resources/fabric.mod.json`. Newer versions can change
some method names, so the code may need small fixes.

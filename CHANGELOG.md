# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).
The versioning scheme is listed in the README.

<!-- ### Known Issues -->
<!-- ### Added -->
<!-- ### Updated -->
<!-- ### Changed -->
<!-- ### Deprecated -->
<!-- ### Removed -->
<!-- ### Fixed -->
<!-- ### Security -->

## Unreleased - DATE

The big headline feature of this release is that this mod can now support having flower types added by other mods! This is something that would have been near-impossible with v1 of the mod, and I'm very glad that it's now possible. There was a big internal rewrite of the mod's code, but it is now easier to add new flowers in a way that doesn't make the game slower and more memory-hungry every time a new one is added.

Oh, and the mod is now available on NeoForge as well as Fabric. That's a big change too.

### Known Issues

- Worlds that used v1 of Tiny Flowers will not be upgraded to v2.
  - Any previously created flower items and blocks will be removed from the world.

### Added

- Support for NeoForge.
- After creating Florists' Shears with regular shears and a single dye, you can now use more dyes to re-color your Florists' Shears to almost any color possible.
- You can now use multiple Tiny Flowers when crafting Suspicious Stew.
  - Adding more of the same type of flower will increase the duration.
  - Adding different types of flower will combine the effects into a single stew.
- Tiny Cactus Flowers and Leaf Litter can be placed and mixed on top of any block that Leaf Litter can be placed on.
- For mod developers:
  - This mod is now entirely data-driven. This means you can use JSON files to add new Tiny Flower types.
  - Read [the README in GitHub](https://github.com/SecretOnline/tiny-flowers/blob/main/README.md) for more information about what files are required.
  - The `misc/tiny_dirt_flower` directory of this mod's source code contains an example mod containing only JSON and textures.
  - I've also made [a mod generator](https://tiny-flowers-generator.secretonline.co/) to create packs of Tiny Flowers entirely within the browser.

### Fixed

- Game startup times should be massively improved.
- Combining two Florists' Shears together to combine the durability no longer leaves the old shears in the crafting menu.

## v1.5.1 - 2026-02-02

### Added

- Translations
  - Chinese (Simplified), by [FluorescentLava](https://github.com/FluorescentLava)

## v1.5.0 - 2025-12-15

### Updated

- Updated to 1.21.11

### Changed

- Internal changes (no change to functionality).
  - Updated versions of Loom and Gradle used to build.
  - Switched from Yarn to Mojmaps.

## v1.4.0 - 2025-10-05

### Updated

- Updated to 1.21.9

## v1.3.0 - 2025-08-07

Big thank you to [eSonOfAnder](https://github.com/eSonOfAnder) for doing the 1.21.6+ update for me.

### Added

- Suspicious Stew recipes for tiny flowers with vanilla equivalents.
  - The effects are the same, but it takes two tiny flowers to make a Stew instead of one regular flower.
- Using Florists' Shears on a flower will convert that flower into a full garden.
  - This is an alternative way to get tiny flowers without having to open a crafting window.

### Updated

- Updated to 1.21.8
  - Also compatible with 1.21.6 and 1.21.7

## v1.2.0 - 2025-04-07

### Added

- Support for 1.21.5
- New flowers (and flower-like things) can be used to create tiny gardens
  - Wildflowers
  - Leaf litter
  - Tiny cactus flowers

Technical additions

- New item data component (`tiny-flowers:tiny_flowers`) to store which flowers a garden item has.
  - This is used by the garden items created with `ctrl + Pick Block`.
  - This mod still supports items created before this (using the `minecraft:block_state` component), including item tooltips.
    - This may be removed in the future. This mod doesn't have much use, and I don't think people would have created too many of these items?

## v1.1.1 - 2025-02-13

### Fixed

- Crash on startup.

## v1.1.0 - 2025-02-11

Thanks to [haykam821](https://github.com/haykam821) for your help with this one, both with code and bug hunting.

### Added

- Tiny Eyeblossoms will now open and close based on the time of day.
- Translations
  - German (German, Austrian, and Swiss). Thanks [Lucanoria](https://github.com/Lucanoria)!

### Changed

- The eyes of Tiny Open Eyeblossoms are updated match the color of regular Eyeblossoms.
- Completely empty gardens now have a hitbox that can be targeted.

### Fixed

- Tiny Garden block breaking particles now match the flowers in the garden.
- Using Florists' Shears on a Tiny Garden or Pink Petals is now picked up by Skulk Sensors.
  - This is a Block Change event, which creates a vibration of frequency 11.
- Bonemeal is no longer consumed with no effect when empty spaces had been made in a Tiny Garden using Florists' Shears.

## v1.0.0 - 2025-02-02

First release!

### Added

- Florists' Shears item.
  - Made by combining Shears with any dye.
  - Used to craft Tiny Flowers.
  - Can pick flowers out of a Tiny Garden.
- Tiny Flower items.
  - One for each small flower variant.
  - Made by combining a small flower with Florists' Shears.
  - Can be placed on the ground to make a Tiny Garden block.
  - Can be placed inside Pink Petals blocks to make a Tiny Garden block with those petals.
- Tiny Garden block.
  - A block like Pink Petals but can mix multiple types of Tiny Flowers together.

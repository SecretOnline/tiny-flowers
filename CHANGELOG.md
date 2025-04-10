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

### Known Issues

This version of the mod for 1.21.1 is a port from 1.21.4. Mojang changed a few things between those versions, which made this port difficult. I've tried to fit everything in,

The following differences from the 1.21.4 version of the mod are known:

- Using `ctrl`/`cmd` + Pick Block on a Tiny Garden does not give a copy of the garden.
  - Since this was only available in Creative mode, an alternative is to use the `/give` command to give yourself a Garden with the `flower_variant_X` properties set.

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

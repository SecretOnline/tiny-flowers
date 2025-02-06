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

Thanks to [haykam821](https://github.com/haykam821) for your help with this one, both with code and bug hunting.

### Added

- Tiny Eyeblossoms will now open and close based on the time of day.

### Fixed

- Tiny Garden block breaking particles now match the flowers in the garden.
- Using Florists' Shears on a Tiny Garden or Pink Petals is now picked up by Skulk Sensors.
  - This is a Block Change event, which creates a vibration of frequency 11.
- Completely empty gardens now have a hitbox that can be targeted.

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

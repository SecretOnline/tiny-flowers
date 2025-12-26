# [Tiny Flowers](https://secretonline.co/mods/tiny-flowers)

Add tiny variants of all Vanilla flowers, much like Pink Petals.

![A flowerbed outside a window.](https://cdn.modrinth.com/data/S0Lqneqb/images/2c5785e8cafdd0e74ab54e612d8b070497f54eb4.png)

If you're looking for a more thorough description of the mod and its features, check out [the mod's description](./MODRINTH.md).

## For other mod developers

So you want to add your own Tiny Flowers? This section explains the JSON files required to do just that.

The files required are split into data and assets. The data files are loaded by the server and synced via a dynamic registry. The asset files are client-only, and affect how the flowers look. Unfortunately, most of the work is in the second half. For each custom file, I have written a Typescript definition, for those proficient in Typescript, and an example JSON file. Alternatively, you can check the source code of this mod for the codec definitions.

To keep this README readable, everything in inside the following expandable section:

<details>
<summary>JSON files</summary>

```ts
type Identifier = `${string}:${string}`;
```

### Data

There is only one data file required per flower, which defines all of the server-side behaviour of the mod.

#### `data/<namespace>/tiny_flowers/tiny_flower/<id>.json`

This file defines the behaviours of this flower variant.

```ts
interface TinyFlowerData {
  /** The unique identifier of this type. Should match <namespace>:<id> from the path of this file. */
  id: Identifier;
  /** The ID of the original flower block. Will be turned into Tiny Flowers if crafted with or used on by Florists' Shears. */
  original_id: Identifier;
  /** Optional. Whether the original block is segmentable like Pink Petals or Wildflowers. Defaults to false. */
  is_segmented?: boolean;
  /** Optional. List of block IDs or tags that this tiny flower can be placed on. Defaults to `#tiny_flowers:tiny_flower_can_survive_on`. */
  can_survive_on?: (Identifier | TagKey)[];
  /** Optional. List of mob effects to be applied if consumed in Suspicious Stew. Defaults to an empty list. */
  suspicious_stew_effects?: {
    /** The ID of the mob effect that will be applied. */
    id: Identifier;
    /** Effect duration in ticks. */
    duration: number;
  }[];
}
```

```json
{
  "id": "tiny_dirt_flower:tiny_dirt",
  "original_id": "minecraft:dirt",
  "can_survive_on": ["minecraft:dirt"],
  "suspicious_stew_effects": [
    {
      "duration": 10,
      "id": "minecraft:darkness"
    }
  ]
}
```

### Assets

Each flower type requires edits to 1 shared JSON file, 6 individual JSON files (mostly to do with item and block models), at least 2 textures (1 for the item and potentially many for the block model), and a translation key in your language files.

#### `<namespace>/assets/tiny_flowers/tiny_flower/<id>.json`

This is the entrypoint for any rendering-related concepts for this flower type. It defines the models that will be used for the item and block, and any other visual behaviours.

```ts
interface TinyFlowerResources {
  /** The unique identifier of this type. Should match <namespace>:<id> from the path of this file. */
  id: Identifier;
  /** The item model to use for this type. Usually of the form `<namespace>:item/<id>`. */
  item_model: Identifier;
  /** How to shade model quads that have a tintindex defined. */
  tint_source?: "grass" | "dry_foliage";
  /** The block model to render when this type is in the first spot. Usually of the form `<namespace>:block/<id>_1`. */
  model1: Identifier;
  /** The block model to render when this type is in the second spot. Usually of the form `<namespace>:block/<id>_2`. */
  model2: Identifier;
  /** The block model to render when this type is in the third spot. Usually of the form `<namespace>:block/<id>_3`. */
  model3: Identifier;
  /** The block model to render when this type is in the fourth spot. Usually of the form `<namespace>:block/<id>_4`. */
  model4: Identifier;
}
```

```json
{
  "id": "tiny_dirt_flower:tiny_dirt",
  "item_model": "tiny_dirt_flower:item/tiny_dirt",
  "model1": "tiny_dirt_flower:block/tiny_flowers/tiny_dirt_1",
  "model2": "tiny_dirt_flower:block/tiny_flowers/tiny_dirt_2",
  "model3": "tiny_dirt_flower:block/tiny_flowers/tiny_dirt_3",
  "model4": "tiny_dirt_flower:block/tiny_flowers/tiny_dirt_4"
}
```

#### `<namespace>/lang/<language>.json`

Each tiny flower type needs a translatable name. For most flowers, the English version of this string will be "Tiny &lt;name of base flower>".

```json
{
  "block.<namespace>.<id>": ""
}
```

#### `<namespace>/items/tiny_flower.json`

Minecraft only starts the item model loading process from an item definition file, so we need to jump in and create one. This also allows resource packs to do more complicated replacements.

This mod uses the `minecraft:select` model type to switch which model is displayed. If you want to follow this pattern, which you probably do, then you will need a case for each type of flower you are adding. The final `model` identifier key must match the `item_model` key in the main resources file.

Read [Items model definition](https://minecraft.wiki/w/Items_model_definition) on the wiki for a full reference.

```json
{
  "model": {
    "type": "minecraft:select",
    "cases": [
      {
        "model": {
          "type": "minecraft:model",
          "model": "tiny_dirt_flower:item/tiny_dirt"
        },
        "when": "tiny_dirt_flower:tiny_dirt"
      }
    ],
    "property": "tiny_flowers:tiny_flower"
  }
}
```

#### `<namespace>/models/item/<id>.json`

The path of this file must match the `item_model` key in the main resources file and be referenced in the items file above.

If using `minecraft:item/generated`, the texture referenced in `layer0` should be placed a`<namespace>/textures/item/<id>.png`.

![Texture for the Tiny Dirt Flower item.](./misc/tiny_dirt_flower/assets/tiny_dirt_flower/textures/item/tiny_dirt.png)

Read [Model#Item_models](https://minecraft.wiki/w/Model#Item_models) on the wiki for a full reference.

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "tiny_dirt_flower:item/tiny_dirt"
  }
}
```

#### `<namespace>/models/block/<id>_<index>.json`

These files define the models that will be rendered when the flower is placed in the world. The path of these files must match the `model<index>` key in the main resources file.

This mod provides a set of pre-defined models for common configurations. You are encouraged to use the model with the fewest number of layers that still gets the idea of your flower across.

| Number of layers | Tinted stem (default)                      | Untinted stem                                       | Texture keys                                                           |
| ---------------- | ------------------------------------------ | --------------------------------------------------- | ---------------------------------------------------------------------- |
| 1                | `tiny_flowers:block/garden_<index>`        | `tiny_flowers:block/garden_untinted_<index>`        | `flowerbed`, `stem`, `particle`                                        |
| 2                | `tiny_flowers:block/garden_double_<index>` | `tiny_flowers:block/garden_double_untinted_<index>` | `flowerbed`, `flowerbed_upper`, `stem`, `particle`                     |
| 2                | `tiny_flowers:block/garden_triple_<index>` | `tiny_flowers:block/garden_triple_untinted_<index>` | `flowerbed`, `flowerbed_middle`, `flowerbed_upper`, `stem`, `particle` |

If using one of these models, textured referenced by the layers should be placed at `<namespace>/textures/item/<id>.png`.

![Texture for the Tiny Dirt Flower block.](./misc/tiny_dirt_flower/assets/tiny_dirt_flower/textures/block/tiny_dirt.png)

Some specialty base models are also available. The most useful of these is likely to be `tiny_flowers:block/garden_leaf_litter_<index>`, which gives a very low single plane like Leaf Litter.

Read [Model#Block_models](https://minecraft.wiki/w/Model#Block_models) on the wiki for a full reference.

```json
{
  "parent": "tiny_flowers:block/garden_1",
  "textures": {
    "flowerbed": "tiny_dirt_flower:block/tiny_dirt",
    "particle": "tiny_dirt_flower:block/tiny_dirt",
    "stem": "minecraft:block/pink_petals_stem"
  }
}
```

</details>

## Contributing

Contributions to this project are welcome. I'm still very must in the process of writing this mod, so might be a bit hesitant to include wide sweepeing changes, as this is a learning experience for me.

Thanks to the following people for their contributions to this mod:

- [haykam821](https://github.com/haykam821)
- [Lucanoria](https://github.com/Lucanoria)
- [eSonOfAnder](https://github.com/eSonOfAnder)

## License

This mod is licensed under the Mozilla Public License 2.0. You may use this mod in your mod packs.

package com.mars.laserbridges;

import com.google.common.collect.Lists;
import com.mars.deimos.config.DeimosConfig;

import java.util.List;

public class LasersConfig extends DeimosConfig {
    @Entry public static int max_length = 15;
    @Entry public static List<String> blocks_cut_through_by_lasers = Lists.newArrayList(
            "minecraft:air", "minecraft:cave_air", "minecraft:void_air", "minecraft:short_grass", "minecraft:tall_grass", "minecraft:fern", "minecraft:large_fern", "minecraft:dead_bush"
    );
}

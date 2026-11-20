package com.spartanforge.godofwar;

import com.spartanforge.godofwar.combat.BloodOrbManager;
import com.spartanforge.godofwar.combat.GodOfWarCombatEvents;
import com.spartanforge.godofwar.combat.PlayerProgressionManager;
import com.spartanforge.godofwar.combat.StunExecutionManager;
import com.spartanforge.godofwar.compat.EpicFightCompat;
import com.spartanforge.godofwar.magic.MagicManager;
import com.spartanforge.godofwar.network.GodOfWarNetwork;
import com.spartanforge.godofwar.registry.ModBlocks;
import com.spartanforge.godofwar.registry.ModCreativeTabs;
import com.spartanforge.godofwar.registry.ModEntities;
import com.spartanforge.godofwar.registry.ModItems;
import com.spartanforge.godofwar.registry.ModMenus;
import com.spartanforge.godofwar.registry.ModSpawns;
import com.spartanforge.godofwar.registry.ModVillagers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;

public final class GodOfWarMod {
    public static final String MOD_ID = "godofwar";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        GeckoLib.initialize();
        ModBlocks.register();
        ModEntities.register();
        ModItems.register();
        ModMenus.register();
        ModVillagers.register();
        ModCreativeTabs.register();
        GodOfWarNetwork.registerServerReceivers();
        GodOfWarCombatEvents.register();
        BloodOrbManager.register();
        MagicManager.register();
        StunExecutionManager.register();
        PlayerProgressionManager.register();
        ModSpawns.register();
        LOGGER.info("God of War Multi-Loader inicializado (45 entidades, 8 bloques, Armas 3D, Ejecuciones Brutales, Estructuras Míticas | EpicFight: {})",
            EpicFightCompat.isLoaded());
    }
}

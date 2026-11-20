package com.spartanforge.godofwar.forge;

import com.spartanforge.godofwar.GodOfWarMod;
import com.spartanforge.godofwar.client.GodOfWarClientInit;
import com.spartanforge.godofwar.client.GodOfWarKeybinds;
import com.spartanforge.godofwar.client.renderer.*;
import com.spartanforge.godofwar.registry.ModEntities;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = GodOfWarMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class GodOfWarForgeClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(GodOfWarClientInit::init);
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(GodOfWarKeybinds.KEY_WEAPON_ABILITY);
        event.register(GodOfWarKeybinds.KEY_EXECUTE);
        event.register(GodOfWarKeybinds.KEY_UPGRADE_MENU);
        event.register(GodOfWarKeybinds.KEY_CURIO_POWER);
        event.register(GodOfWarKeybinds.KEY_MAGIC_MENU);
        event.register(GodOfWarKeybinds.KEY_CYCLE_MAGIC);
    }

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        for (String skin : event.getSkins()) {
            LivingEntityRenderer<?, ?> renderer = event.getSkin(skin);
            if (renderer instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(new BackWeaponRenderLayer(playerRenderer));
            }
        }
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.ZEUS_BOSS.get(), ZeusBossRenderer::new);
        event.registerEntityRenderer(ModEntities.ARES_BOSS.get(), AresBossRenderer::new);
        event.registerEntityRenderer(ModEntities.ATHENA_BOSS.get(), AthenaBossRenderer::new);
        event.registerEntityRenderer(ModEntities.POSEIDON_BOSS.get(), PoseidonBossRenderer::new);
        event.registerEntityRenderer(ModEntities.HADES_BOSS.get(), HadesBossRenderer::new);
        event.registerEntityRenderer(ModEntities.HERMES_BOSS.get(), HermesBossRenderer::new);
        event.registerEntityRenderer(ModEntities.HELIOS_BOSS.get(), HeliosBossRenderer::new);
        event.registerEntityRenderer(ModEntities.HERCULES_BOSS.get(), HerculesBossRenderer::new);
        event.registerEntityRenderer(ModEntities.PERSEPHONE_BOSS.get(), PersephoneBossRenderer::new);
        event.registerEntityRenderer(ModEntities.THANATOS_BOSS.get(), ThanatosBossRenderer::new);
        event.registerEntityRenderer(ModEntities.ODIN_BOSS.get(), OdinBossRenderer::new);
        event.registerEntityRenderer(ModEntities.THOR_BOSS.get(), ThorBossRenderer::new);
        event.registerEntityRenderer(ModEntities.BALDUR_BOSS.get(), BaldurBossRenderer::new);
        event.registerEntityRenderer(ModEntities.SIGRUN_BOSS.get(), SigrunBossRenderer::new);
        event.registerEntityRenderer(ModEntities.CRONOS_TITAN.get(), CronosTitanRenderer::new);
        event.registerEntityRenderer(ModEntities.GAIA_TITAN.get(), GaiaTitanRenderer::new);
        event.registerEntityRenderer(ModEntities.ATLAS_TITAN.get(), AtlasTitanRenderer::new);
        event.registerEntityRenderer(ModEntities.TYPHON_TITAN.get(), TyphonTitanRenderer::new);
        event.registerEntityRenderer(ModEntities.PERSES_TITAN.get(), PersesTitanRenderer::new);
        event.registerEntityRenderer(ModEntities.MINOTAUR_MOB.get(), MinotaurMobRenderer::new);
        event.registerEntityRenderer(ModEntities.SIREN_MOB.get(), SirenMobRenderer::new);
        event.registerEntityRenderer(ModEntities.CYCLOPS_MOB.get(), CyclopsMobRenderer::new);
        event.registerEntityRenderer(ModEntities.GORGON_MOB.get(), GorgonMobRenderer::new);
        event.registerEntityRenderer(ModEntities.CERBERUS_MOB.get(), CerberusMobRenderer::new);
        event.registerEntityRenderer(ModEntities.HARPY_MOB.get(), HarpyMobRenderer::new);
        event.registerEntityRenderer(ModEntities.SATYR_MOB.get(), SatyrMobRenderer::new);
        event.registerEntityRenderer(ModEntities.CENTAUR_MOB.get(), CentaurMobRenderer::new);
        event.registerEntityRenderer(ModEntities.DRAUGR_MOB.get(), DraugrMobRenderer::new);
        event.registerEntityRenderer(ModEntities.WULVER_MOB.get(), WulverMobRenderer::new);
        event.registerEntityRenderer(ModEntities.EINHERJAR_MOB.get(), EinherjarMobRenderer::new);
        event.registerEntityRenderer(ModEntities.ROMAN_WARRIOR.get(), RomanWarriorRenderer::new);
        event.registerEntityRenderer(ModEntities.ROMAN_ARCHER.get(), RomanArcherRenderer::new);
        event.registerEntityRenderer(ModEntities.ROMAN_BERSERKER.get(), RomanBerserkerRenderer::new);
        event.registerEntityRenderer(ModEntities.ROMAN_CENTURION.get(), RomanCenturionRenderer::new);
        event.registerEntityRenderer(ModEntities.ROMAN_PRAETORIAN.get(), RomanPraetorianRenderer::new);
        event.registerEntityRenderer(ModEntities.PHOENIX_CREATURE.get(), PhoenixCreatureRenderer::new);
        event.registerEntityRenderer(ModEntities.PEGASUS_CREATURE.get(), PegasusCreatureRenderer::new);
        event.registerEntityRenderer(ModEntities.HIPPOCAMPUS_CREATURE.get(), HippocampusCreatureRenderer::new);
        event.registerEntityRenderer(ModEntities.FENRIR_CREATURE.get(), FenrirCreatureRenderer::new);
        event.registerEntityRenderer(ModEntities.SPARTAN_SMITH_VILLAGER.get(), SpartanSmithVillagerRenderer::new);
        event.registerEntityRenderer(ModEntities.GREEK_ORACLE_VILLAGER.get(), GreekOracleVillagerRenderer::new);
        event.registerEntityRenderer(ModEntities.DWARF_FORGER_VILLAGER.get(), DwarfForgerVillagerRenderer::new);
        event.registerEntityRenderer(ModEntities.ROMAN_QUARTERMASTER_VILLAGER.get(), RomanQuartermasterVillagerRenderer::new);
        event.registerEntityRenderer(ModEntities.THROWN_LEVIATHAN_AXE.get(), ThrownLeviathanAxeRenderer::new);
        event.registerEntityRenderer(ModEntities.DRAUPNIR_SPEAR_PROJECTILE.get(), DraupnirSpearRenderer::new);
        event.registerEntityRenderer(ModEntities.BLOOD_ORB.get(), BloodOrbRenderer::new);
    }
}

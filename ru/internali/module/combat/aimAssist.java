//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Basic\Desktop\projects\java\deof\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingUpdateEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package ru.internali.module.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.internali.CatClient;
import ru.internali.module.Category;
import ru.internali.module.Module;
import ru.internali.settings.Setting;

public class aimAssist
extends Module {
    public float[] facing;
    public Entity target;

    public aimAssist() {
        super("Aim Assist", "aim to players", Category.COMBAT);
        CatClient.instance.settingsManager.rSetting(new Setting("Range", this, 8.0, 0.0, 16.0, false));
        CatClient.instance.settingsManager.rSetting(new Setting("Yaw", this, 0.3, 0.0, 2.0, false));
        CatClient.instance.settingsManager.rSetting(new Setting("Pitch", this, 0.3, 0.0, 2.0, false));
        CatClient.instance.settingsManager.rSetting(new Setting("Predict", this, 0.3, 0.0, 2.0, false));
        CatClient.instance.settingsManager.rSetting(new Setting("Walls", this, true));
    }

    public boolean isTarget(Entity entity) {
        float Range = (float)CatClient.instance.settingsManager.getSettingByName(this, "Range").getValDouble();
        return entity != Minecraft.getMinecraft().player && entity != Minecraft.getMinecraft().getRenderViewEntity() && Minecraft.getMinecraft().player.getDistanceSq(entity) <= Math.pow(Range, 2.0) && entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() > 0.0f;
    }

    public static float[] faceTarget(Entity target, float p_706252, float p_706253, boolean miss) {
        double var7;
        double var4 = target.posX - aimAssist.mc.player.posX;
        double var5 = target.posZ - aimAssist.mc.player.posZ;
        if (target instanceof EntityLivingBase) {
            EntityLivingBase var6 = (EntityLivingBase)target;
            var7 = var6.posY + (double)var6.getEyeHeight() - (aimAssist.mc.player.posY + (double)aimAssist.mc.player.getEyeHeight());
        } else {
            var7 = (target.getEntityBoundingBox().minY + target.getEntityBoundingBox().maxY) / 2.0 - (aimAssist.mc.player.posY + (double)aimAssist.mc.player.getEyeHeight());
        }
        double var8 = MathHelper.sqrt((double)(var4 * var4 + var5 * var5));
        float var9 = (float)(Math.atan2(var5, var4) * 180.0 / Math.PI) - 90.0f;
        float var10 = (float)(-(Math.atan2(var7 - (target instanceof EntityPlayer ? 0.25 : 0.0), var8) * 180.0 / Math.PI));
        float f = aimAssist.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
        float gcd = f * f * f * 1.2f;
        float pitch = aimAssist.updateRotation(aimAssist.mc.player.rotationPitch, var10, p_706253);
        float yaw = aimAssist.updateRotation(aimAssist.mc.player.rotationYaw, var9, p_706252);
        yaw -= yaw % gcd;
        pitch -= pitch % gcd;
        return new float[]{yaw, pitch};
    }

    public static float updateRotation(float current, float intended, float speed) {
        float f = MathHelper.wrapDegrees((float)(intended - current));
        if (f > speed) {
            f = speed;
        }
        if (f < -speed) {
            f = -speed;
        }
        return current + f;
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
        float Range = (float)CatClient.instance.settingsManager.getSettingByName(this, "Range").getValDouble();
        float Yaw = (float)CatClient.instance.settingsManager.getSettingByName(this, "Yaw").getValDouble();
        float Pitch = (float)CatClient.instance.settingsManager.getSettingByName(this, "Pitch").getValDouble();
        boolean Walls = CatClient.instance.settingsManager.getSettingByName(this, "Walls").getValBoolean();
        for (Object theObject : aimAssist.mc.world.loadedEntityList) {
            EntityLivingBase entity;
            if (!(theObject instanceof EntityLivingBase) || (entity = (EntityLivingBase)theObject) instanceof EntityPlayerSP) continue;
            this.facing = aimAssist.faceTarget((Entity)entity, 360.0f, 360.0f, false);
            if (!((double)aimAssist.mc.player.getDistance((Entity)entity) < (double)Range)) continue;
            if (!(entity instanceof EntityPlayer)) {
                return;
            }
            float f = this.facing[0];
            float f2 = this.facing[1];
            if (aimAssist.mc.player.rotationYaw < f) {
                aimAssist.mc.player.rotationYaw += Yaw;
            }
            if (aimAssist.mc.player.rotationYaw > f) {
                aimAssist.mc.player.rotationYaw -= Yaw;
            }
            if (aimAssist.mc.player.rotationPitch < f2) {
                aimAssist.mc.player.rotationPitch += Pitch;
            }
            if (!(aimAssist.mc.player.rotationPitch > f2)) continue;
            aimAssist.mc.player.rotationPitch -= Pitch;
        }
    }
}


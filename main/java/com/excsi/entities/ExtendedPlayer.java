package com.excsi.entities;

import com.excsi.network.ExtendedPlayerSyncPacket;
import com.excsi.network.NetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import java.util.UUID;

public class ExtendedPlayer implements IExtendedEntityProperties {
public static final String propertyname = "GoldenSwordsFaction";
public final EntityPlayer player;
public int GoldenSwordsLvl;
public float GoldShieldAmount;
public long ReflectTime;
public long ReflectCooldown;
public long AllySpawnCooldown;
public long RespawnCooldown;
public long RespawnInvulnerability;
public int[] IncurablePotionData;

    public ExtendedPlayer(EntityPlayer player){
    this.player = player;
    this.GoldenSwordsLvl = 0;
    this.GoldShieldAmount = 0.0f;
    this.ReflectTime=0L;
    this.ReflectCooldown=0L;
    this.AllySpawnCooldown=0L;
    this.RespawnCooldown=0L;
    this.RespawnInvulnerability=0L;
    this.IncurablePotionData = new int[]{0, 0, 0};
    }
    public static void register(EntityPlayer player){
    player.registerExtendedProperties(ExtendedPlayer.propertyname, new ExtendedPlayer(player));
    }
    public static ExtendedPlayer get(EntityPlayer player){
        return (ExtendedPlayer)player.getExtendedProperties(ExtendedPlayer.propertyname);
    }
    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = new NBTTagCompound();
        properties.setInteger("GoldenSwordsLvl", this.GoldenSwordsLvl);
        properties.setFloat("GoldShieldAmount", this.GoldShieldAmount);
        properties.setLong("ReflectTime", this.ReflectTime);
        properties.setLong("ReflectCooldown", this.ReflectCooldown);
        properties.setLong("AllySpawnCooldown", this.AllySpawnCooldown);
        properties.setLong("RespawnCooldown", this.RespawnCooldown);
        properties.setLong("RespawnInvulnerability", this.RespawnInvulnerability);
        properties.setIntArray("IncurablePotionData", this.IncurablePotionData);
        compound.setTag(ExtendedPlayer.propertyname, properties);
    }
    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = (NBTTagCompound)compound.getTag(ExtendedPlayer.propertyname);
        this.GoldenSwordsLvl = properties.getInteger("GoldenSwordsLvl");
        this.GoldShieldAmount = properties.getFloat("GoldShieldAmount");
        this.ReflectTime = properties.getLong("ReflectTime");
        this.ReflectCooldown = properties.getLong("ReflectCooldown");
        this.AllySpawnCooldown = properties.getLong("AllySpawnCooldown");
        this.RespawnCooldown = properties.getLong("RespawnCooldown");
        this.RespawnInvulnerability = properties.getLong("RespawnInvulnerability");
        this.IncurablePotionData = properties.getIntArray("IncurablePotionData");
    }
    @Override
    public void init(Entity entity, World world) {
    }
    public int getGoldenSwordsLvl(){
        return this.GoldenSwordsLvl;
    }
    public void setGoldenSwordsLvl(int level){
        if(level >=0 && level<=10){
            this.GoldenSwordsLvl=level;
            AttributeModifier health = new AttributeModifier(UUID.fromString("19519bac-8b89-4292-a56f-95be05597d4e"),"goldenswordhealth",(double)this.GoldenSwordsLvl*4,0);
            player.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(health);
            player.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(health);
            AttributeModifier speed = new AttributeModifier(UUID.fromString("8361fa0d-f1ef-46d4-b8c3-fa633c0e5007"),"goldenswordspeed",(double)this.GoldenSwordsLvl*0.05,1);
            player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(speed);
            player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(speed);
            this.sync();
        }
    }
    public float getGoldShieldAmount(){
        return this.GoldShieldAmount;
    }
    public int maxAllowedShields(){
        if(this.getGoldenSwordsLvl()>=5){
            return this.GoldenSwordsLvl-4;
        }
        return 0;
    }
    public void setGoldShieldAmount(float amount){
        this.GoldShieldAmount=amount;
        this.sync();
    }
    public void addGoldShieldAmount(float amount){
    float maxShield = this.maxAllowedShields()*100f;
    if(amount+this.getGoldShieldAmount()>maxShield){
        this.GoldShieldAmount=maxShield;
        this.sync();
    }else{
        this.GoldShieldAmount=amount+this.getGoldShieldAmount();
        this.sync();
        }
    }
    public void setAllySpawnCooldown(long cd){
    this.AllySpawnCooldown=cd;
    this.sync();
    }
    public long getAllySpawnCooldown(){
    return this.AllySpawnCooldown;
    }
    public void setReflectTime(long l){
    this.ReflectTime=l;
    this.sync();
    }
    public long getReflectTime(){
    return this.ReflectTime;
    }
    public void setReflectCooldown(long a){
    this.ReflectCooldown=a;
    this.sync();
    }
    public long getReflectCooldown(){
    return this.ReflectCooldown;
    }
    public void setRespawnCooldown(long g){
    this.RespawnCooldown=g;
    this.sync();
    }
    public long getRespawnCooldown(){
    return this.RespawnCooldown;
    }
    public void setRespawnInvulnerability(long d){
    this.RespawnInvulnerability=d;
    this.sync();
    }
    public long getRespawnInvulnerability(){
    return this.RespawnInvulnerability;
    }
    public void resetCooldowns(){
    this.RespawnCooldown = -36000L;
    this.ReflectCooldown = -2400L;
    this.AllySpawnCooldown = -1800L;
    this.sync();
    }
    public void setIncurablePotionData(int id,int duration,int amplifier){
        this.IncurablePotionData = new int[]{id,duration,amplifier};
    }
    public int[] getIncurablePotionData(){
    return this.IncurablePotionData;
    }
    public void sync(){
    NetworkHandler.sendToPlayer(new ExtendedPlayerSyncPacket(this.player), this.player);
    }
}
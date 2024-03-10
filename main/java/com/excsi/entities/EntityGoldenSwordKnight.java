package com.excsi.entities;

import com.emoniph.witchery.util.ParticleEffect;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityGoldenSwordKnight extends EntityTameable {
public long EntityExistedMax=1200L;
public EntityPlayer owner;
    public EntityGoldenSwordKnight(World world){
        super(world);
        this.setSize(1f,2f);
        this.setTamed(true);
        this.tasks.addTask(1, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(2,new EntityAIWander(this,0.5d));
        this.tasks.addTask(3, new EntityAIAttackOnCollide(this, 1.0D, true));
        this.tasks.addTask(4, new EntityAISwimming(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, true, IMob.mobSelector));
    }
    @Override
    public EntityAgeable createChild(EntityAgeable p_90011_1_) {
        return null;
    }
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(this.ticksExisted>=EntityExistedMax){
            this.setDead();
            ParticleEffect.CLOUD.send(null,this.worldObj,this.posX,this.posY+0.5,this.posZ,0.5,0.5,10);
        }
        if(!this.worldObj.isRemote && this.getOwner()==null){
            this.setDead();
            ParticleEffect.CLOUD.send(null,this.worldObj,this.posX,this.posY+0.5,this.posZ,0.5,0.5,10);
        }
    }
    @Override
    public void setAttackTarget(EntityLivingBase t) {
        super.setAttackTarget(t);
        if(t instanceof EntityPlayer){
            if(t.isEntityEqual(this.owner)){
                this.setAttackTarget(null);
            }
        }
    }
    @Override
    public boolean isAIEnabled() {
        return true;
    }
    @Override
    protected void applyEntityAttributes(){
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(150d);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4d);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(250d);
        this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1d);
    }
    @Override
    public boolean isOnSameTeam(EntityLivingBase p_142014_1_)
    {
        if (this.isTamed())
        {
            EntityLivingBase entitylivingbase1 = this.getOwner();

            if (p_142014_1_ == entitylivingbase1)
            {
                return true;
            }

            if (entitylivingbase1 != null)
            {
                return entitylivingbase1.isOnSameTeam(p_142014_1_);
            }
        }
        return super.isOnSameTeam(p_142014_1_);
    }
    public void setOwner(EntityPlayer owner){
        this.owner=owner;
    }
    @Override
    public EntityLivingBase getOwner() {
        return this.owner;
    }
    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this),150f);
    }
    @Override
    public void setTamed(boolean p_70903_1_){
        super.setTamed(p_70903_1_);
    }
    @Override
    public boolean canAttackClass(Class var){
        return var != EntityGoldenSwordKnight.class;
    }
}
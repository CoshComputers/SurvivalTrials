package com.dsd.st.entities;

import com.dsd.st.util.CustomLogger;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class GiantZombieEntity extends ZombieEntity {
    private static final float ORIGINAL_HEIGHT = 1.8f;
    private static final float ORIGINAL_WIDTH = 0.6f;
    //private static final float SCALE_FACTOR = 2;
    private static float SCALE_FACTOR = 2;
    public static GiantZombieEntity createForRegistration(EntityType<? extends ZombieEntity> type, World worldIn) {
        return new GiantZombieEntity(type, worldIn);
    }
    public GiantZombieEntity(EntityType<? extends ZombieEntity>type, World worldIn) {
        super(type,worldIn);

        //SCALE_FACTOR = ConfigManager.getInstance().getGiantConfigContainer().getGiantConfig().getScaleFactor();

    }
    public float getScaleFactor() {
        return SCALE_FACTOR;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)  // Max health of the entity
                .add(Attributes.MOVEMENT_SPEED, 0.05D)  // Movement speed of the entity
                .add(Attributes.ATTACK_DAMAGE, 10.0D)  // Attack damage of the entity
                .add(Attributes.FOLLOW_RANGE, 80.0D)  // Follow range of the entity
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)  // Knockback resistance - immune
                .add(Attributes.ATTACK_KNOCKBACK, 1.0D)  // Attack knockback of the entity
                .add(Attributes.FLYING_SPEED, 0.0D)  // Flying speed of the entity, if applicable
                .add(Attributes.ATTACK_SPEED, 4.0D)  // Attack speed of the entity
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0D); //no chance of Reinforcements
    }


    /*-------------------------------------- OVERRIDES BELOW HERE -----------------------------------*/

    @Override
    protected void checkInsideBlocks() {
        // Call super method to ensure normal behavior
        CustomLogger.getInstance().debug(String.format("checkInsideBlocks Called [Giant] Block Pos:%s",this.blockPosition()));
        super.checkInsideBlocks();

        // Your debugging code here
    }
    @Override
    public void move(MoverType typeIn, Vector3d pos) {
        // Call super method to ensure normal behavior
        CustomLogger.getInstance().debug(String.format("move Called [Giant] MoverType: %s, Vector: %s",typeIn.toString(),pos.toString()));
        super.move(typeIn, pos);
        // Your debugging code here
    }
    @Override
    public boolean isColliding(BlockPos pos, BlockState state) {
        CustomLogger.getInstance().debug(String.format("isColliding [Giant] Block Pos:%s",pos));
        // Call super method to ensure normal behavior
        return super.isColliding(pos,state);
    }



    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return super.getStandingEyeHeight(poseIn, sizeIn) * SCALE_FACTOR;
    }

    @Override
    protected void registerGoals() {
        // No call to super, so the goals from ZombieEntity are not registered.
    }
     @Override
    public EntitySize getDimensions(Pose pose) {
        EntitySize original = super.getDimensions(pose);
        float scaleFactor = getScaleFactor();  // Assuming getScaleFactor is a method on your GiantZombieEntity class
        return EntitySize.scalable(original.width * scaleFactor, original.height * scaleFactor);
    }

    @Override
    public String toString(){
       StringBuilder sb = new StringBuilder();
       sb.append("Health = ").append(this.getHealth()).append("\n");
       sb.append("Is In Wall = ").append(this.isInWall() ? "true" : "false").append("\n");
       sb.append("Eye Height = ").append(this.getStandingEyeHeight(this.getPose(),this.getDimensions(this.getPose()))).append("\n");
       sb.append("Air Level = ").append(this.getAirSupply()).append("\n");
       sb.append("Giant Entity Detail, Scale Factory = ").append(this.getScaleFactor()).append("\n");
       sb.append("Bounding Box = ").append(this.getBoundingBox()).append("\n");
       sb.append("Pose = ").append(this.getPose()).append("\n");
       sb.append(this.getDimensions(this.getPose())).append("\n");
       sb.append("Position = ").append(this.blockPosition()).append("\n");

       return sb.toString();
    }
}

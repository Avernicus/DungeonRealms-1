package net.dungeonrealms.game.world.entity.type.monster.StaffMobs;

import net.dungeonrealms.GameAPI;
import net.dungeonrealms.game.world.entity.EnumEntityType;
import net.dungeonrealms.game.world.entity.type.monster.EnumMonster;
import net.dungeonrealms.game.world.entity.type.monster.base.DRZombie;
import net.dungeonrealms.game.world.item.DamageAPI;
import net.dungeonrealms.game.world.item.Item;
import net.dungeonrealms.game.world.item.itemgenerator.ItemGenerator;
import net.minecraft.server.v1_9_R2.*;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;

/**
 * Created by Kieran Quigley (Proxying) on 14-Jun-16.
 */
public class StaffZombie extends DRZombie implements IRangedEntity {

    public StaffZombie(World world, EnumMonster mons, int tier) {
        super(world, mons, tier, EnumEntityType.HOSTILE_MOB);
        this.tier = tier;
        setWeapon(tier);
    }

    public StaffZombie(World world) {
        super(world);
    }

    /**
     * Implemented by Giovanni on 13/8/2016
     */
    @Override
    public void r()
    {
        try
        {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);

            bField.set(goalSelector, new LinkedHashSet<PathfinderGoalSelector>());
            bField.set(targetSelector, new LinkedHashSet<PathfinderGoalSelector>());
            cField.set(goalSelector, new LinkedHashSet<PathfinderGoalSelector>());
            cField.set(targetSelector, new LinkedHashSet<PathfinderGoalSelector>());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        this.goalSelector.a(0, new PathfinderGoalRandomStroll(this, .6F));
        this.goalSelector.a(1, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(2, new PathfinderGoalRandomLookaround(this));
        this.goalSelector.a(4, new PathfinderGoalArrowAttack(this, 1.0D, 20, 60, 15.0F));
    }

    @Override
    public void setWeapon(int tier) {
        org.bukkit.inventory.ItemStack weapon = new ItemGenerator().setType(Item.ItemType.STAFF).setTier(Item.ItemTier.getByTier(tier)).setRarity(GameAPI.getItemRarity(false)).generateItem().getItem();
        this.setEquipment(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(weapon));
        ((LivingEntity) this.getBukkitEntity()).getEquipment().setItemInMainHand(weapon);
    }


    @Override
    public void setStats() {

    }

    @Override
    public EnumMonster getEnum() {
        return this.monsterType;
    }

    @Override
    public void a(EntityLiving entity, float f) {
        net.minecraft.server.v1_9_R2.ItemStack nmsItem = this.getEquipment(EnumItemSlot.MAINHAND);
        NBTTagCompound tag = nmsItem.getTag();
        DamageAPI.fireStaffProjectileMob((CraftLivingEntity) this.getBukkitEntity(), tag, (CraftLivingEntity) entity.getBukkitEntity());
    }

    private void clearGoalSelectors() {
        try {
            Field a = PathfinderGoalSelector.class.getDeclaredField("b");
            Field b = PathfinderGoalSelector.class.getDeclaredField("c");
            a.setAccessible(true);
            b.setAccessible(true);
            ((LinkedHashSet) a.get(this.goalSelector)).clear();
            ((LinkedHashSet) b.get(this.goalSelector)).clear();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}

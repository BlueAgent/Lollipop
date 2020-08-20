package owmii.lib.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class Stack {
    public static boolean isFull(ItemStack stack) {
        return stack.getCount() >= stack.getMaxStackSize();
    }

    public static boolean isNBTEqual(ItemStack stack, ItemStack stack1) {
        return getTagOrEmpty(stack).equals(getTagOrEmpty(stack1));
    }

    public static CompoundNBT getTagOrEmptyChild(ItemStack stack, String key) {
        CompoundNBT nbt = stack.getChildTag(key);
        return nbt != null ? nbt : new CompoundNBT();
    }

    public static CompoundNBT getTagOrEmpty(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        return nbt != null ? nbt : new CompoundNBT();
    }

    public static String path(ItemStack provider) {
        return location(provider).getPath();
    }

    public static String modId(ItemStack provider) {
        return location(provider).getNamespace();
    }

    public static ResourceLocation location(ItemStack stack) {
        return location(stack.getItem());
    }

    public static String path(IItemProvider provider) {
        return location(provider).getPath();
    }

    public static String modId(IItemProvider provider) {
        return location(provider).getNamespace();
    }

    @SuppressWarnings("ConstantConditions")
    public static ResourceLocation location(IItemProvider provider) {
        return ForgeRegistries.ITEMS.getKey(provider.asItem());
    }

    public static boolean orEquals(ItemStack stack, ItemStack... stacks) {
        for (ItemStack stack1 : stacks) {
            if (stack.equals(stack1)) {
                return true;
            }
        }
        return false;
    }

    public static void drop(Entity entity, ItemStack stack) {
        drop(entity.world, entity.getPositionVec().add(0.0D, 0.3D, 0.0D), stack);
    }

    public static void drop(World world, BlockPos pos, ItemStack stack) {
        drop(world, new Vector3d(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D), stack);
    }

    public static void drop(World world, Vector3d pos, ItemStack stack) {
        ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        entity.setPickupDelay(8);
        world.addEntity(entity);
    }
}

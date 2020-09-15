package owmii.lib.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import owmii.lib.block.AbstractEnergyBlock;
import owmii.lib.client.wiki.page.panel.InfoBox;
import owmii.lib.config.IConfigHolder;
import owmii.lib.config.IEnergyConfig;
import owmii.lib.logistics.TransferType;
import owmii.lib.logistics.energy.Energy;
import owmii.lib.registry.IVariant;

import javax.annotation.Nullable;

public class EnergyBlockItem<V extends IVariant<?>, C extends IEnergyConfig<V>, B extends AbstractEnergyBlock<V, C, B>> extends ItemBlock<V, B> implements IConfigHolder<V, C>, InfoBox.IInfoBoxHolder, IEnergyItemProvider {
    public EnergyBlockItem(B block, Properties builder, @Nullable ItemGroup group) {
        super(block, builder, group);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        long transfer = getConfig().getTransfer(getVariant());
        return new Energy.Item.Provider(stack, getConfig().getCapacity(getVariant()), getTransferType().canExtract ? transfer : 0, getTransferType().canReceive ? transfer : 0);
    }

    public TransferType getTransferType() {
        return getBlock().getTransferType();
    }

    @Override
    public C getConfig() {
        return getBlock().getConfig();
    }

    public V getVariant() {
        return getBlock().getVariant();
    }

    @Override
    public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
        return getBlock().getInfoBox(stack, box);
    }

    @Override
    public boolean isChargeable(ItemStack stack) {
        return getBlock().isChargeable(stack);
    }
}

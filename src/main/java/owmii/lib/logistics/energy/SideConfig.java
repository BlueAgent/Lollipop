package owmii.lib.logistics.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import owmii.lib.block.AbstractEnergyStorage;
import owmii.lib.logistics.Transfer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static owmii.lib.logistics.Transfer.ALL;
import static owmii.lib.logistics.Transfer.NONE;

public class SideConfig {
    private final Transfer[] transfers = new Transfer[6];
    private final AbstractEnergyStorage storage;
    private boolean isSetFromNBT;

    public SideConfig(AbstractEnergyStorage storage) {
        this.storage = storage;
        Arrays.fill(this.transfers, NONE);
    }

    public void init() {
        if (!this.isSetFromNBT) {
            for (Direction side : Direction.values()) {
                setType(side, this.storage.getTransferType());
            }
        }
    }

    public void read(CompoundNBT nbt) {
        if (nbt.contains("side_transfer_type", Constants.NBT.TAG_INT_ARRAY)) {
            int[] arr = nbt.getIntArray("side_transfer_type");
            for (int i = 0; i < arr.length; i++) {
                this.transfers[i] = Transfer.values()[arr[i]];
            }
            this.isSetFromNBT = true;
        }
    }

    public CompoundNBT write(CompoundNBT nbt) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0, valuesLength = this.transfers.length; i < valuesLength; i++) {
            list.add(i, this.transfers[i].ordinal());
        }
        nbt.putIntArray("side_transfer_type", list);
        return nbt;
    }

    public void nextTypeAll() {
        if (isAllEquals()) {
            for (Direction side : Direction.values()) {
                nextType(side);
            }
        } else {
            for (Direction side : Direction.values()) {
                setType(side, ALL);
            }
        }
    }

    public boolean isAllEquals() {
        boolean flag = true;
        int first = -1;
        for (int i = 1; i < 6; i++) {
            if (this.storage.isEnergyPresent(Direction.byIndex(i))) {
                if (first < 0) {
                    first = this.transfers[i].ordinal();
                } else if (this.transfers[i].ordinal() != first) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    public void nextType(@Nullable Direction side) {
        setType(side, getType(side).next(this.storage.getTransferType()));
    }

    public Transfer getType(@Nullable Direction side) {
        if (side != null) {
            return this.transfers[side.getIndex()];
        }
        return NONE;
    }

    public void setType(@Nullable Direction side, Transfer type) {
        if (side == null || this.storage.getTransferType().equals(NONE))
            return;
        if (!this.storage.isEnergyPresent(side))
            return;
        this.transfers[side.getIndex()] = type;
    }
}

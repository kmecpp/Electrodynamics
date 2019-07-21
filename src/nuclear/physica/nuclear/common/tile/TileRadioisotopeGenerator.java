package physica.nuclear.common.tile;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import physica.api.core.abstraction.AbstractionLayer;
import physica.api.core.electricity.IElectricityProvider;
import physica.api.core.inventory.IGuiInterface;
import physica.library.energy.ElectricityUtilities;
import physica.library.energy.base.Unit;
import physica.library.tile.TileBaseContainer;
import physica.library.util.OreDictionaryUtilities;
import physica.nuclear.client.gui.GuiRadioisotopeGenerator;
import physica.nuclear.common.inventory.ContainerRadioisotopeGenerator;

public class TileRadioisotopeGenerator extends TileBaseContainer implements IGuiInterface, IElectricityProvider {

	public static final int		SLOT_INPUT			= 0;
	private static final int[]	ACCESSIBLE_SLOTS	= new int[] { SLOT_INPUT };
	public int					generate;
	private TileEntity[]		cachedOutputs		= new TileEntity[2];

	@Override
	public boolean canConnectElectricity(ForgeDirection from)
	{
		return from.ordinal() < 2;
	}

	@Override
	public void updateServer(int ticks)
	{
		super.updateServer(ticks);
		generate = getStackInSlot(SLOT_INPUT) == null ? 0 : getStackInSlot(SLOT_INPUT).stackSize * (ElectricityUtilities.convertEnergy(640, Unit.WATT, Unit.RF) / getInventoryStackLimit());
		if (generate > 0)
		{
			int validReceivers = 0;
			for (int index = 0; index < cachedOutputs.length; index++)
			{
				TileEntity cachedOutput = cachedOutputs[index];
				ForgeDirection out = ForgeDirection.getOrientation(index);
				if (cachedOutput == null || cachedOutput.isInvalid())
				{
					cachedOutput = null;
					TileEntity outputTile = worldObj.getTileEntity(xCoord + out.offsetX, yCoord + out.offsetY, zCoord + out.offsetZ);
					if (AbstractionLayer.Electricity.isElectricReceiver(outputTile))
					{
						cachedOutputs[index] = outputTile;
					}
				}
				if (cachedOutput != null)
				{
					if (AbstractionLayer.Electricity.canConnectElectricity(cachedOutput, out.getOpposite()))
					{
						validReceivers++;
					}
				}
			}
			if (validReceivers > 0)
			{
				int index = 0;
				for (TileEntity cachedOutput : cachedOutputs)
				{
					ForgeDirection out = ForgeDirection.getOrientation(index);
					if (AbstractionLayer.Electricity.canConnectElectricity(cachedOutput, out.getOpposite()))
					{
						AbstractionLayer.Electricity.receiveElectricity(cachedOutput, out.getOpposite(), generate / validReceivers, false);
					}
					index++;
				}
			}
		}
	}

	@Override
	public void writeClientGuiPacket(List<Object> dataList, EntityPlayer player)
	{
		super.writeClientGuiPacket(dataList, player);
		dataList.add(generate);
	}

	@Override
	public void readClientGuiPacket(ByteBuf buf, EntityPlayer player)
	{
		super.readClientGuiPacket(buf, player);
		generate = buf.readInt();
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return OreDictionaryUtilities.isSameOre(stack, "oreUranium") || OreDictionaryUtilities.isSameOre(stack, "oreUraniumPhysica");
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side)
	{
		return side < 2 ? ACCESSIBLE_SLOTS : ACCESSIBLE_SLOTS_NONE;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack item, int side)
	{
		return side < 2 && isItemValidForSlot(slot, item);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack item, int side)
	{
		return side < 2;
	}

	@Override
	public int getSizeInventory()
	{
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiScreen getClientGuiElement(int id, EntityPlayer player)
	{
		return new GuiRadioisotopeGenerator(player, this);
	}

	@Override
	public Container getServerGuiElement(int id, EntityPlayer player)
	{
		return new ContainerRadioisotopeGenerator(player, this);
	}

	@Override
	public int getElectricityStored(ForgeDirection from)
	{
		return generate;
	}

	@Override
	public int extractElectricity(ForgeDirection from, int maxExtract, boolean simulate)
	{
		return from == getFacing().getOpposite() ? getElectricityStored(from) : 0;
	}

	@Override
	public int getElectricCapacity(ForgeDirection from)
	{
		return ElectricityUtilities.convertEnergy(6400, Unit.WATT, Unit.RF);
	}
}
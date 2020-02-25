package com.mrcrayfish.improvedwolves.entity.ai.goal;

import com.mrcrayfish.improvedwolves.common.entity.PlayerDataHandler;
import com.mrcrayfish.improvedwolves.common.entity.WolfHeldItemDataHandler;
import com.mrcrayfish.improvedwolves.init.ModMemoryModuleTypes;
import com.mrcrayfish.improvedwolves.network.PacketHandler;
import com.mrcrayfish.improvedwolves.network.message.MessageSyncHeldWolfItem;
import com.mrcrayfish.improvedwolves.util.InventoryUtil;
import com.mrcrayfish.improvedwolves.util.TileEntityUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.EnumSet;
import java.util.Optional;

/**
 * Author: MrCrayfish
 */
public class PutInChestGoal extends Goal
{
    private State state = State.FINISHED;
    private WolfEntity entity;
    private LivingEntity owner;
    private GlobalPos globalPos;
    private IInventory inventory;
    private TileEntity tileEntity;

    public PutInChestGoal(WolfEntity entity)
    {
        this.entity = entity;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean shouldExecute()
    {
        if(!this.entity.isTamed() || this.entity.isSitting() || !this.entity.getBrain().hasMemory(ModMemoryModuleTypes.CHEST))
        {
            return false;
        }
        LivingEntity owner = this.entity.getOwner();
        if(owner == null)
        {
            return false;
        }
        GlobalPos globalPos = this.entity.getBrain().getMemory(ModMemoryModuleTypes.CHEST).orElse(null);
        if(globalPos == null)
        {
            return false;
        }
        ItemStack heldItem = owner.getHeldItemMainhand();
        if(heldItem.isEmpty())
        {
            return false;
        }
        TileEntity tileEntity = this.getStorage(globalPos.getPos());
        if(tileEntity == null)
        {
            return false;
        }
        if(canInsertItemIntoStorage((IInventory) tileEntity, heldItem))
        {
            this.globalPos = globalPos;
            this.inventory = (IInventory) tileEntity;
            this.owner = owner;
            this.state = State.GET_ITEM_FROM_PLAYER;
            this.tileEntity = tileEntity;
            return true;
        }
        return false;
    }

    @Override
    public void tick()
    {
        switch(this.state)
        {
            case GET_ITEM_FROM_PLAYER:
                if(this.tileEntity.isRemoved())
                {
                    this.state = State.FINISHED;
                    return;
                }

                Optional<GlobalPos> optional = this.entity.getBrain().getMemory(ModMemoryModuleTypes.CHEST);
                if(!optional.isPresent())
                {
                    this.state = State.GIVE_ITEM_TO_PLAYER;
                    return;
                }

                if(!this.owner.isAlive())
                {
                    this.state = State.FINISHED;
                    return;
                }

                ItemStack heldItem = this.owner.getHeldItemMainhand();
                if(heldItem.isEmpty())
                {
                    this.state = State.FINISHED;
                    return;
                }

                if(this.entity.getDistance(this.owner) >= 2.0)
                {
                    this.entity.getLookController().setLookPositionWithEntity(this.owner, 30F, 30F);
                    if(!this.entity.getNavigator().tryMoveToEntityLiving(this.owner, 1))
                    {
                        this.state = State.FINISHED;
                        return;
                    }
                }
                else
                {
                    WolfHeldItemDataHandler.IWolfHeldItem handler = WolfHeldItemDataHandler.getHandler(this.entity);
                    if(handler != null)
                    {
                        int maxCount = getMaxInsertCount(this.inventory, heldItem);
                        ItemStack copy = heldItem.copy();
                        copy.setCount(Math.min(maxCount, copy.getCount()));
                        handler.setItemStack(copy);
                        PacketHandler.instance.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.entity), new MessageSyncHeldWolfItem(this.entity.getEntityId(), handler.getItemStack()));
                        heldItem.shrink(copy.getCount());
                        this.state = State.PUT_ITEM_IN_CHEST;
                        this.entity.world.playSound(null, this.entity.getPosX(), this.entity.getPosY() + this.entity.getEyeHeight(), this.entity.getPosZ(), SoundEvents.ENTITY_FOX_BITE, SoundCategory.NEUTRAL, 1.0F, 0.5F);
                        return;
                    }
                }
                this.state = State.FINISHED;
                break;
            case PUT_ITEM_IN_CHEST:
                if(this.tileEntity.isRemoved())
                {
                    this.state = State.GIVE_ITEM_TO_PLAYER;
                    return;
                }

                WolfHeldItemDataHandler.IWolfHeldItem handler = WolfHeldItemDataHandler.getHandler(this.entity);
                if(handler != null)
                {
                    ItemStack stack = handler.getItemStack();
                    if(!stack.isEmpty())
                    {
                        Optional<GlobalPos> optional2 = this.entity.getBrain().getMemory(ModMemoryModuleTypes.CHEST);
                        if(!optional2.isPresent())
                        {
                            this.entity.getNavigator().clearPath();
                            this.state = State.GIVE_ITEM_TO_PLAYER;
                            return;
                        }

                        this.updateTargetChest(stack);

                        if(canInsertItemIntoStorage(this.inventory, stack))
                        {
                            if(!this.globalPos.getPos().withinDistance(this.entity.getPositionVec(), 1.5))
                            {
                                this.entity.getLookController().setLookPosition(this.globalPos.getPos().getX() + 0.5, this.globalPos.getPos().getY() + 0.5, this.globalPos.getPos().getZ() + 0.5);
                                if(!this.entity.getNavigator().tryMoveToXYZ(this.globalPos.getPos().getX() + 0.5, this.globalPos.getPos().getY(), this.globalPos.getPos().getZ() + 0.5, 1.0))
                                {
                                    this.state = State.GIVE_ITEM_TO_PLAYER;
                                    return;
                                }
                                return;
                            }
                            else
                            {
                                ItemStack copy = stack.copy();
                                addItemStackToInventory(this.inventory, copy);
                                if(!copy.isEmpty())
                                {
                                    this.state = State.GIVE_ITEM_TO_PLAYER;
                                }
                                else
                                {
                                    this.state = State.MOVE_TO_PLAYER;
                                }
                                handler.setItemStack(copy);
                                PacketHandler.instance.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.entity), new MessageSyncHeldWolfItem(this.entity.getEntityId(), handler.getItemStack()));
                                this.entity.getBrain().removeMemory(ModMemoryModuleTypes.CHEST);
                                this.entity.world.playSound(null, this.entity.getPosX(), this.entity.getPosY() + this.entity.getEyeHeight(), this.entity.getPosZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 1.0F, 0.5F);
                                if(this.inventory instanceof TileEntity)
                                {
                                    TileEntityUtil.sendUpdatePacket((TileEntity) this.inventory);
                                }
                                return;
                            }
                        }
                        else
                        {
                            this.state = State.GIVE_ITEM_TO_PLAYER;
                            return;
                        }
                    }
                }
                this.state = State.FINISHED;
                break;
            case GIVE_ITEM_TO_PLAYER:
                WolfHeldItemDataHandler.IWolfHeldItem handler2 = WolfHeldItemDataHandler.getHandler(this.entity);
                if(handler2 != null)
                {
                    ItemStack stack = handler2.getItemStack();
                    if(!stack.isEmpty())
                    {
                        if(!this.owner.isAlive())
                        {
                            this.state = State.FINISHED;
                            PacketHandler.instance.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.entity), new MessageSyncHeldWolfItem(this.entity.getEntityId(), handler2.getItemStack()));
                            handler2.setItemStack(ItemStack.EMPTY);
                            this.entity.getEntityWorld().addEntity(new ItemEntity(this.entity.world, this.entity.getPosX(), this.entity.getPosY(), this.entity.getPosZ(), stack.copy()));
                            return;
                        }

                        this.updateTargetChest(stack);

                        Optional<GlobalPos> optional2 = this.entity.getBrain().getMemory(ModMemoryModuleTypes.CHEST);
                        if(optional2.isPresent() && canInsertItemIntoStorage(this.inventory, stack))
                        {
                            this.state = State.PUT_ITEM_IN_CHEST;
                            return;
                        }
                        else if(this.entity.getDistance(this.owner) > 2.0)
                        {
                            this.entity.getLookController().setLookPositionWithEntity(this.owner, 30F, 30F);
                            if(this.entity.getNavigator().tryMoveToEntityLiving(this.owner, 1))
                            {
                                if(this.entity.getDistanceSq(this.owner) >= 144.0D)
                                {
                                    this.moveToPlayer();
                                }
                                return;
                            }
                            return;
                        }
                        else
                        {
                            if(this.owner instanceof PlayerEntity)
                            {
                                PlayerInventory playerInventory = ((PlayerEntity) this.owner).inventory;
                                playerInventory.placeItemBackInInventory(this.owner.world, stack);
                                PacketHandler.instance.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.entity), new MessageSyncHeldWolfItem(this.entity.getEntityId(), handler2.getItemStack()));
                                handler2.setItemStack(ItemStack.EMPTY);
                                if(this.owner instanceof PlayerEntity)
                                {
                                    PlayerDataHandler.IPlayerData playerDataHandler = PlayerDataHandler.getHandler((PlayerEntity) this.owner);
                                    if(playerDataHandler != null)
                                    {
                                        playerDataHandler.setCommandingWolf(null);
                                    }
                                }
                                this.state = State.FINISHED;
                                this.entity.world.playSound(null, this.entity.getPosX(), this.entity.getPosY() + this.entity.getEyeHeight(), this.entity.getPosZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                                return;
                            }
                        }
                    }
                }
                this.state = State.FINISHED;
                break;
            case MOVE_TO_PLAYER:
                if(this.entity.getDistance(this.owner) > 2.0)
                {
                    this.entity.getLookController().setLookPositionWithEntity(this.owner, 30F, 30F);
                    if(!this.entity.getNavigator().tryMoveToEntityLiving(this.owner, 1))
                    {
                        if(this.entity.getDistanceSq(this.owner) >= 144.0D)
                        {
                            this.moveToPlayer();
                        }
                    }
                }
                else
                {
                    Optional<GlobalPos> optional3 = this.entity.getBrain().getMemory(ModMemoryModuleTypes.CHEST);
                    if(optional3.isPresent())
                    {
                        ItemStack heldItem2 = this.owner.getHeldItemMainhand();
                        if(!heldItem2.isEmpty())
                        {
                            this.updateTargetChest(heldItem2);
                            if(canInsertItemIntoStorage(this.inventory, heldItem2))
                            {
                                this.state = State.PUT_ITEM_IN_CHEST;
                                return;
                            }
                        }
                    }
                    this.state = State.FINISHED;
                }
                break;
            case FINISHED:
                WolfHeldItemDataHandler.IWolfHeldItem handler1 = WolfHeldItemDataHandler.getHandler(this.entity);
                if(handler1 != null)
                {
                    ItemStack stack = handler1.getItemStack();
                    if(!stack.isEmpty())
                    {

                    }
                }
                break;
        }
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return this.state != State.FINISHED && this.owner.isAlive();
    }

    @Override
    public void resetTask()
    {
        this.state = State.FINISHED;
        if(this.owner instanceof PlayerEntity)
        {
            PlayerDataHandler.IPlayerData playerDataHandler = PlayerDataHandler.getHandler((PlayerEntity) this.owner);
            if(playerDataHandler != null)
            {
                playerDataHandler.setCommandingWolf(null);
            }
        }
        this.owner = null;
        this.inventory = null;
        this.globalPos = null;
        this.tileEntity = null;
    }

    private TileEntity getStorage(BlockPos pos)
    {
        TileEntity tileEntity = this.entity.getEntityWorld().getTileEntity(pos);
        if(tileEntity instanceof IInventory)
        {
           return tileEntity;
        }
        return null;
    }

    public static boolean canInsertItemIntoStorage(IInventory inventory, ItemStack stack) //TODO test this
    {
        for(int i = 0; i < inventory.getSizeInventory(); i++)
        {
            if(inventory.isItemValidForSlot(i, stack))
            {
                ItemStack slotStack = inventory.getStackInSlot(i);
                if(slotStack.isEmpty())
                {
                    return true;
                }
                if(InventoryUtil.areItemStackEqualIgnoreCount(slotStack, stack))
                {
                    if(slotStack.getCount() < slotStack.getMaxStackSize())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static int getMaxInsertCount(IInventory inventory, ItemStack stack)
    {
        int count = 0;
        for(int i = 0; i < inventory.getSizeInventory(); i++)
        {
            if(inventory.isItemValidForSlot(i, stack))
            {
                ItemStack slotStack = inventory.getStackInSlot(i);
                if(slotStack.isEmpty())
                {
                    count += stack.getMaxStackSize();
                }
                if(InventoryUtil.areItemStackEqualIgnoreCount(slotStack, stack))
                {
                    count += slotStack.getMaxStackSize() - slotStack.getCount();
                }
            }
        }
        return MathHelper.clamp(count, 0, 64);
    }

    public static void addItemStackToInventory(IInventory inventory, ItemStack stack)
    {
        for(int i = 0; i < inventory.getSizeInventory(); i++)
        {
            if(stack.isEmpty())
            {
                return;
            }
            if(inventory.isItemValidForSlot(i, stack))
            {
                ItemStack slotStack = inventory.getStackInSlot(i);
                if(slotStack.isEmpty())
                {
                    inventory.setInventorySlotContents(i, stack.copy());
                    stack.setCount(0);
                    return;
                }
                if(InventoryUtil.areItemStackEqualIgnoreCount(slotStack, stack))
                {
                    if(slotStack.getCount() < slotStack.getMaxStackSize())
                    {
                        int deltaCount = slotStack.getMaxStackSize() - slotStack.getCount();
                        int minCount = Math.min(deltaCount, stack.getCount());
                        slotStack.grow(minCount);
                        stack.shrink(minCount);
                    }
                }
            }
        }
    }

    private void updateTargetChest(ItemStack stack)
    {
        Optional<GlobalPos> optional = this.entity.getBrain().getMemory(ModMemoryModuleTypes.CHEST);
        if(optional.isPresent())
        {
            GlobalPos newPos = optional.get();
            if(!newPos.equals(this.globalPos))
            {
                TileEntity tileEntity = this.getStorage(newPos.getPos());
                if(tileEntity != null)
                {
                    if(canInsertItemIntoStorage((IInventory) tileEntity, stack))
                    {
                        this.globalPos = newPos;
                        this.inventory = (IInventory) tileEntity;
                        this.tileEntity = tileEntity;
                    }
                }
            }
        }
    }

    private void moveToPlayer()
    {
        BlockPos pos = new BlockPos(this.owner);
        for(int i = 0; i < 10; i++)
        {
            int offsetX = this.getRandomOffset(-3, 3);
            int offsetY = this.getRandomOffset(-1, 1);
            int offsetZ = this.getRandomOffset(-3, 3);
            boolean moved = this.moveToPlayer(pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ);
            if(moved)
            {
                return;
            }
        }
    }

    private int getRandomOffset(int min, int max)
    {
        return this.entity.getRNG().nextInt(max - min + 1) + min;
    }

    private boolean moveToPlayer(int posX, int posY, int posZ)
    {
        if(Math.abs((double) posX - this.owner.getPosX()) < 2.0D && Math.abs((double) posZ - this.owner.getPosZ()) < 2.0D)
        {
            return false;
        }
        else if(!this.isValidPos(new BlockPos(posX, posY, posZ)))
        {
            return false;
        }
        else
        {
            this.entity.setLocationAndAngles((double) ((float) posX + 0.5F), (double) posY, (double) ((float) posZ + 0.5F), this.entity.rotationYaw, this.entity.rotationPitch);
            this.entity.getNavigator().clearPath();
            return true;
        }
    }

    private boolean isValidPos(BlockPos pos)
    {
        PathNodeType pathNodeType = WalkNodeProcessor.func_227480_b_(this.entity.world, pos.getX(), pos.getY(), pos.getZ());
        if(pathNodeType != PathNodeType.WALKABLE)
        {
            return false;
        }

        BlockState state = this.entity.world.getBlockState(pos.down());
        if(state.getBlock() instanceof LeavesBlock)
        {
            return false;
        }
        else
        {
            BlockPos entityPos = pos.subtract(new BlockPos(this.entity));
            return this.entity.world.func_226665_a__(this.entity, this.entity.getBoundingBox().offset(entityPos));
        }
    }

    public enum State
    {
        GET_ITEM_FROM_PLAYER,
        PUT_ITEM_IN_CHEST,
        GIVE_ITEM_TO_PLAYER,
        MOVE_TO_PLAYER,
        FINISHED;
    }
}

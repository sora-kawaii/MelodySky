/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.fakemc;

import java.io.File;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenPlains;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import xyz.Melody.Utils.fakemc.FakeNetHandlerPlayClient;

public final class FakeWorld
extends WorldClient {
    public FakeWorld(WorldSettings worldSettings, FakeNetHandlerPlayClient netHandler) {
        super(netHandler, worldSettings, 0, EnumDifficulty.HARD, new Profiler());
        this.provider.registerWorld(this);
    }

    @Override
    protected boolean isChunkLoaded(int i, int i1, boolean b) {
        return false;
    }

    @Override
    public BlockPos getTopSolidOrLiquidBlock(BlockPos pos) {
        return new BlockPos(pos.getX(), 63, pos.getZ());
    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return pos.getY() > 63;
    }

    @Override
    public boolean setBlockState(BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public boolean setBlockToAir(BlockPos pos) {
        return true;
    }

    @Override
    public void markChunkDirty(BlockPos pos, TileEntity unusedTileEntity) {
    }

    public void notifyBlockUpdate(BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropBlock) {
        return this.isAirBlock(pos);
    }

    @Override
    public void notifyNeighborsOfStateExcept(BlockPos pos, Block blockType, EnumFacing skipSide) {
    }

    public void notifyNeighborsRespectDebug(BlockPos pos, Block blockType, boolean p_175722_3_) {
    }

    public void markAndNotifyBlock(BlockPos pos, Chunk chunk, IBlockState iblockstate, IBlockState newState, int flags) {
    }

    @Override
    public void markBlocksDirtyVertical(int par1, int par2, int par3, int par4) {
    }

    @Override
    public void markBlockRangeForRenderUpdate(int p_147458_1_, int p_147458_2_, int p_147458_3_, int p_147458_4_, int p_147458_5_, int p_147458_6_) {
    }

    @Override
    public boolean isBlockTickPending(BlockPos pos, Block blockType) {
        return false;
    }

    @Override
    public int getLightFromNeighbors(BlockPos pos) {
        return 14;
    }

    @Override
    public int getLight(BlockPos pos, boolean checkNeighbors) {
        return 14;
    }

    @Override
    public int getLight(BlockPos pos) {
        return 14;
    }

    @Override
    public int getLightFor(EnumSkyBlock type, BlockPos pos) {
        return 14;
    }

    @Override
    public int getLightFromNeighborsFor(EnumSkyBlock type, BlockPos pos) {
        return 14;
    }

    @Override
    public boolean canBlockSeeSky(BlockPos pos) {
        return pos.getY() > 62;
    }

    @Override
    public BlockPos getHeight(BlockPos pos) {
        return new BlockPos(pos.getX(), 63, pos.getZ());
    }

    @Override
    public int getChunksLowestHorizon(int x, int z) {
        return 63;
    }

    @Override
    protected void updateBlocks() {
    }

    @Override
    public void markBlockRangeForRenderUpdate(BlockPos rangeMin, BlockPos rangeMax) {
    }

    @Override
    public void setLightFor(EnumSkyBlock type, BlockPos pos, int lightValue) {
    }

    @Override
    public float getLightBrightness(BlockPos pos) {
        return 1.0f;
    }

    public float getSunBrightnessFactor(float p_72967_1_) {
        return 1.0f;
    }

    @Override
    public float getSunBrightness(float p_72971_1_) {
        return 1.0f;
    }

    public float getSunBrightnessBody(float p_72971_1_) {
        return 1.0f;
    }

    @Override
    public boolean isDaytime() {
        return true;
    }

    @Override
    public boolean addWeatherEffect(Entity par1Entity) {
        return false;
    }

    @Override
    public boolean spawnEntityInWorld(Entity par1Entity) {
        return false;
    }

    @Override
    public void onEntityAdded(Entity par1Entity) {
    }

    @Override
    public void onEntityRemoved(Entity par1Entity) {
    }

    @Override
    public void removeEntity(Entity par1Entity) {
    }

    public void removeEntityDangerously(Entity entityIn) {
    }

    @Override
    public int calculateSkylightSubtracted(float par1) {
        return 6;
    }

    @Override
    public void scheduleBlockUpdate(BlockPos pos, Block blockIn, int delay, int priority) {
    }

    @Override
    public void updateEntities() {
    }

    @Override
    public void updateEntityWithOptionalForce(Entity entityIn, boolean forceUpdate) {
        if (forceUpdate) {
            ++entityIn.ticksExisted;
        }
    }

    @Override
    public boolean checkNoEntityCollision(AxisAlignedBB bb) {
        return true;
    }

    @Override
    public boolean checkNoEntityCollision(AxisAlignedBB bb, Entity entityIn) {
        return true;
    }

    @Override
    public boolean checkBlockCollision(AxisAlignedBB bb) {
        return false;
    }

    public boolean containsAnyLiquid(AxisAlignedBB bb) {
        return false;
    }

    @Override
    public boolean handleMaterialAcceleration(AxisAlignedBB par1AxisAlignedBB, Material par2Material, Entity par3Entity) {
        return false;
    }

    @Override
    public boolean isMaterialInBB(AxisAlignedBB par1AxisAlignedBB, Material par2Material) {
        return false;
    }

    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return null;
    }

    @Override
    public boolean extinguishFire(EntityPlayer player, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public String getDebugLoadedEntities() {
        return "";
    }

    @Override
    public String getProviderName() {
        return "";
    }

    @Override
    public void setTileEntity(BlockPos pos, TileEntity tileEntityIn) {
    }

    @Override
    public void removeTileEntity(BlockPos pos) {
    }

    @Override
    public void markTileEntityForRemoval(TileEntity p_147457_1_) {
    }

    @Override
    public boolean isBlockNormalCube(BlockPos pos, boolean _default) {
        return true;
    }

    @Override
    public void tick() {
    }

    @Override
    protected void updateWeather() {
    }

    public void updateWeatherBody() {
    }

    @Override
    public boolean canBlockFreezeWater(BlockPos pos) {
        return false;
    }

    @Override
    public boolean canBlockFreezeNoWater(BlockPos pos) {
        return false;
    }

    @Override
    public boolean canBlockFreeze(BlockPos pos, boolean noWaterAdj) {
        return false;
    }

    public boolean canBlockFreezeBody(BlockPos pos, boolean noWaterAdj) {
        return false;
    }

    @Override
    public boolean canSnowAt(BlockPos pos, boolean checkLight) {
        return false;
    }

    public boolean canSnowAtBody(BlockPos pos, boolean checkLight) {
        return false;
    }

    @Override
    public boolean tickUpdates(boolean par1) {
        return false;
    }

    public List getPendingBlockUpdates(Chunk par1Chunk, boolean par2) {
        return null;
    }

    public Entity findNearestEntityWithinAABB(Class par1Class, AxisAlignedBB par2AxisAlignedBB, Entity par3Entity) {
        return null;
    }

    public int countEntities(Class par1Class) {
        return 0;
    }

    @Override
    public int getStrongPower(BlockPos pos) {
        return 0;
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction) {
        return 0;
    }

    @Override
    public boolean isSidePowered(BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public int getRedstonePower(BlockPos pos, EnumFacing facing) {
        return 0;
    }

    @Override
    public boolean isBlockPowered(BlockPos pos) {
        return false;
    }

    @Override
    public int isBlockIndirectlyGettingPowered(BlockPos pos) {
        return 0;
    }

    @Override
    public void checkSessionLock() throws MinecraftException {
    }

    @Override
    public long getSeed() {
        return 1L;
    }

    @Override
    public long getTotalWorldTime() {
        return 1L;
    }

    @Override
    public long getWorldTime() {
        return 1L;
    }

    @Override
    public void setWorldTime(long par1) {
    }

    @Override
    public BlockPos getSpawnPoint() {
        return new BlockPos(0, 64, 0);
    }

    @Override
    public void joinEntityInSurroundings(Entity par1Entity) {
    }

    @Override
    public boolean canSeeSky(BlockPos pos) {
        return pos.getY() > 62;
    }

    public boolean canMineBlockBody(EntityPlayer player, BlockPos pos) {
        return false;
    }

    @Override
    public void setEntityState(Entity par1Entity, byte par2) {
    }

    @Override
    public float getThunderStrength(float delta) {
        return 0.0f;
    }

    @Override
    public void addBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam) {
    }

    @Override
    public void updateAllPlayersSleepingFlag() {
    }

    @Override
    public boolean isRainingAt(BlockPos strikePosition) {
        return false;
    }

    @Override
    public void setThunderStrength(float p_147442_1_) {
    }

    @Override
    public float getRainStrength(float par1) {
        return 0.0f;
    }

    @Override
    public void setRainStrength(float par1) {
    }

    @Override
    public boolean isThundering() {
        return false;
    }

    @Override
    public boolean isRaining() {
        return false;
    }

    @Override
    public boolean isBlockinHighHumidity(BlockPos pos) {
        return false;
    }

    @Override
    public void setItemData(String dataID, WorldSavedData worldSavedDataIn) {
    }

    @Override
    public void playBroadcastSound(int p_175669_1_, BlockPos pos, int p_175669_3_) {
    }

    public void playEvent(EntityPlayer player, int type, BlockPos pos, int data) {
    }

    public void playEvent(int type, BlockPos pos, int data) {
    }

    @Override
    public int getHeight() {
        return 256;
    }

    @Override
    public int getActualHeight() {
        return 256;
    }

    @Override
    public void makeFireworks(double par1, double par3, double par5, double par7, double par9, double par11, NBTTagCompound par13nbtTagCompound) {
    }

    @Override
    public boolean addTileEntity(TileEntity tile) {
        return true;
    }

    public boolean isSideSolid(BlockPos pos, EnumFacing side) {
        return pos.getY() <= 63;
    }

    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
        return pos.getY() <= 63;
    }

    public int countEntities(EnumCreatureType type, boolean forSpawnCount) {
        return 0;
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        return new FakeChunkProvider();
    }

    @Override
    public Chunk getChunkFromChunkCoords(int par1, int par2) {
        return null;
    }

    protected static class FakeChunkProvider
    implements IChunkProvider {
        protected FakeChunkProvider() {
        }

        public Chunk getLoadedChunk(int x, int z) {
            return null;
        }

        @Override
        public Chunk provideChunk(int var1, int var2) {
            return null;
        }

        @Override
        public String makeString() {
            return null;
        }

        @Override
        public boolean unloadQueuedChunks() {
            return false;
        }

        public boolean func_191062_e(int p_191062_1_, int p_191062_2_) {
            return true;
        }

        @Override
        public boolean chunkExists(int var1, int var2) {
            return false;
        }

        @Override
        public void populate(IChunkProvider var1, int var2, int var3) {
        }

        @Override
        public boolean populateChunk(IChunkProvider var1, Chunk var2, int var3, int var4) {
            return false;
        }

        @Override
        public boolean saveChunks(boolean var1, IProgressUpdate var2) {
            return false;
        }

        @Override
        public boolean canSave() {
            return false;
        }

        @Override
        public int getLoadedChunkCount() {
            return 0;
        }

        @Override
        public void saveExtraData() {
        }

        @Override
        public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType arg0, BlockPos arg1) {
            return null;
        }

        @Override
        public BlockPos getStrongholdGen(World arg0, String arg1, BlockPos arg2) {
            return null;
        }

        @Override
        public Chunk provideChunk(BlockPos arg0) {
            return null;
        }

        @Override
        public void recreateStructures(Chunk arg0, int arg1, int arg2) {
        }
    }

    protected static class FakeSaveHandler
    implements ISaveHandler {
        protected FakeSaveHandler() {
        }

        @Override
        public WorldInfo loadWorldInfo() {
            return null;
        }

        @Override
        public void checkSessionLock() {
        }

        @Override
        public IChunkLoader getChunkLoader(WorldProvider var1) {
            return null;
        }

        @Override
        public void saveWorldInfoWithPlayer(WorldInfo var1, NBTTagCompound var2) {
        }

        @Override
        public void saveWorldInfo(WorldInfo var1) {
        }

        @Override
        public IPlayerFileData getPlayerNBTManager() {
            return null;
        }

        @Override
        public void flush() {
        }

        @Override
        public File getWorldDirectory() {
            return null;
        }

        @Override
        public File getMapFileFromName(String var1) {
            return null;
        }

        @Override
        public String getWorldDirectoryName() {
            return null;
        }
    }

    protected static class FakeWorldProvider
    extends WorldProvider {
        protected FakeWorldProvider() {
        }

        @Override
        public boolean isSurfaceWorld() {
            return true;
        }

        @Override
        public boolean canRespawnHere() {
            return true;
        }

        @Override
        public int getAverageGroundLevel() {
            return 63;
        }

        @Override
        public boolean doesXZShowFog(int par1, int par2) {
            return false;
        }

        public void setDimension(int dim) {
        }

        public String getSaveFolder() {
            return null;
        }

        public BlockPos getRandomizedSpawnPoint() {
            return new BlockPos(0, 64, 0);
        }

        public boolean shouldMapSpin(String entity, double x, double y, double z) {
            return false;
        }

        public int getRespawnDimension(EntityPlayerMP player) {
            return 0;
        }

        public BiomeGenBase getBiomeForCoords(BlockPos pos) {
            return new BiomeGenPlains(0);
        }

        public boolean isDaytime() {
            return true;
        }

        public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful) {
        }

        public void calculateInitialWeather() {
        }

        public void updateWeather() {
        }

        public boolean canBlockFreeze(BlockPos pos, boolean byWater) {
            return false;
        }

        public boolean canSnowAt(BlockPos pos, boolean checkLight) {
            return false;
        }

        public long getSeed() {
            return 1L;
        }

        public long getWorldTime() {
            return 1L;
        }

        public void setWorldTime(long time) {
        }

        public boolean canMineBlock(EntityPlayer player, BlockPos pos) {
            return false;
        }

        public boolean isBlockHighHumidity(BlockPos pos) {
            return false;
        }

        public int getHeight() {
            return 256;
        }

        public int getActualHeight() {
            return 256;
        }

        public void resetRainAndThunder() {
        }

        public boolean canDoLightning(Chunk chunk) {
            return false;
        }

        public boolean canDoRainSnowIce(Chunk chunk) {
            return false;
        }

        public BlockPos getSpawnPoint() {
            return new BlockPos(0, 64, 0);
        }

        @Override
        public boolean canCoordinateBeSpawn(int par1, int par2) {
            return true;
        }

        @Override
        public String getDimensionName() {
            return null;
        }

        @Override
        public String getInternalNameSuffix() {
            return null;
        }
    }
}


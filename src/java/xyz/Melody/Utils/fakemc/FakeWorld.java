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
    public FakeWorld(WorldSettings worldSettings, FakeNetHandlerPlayClient fakeNetHandlerPlayClient) {
        super(fakeNetHandlerPlayClient, worldSettings, 0, EnumDifficulty.HARD, new Profiler());
        this.provider.registerWorld(this);
    }

    @Override
    protected boolean isChunkLoaded(int n, int n2, boolean bl) {
        return false;
    }

    @Override
    public BlockPos getTopSolidOrLiquidBlock(BlockPos blockPos) {
        return new BlockPos(blockPos.getX(), 63, blockPos.getZ());
    }

    @Override
    public boolean isAirBlock(BlockPos blockPos) {
        return blockPos.getY() > 63;
    }

    @Override
    public boolean setBlockState(BlockPos blockPos, IBlockState iBlockState) {
        return true;
    }

    @Override
    public boolean setBlockToAir(BlockPos blockPos) {
        return true;
    }

    @Override
    public void markChunkDirty(BlockPos blockPos, TileEntity tileEntity) {
    }

    public void notifyBlockUpdate(BlockPos blockPos, IBlockState iBlockState, IBlockState iBlockState2, int n) {
    }

    @Override
    public boolean destroyBlock(BlockPos blockPos, boolean bl) {
        return this.isAirBlock(blockPos);
    }

    @Override
    public void notifyNeighborsOfStateExcept(BlockPos blockPos, Block block, EnumFacing enumFacing) {
    }

    public void notifyNeighborsRespectDebug(BlockPos blockPos, Block block, boolean bl) {
    }

    public void markAndNotifyBlock(BlockPos blockPos, Chunk chunk, IBlockState iBlockState, IBlockState iBlockState2, int n) {
    }

    @Override
    public void markBlocksDirtyVertical(int n, int n2, int n3, int n4) {
    }

    @Override
    public void markBlockRangeForRenderUpdate(int n, int n2, int n3, int n4, int n5, int n6) {
    }

    @Override
    public boolean isBlockTickPending(BlockPos blockPos, Block block) {
        return false;
    }

    @Override
    public int getLightFromNeighbors(BlockPos blockPos) {
        return 14;
    }

    @Override
    public int getLight(BlockPos blockPos, boolean bl) {
        return 14;
    }

    @Override
    public int getLight(BlockPos blockPos) {
        return 14;
    }

    @Override
    public int getLightFor(EnumSkyBlock enumSkyBlock, BlockPos blockPos) {
        return 14;
    }

    @Override
    public int getLightFromNeighborsFor(EnumSkyBlock enumSkyBlock, BlockPos blockPos) {
        return 14;
    }

    @Override
    public boolean canBlockSeeSky(BlockPos blockPos) {
        return blockPos.getY() > 62;
    }

    @Override
    public BlockPos getHeight(BlockPos blockPos) {
        return new BlockPos(blockPos.getX(), 63, blockPos.getZ());
    }

    @Override
    public int getChunksLowestHorizon(int n, int n2) {
        return 63;
    }

    @Override
    protected void updateBlocks() {
    }

    @Override
    public void markBlockRangeForRenderUpdate(BlockPos blockPos, BlockPos blockPos2) {
    }

    @Override
    public void setLightFor(EnumSkyBlock enumSkyBlock, BlockPos blockPos, int n) {
    }

    @Override
    public float getLightBrightness(BlockPos blockPos) {
        return 1.0f;
    }

    public float getSunBrightnessFactor(float f) {
        return 1.0f;
    }

    @Override
    public float getSunBrightness(float f) {
        return 1.0f;
    }

    public float getSunBrightnessBody(float f) {
        return 1.0f;
    }

    @Override
    public boolean isDaytime() {
        return true;
    }

    @Override
    public boolean addWeatherEffect(Entity entity) {
        return false;
    }

    @Override
    public boolean spawnEntityInWorld(Entity entity) {
        return false;
    }

    @Override
    public void onEntityAdded(Entity entity) {
    }

    @Override
    public void onEntityRemoved(Entity entity) {
    }

    @Override
    public void removeEntity(Entity entity) {
    }

    public void removeEntityDangerously(Entity entity) {
    }

    @Override
    public int calculateSkylightSubtracted(float f) {
        return 6;
    }

    @Override
    public void scheduleBlockUpdate(BlockPos blockPos, Block block, int n, int n2) {
    }

    @Override
    public void updateEntities() {
    }

    @Override
    public void updateEntityWithOptionalForce(Entity entity, boolean bl) {
        if (bl) {
            ++entity.ticksExisted;
        }
    }

    @Override
    public boolean checkNoEntityCollision(AxisAlignedBB axisAlignedBB) {
        return true;
    }

    @Override
    public boolean checkNoEntityCollision(AxisAlignedBB axisAlignedBB, Entity entity) {
        return true;
    }

    @Override
    public boolean checkBlockCollision(AxisAlignedBB axisAlignedBB) {
        return false;
    }

    public boolean containsAnyLiquid(AxisAlignedBB axisAlignedBB) {
        return false;
    }

    @Override
    public boolean handleMaterialAcceleration(AxisAlignedBB axisAlignedBB, Material material, Entity entity) {
        return false;
    }

    @Override
    public boolean isMaterialInBB(AxisAlignedBB axisAlignedBB, Material material) {
        return false;
    }

    @Override
    public TileEntity getTileEntity(BlockPos blockPos) {
        return null;
    }

    @Override
    public boolean extinguishFire(EntityPlayer entityPlayer, BlockPos blockPos, EnumFacing enumFacing) {
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
    public void setTileEntity(BlockPos blockPos, TileEntity tileEntity) {
    }

    @Override
    public void removeTileEntity(BlockPos blockPos) {
    }

    @Override
    public void markTileEntityForRemoval(TileEntity tileEntity) {
    }

    @Override
    public boolean isBlockNormalCube(BlockPos blockPos, boolean bl) {
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
    public boolean canBlockFreezeWater(BlockPos blockPos) {
        return false;
    }

    @Override
    public boolean canBlockFreezeNoWater(BlockPos blockPos) {
        return false;
    }

    @Override
    public boolean canBlockFreeze(BlockPos blockPos, boolean bl) {
        return false;
    }

    public boolean canBlockFreezeBody(BlockPos blockPos, boolean bl) {
        return false;
    }

    @Override
    public boolean canSnowAt(BlockPos blockPos, boolean bl) {
        return false;
    }

    public boolean canSnowAtBody(BlockPos blockPos, boolean bl) {
        return false;
    }

    @Override
    public boolean tickUpdates(boolean bl) {
        return false;
    }

    public List getPendingBlockUpdates(Chunk chunk, boolean bl) {
        return null;
    }

    public Entity findNearestEntityWithinAABB(Class clazz, AxisAlignedBB axisAlignedBB, Entity entity) {
        return null;
    }

    public int countEntities(Class clazz) {
        return 0;
    }

    @Override
    public int getStrongPower(BlockPos blockPos) {
        return 0;
    }

    @Override
    public int getStrongPower(BlockPos blockPos, EnumFacing enumFacing) {
        return 0;
    }

    @Override
    public boolean isSidePowered(BlockPos blockPos, EnumFacing enumFacing) {
        return false;
    }

    @Override
    public int getRedstonePower(BlockPos blockPos, EnumFacing enumFacing) {
        return 0;
    }

    @Override
    public boolean isBlockPowered(BlockPos blockPos) {
        return false;
    }

    @Override
    public int isBlockIndirectlyGettingPowered(BlockPos blockPos) {
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
    public void setWorldTime(long l2) {
    }

    @Override
    public BlockPos getSpawnPoint() {
        return new BlockPos(0, 64, 0);
    }

    @Override
    public void joinEntityInSurroundings(Entity entity) {
    }

    @Override
    public boolean canSeeSky(BlockPos blockPos) {
        return blockPos.getY() > 62;
    }

    public boolean canMineBlockBody(EntityPlayer entityPlayer, BlockPos blockPos) {
        return false;
    }

    @Override
    public void setEntityState(Entity entity, byte by) {
    }

    @Override
    public float getThunderStrength(float f) {
        return 0.0f;
    }

    @Override
    public void addBlockEvent(BlockPos blockPos, Block block, int n, int n2) {
    }

    @Override
    public void updateAllPlayersSleepingFlag() {
    }

    @Override
    public boolean isRainingAt(BlockPos blockPos) {
        return false;
    }

    @Override
    public void setThunderStrength(float f) {
    }

    @Override
    public float getRainStrength(float f) {
        return 0.0f;
    }

    @Override
    public void setRainStrength(float f) {
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
    public boolean isBlockinHighHumidity(BlockPos blockPos) {
        return false;
    }

    @Override
    public void setItemData(String string, WorldSavedData worldSavedData) {
    }

    @Override
    public void playBroadcastSound(int n, BlockPos blockPos, int n2) {
    }

    public void playEvent(EntityPlayer entityPlayer, int n, BlockPos blockPos, int n2) {
    }

    public void playEvent(int n, BlockPos blockPos, int n2) {
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
    public void makeFireworks(double d, double d2, double d3, double d4, double d5, double d6, NBTTagCompound nBTTagCompound) {
    }

    @Override
    public boolean addTileEntity(TileEntity tileEntity) {
        return true;
    }

    public boolean isSideSolid(BlockPos blockPos, EnumFacing enumFacing) {
        return blockPos.getY() <= 63;
    }

    public boolean isSideSolid(BlockPos blockPos, EnumFacing enumFacing, boolean bl) {
        return blockPos.getY() <= 63;
    }

    public int countEntities(EnumCreatureType enumCreatureType, boolean bl) {
        return 0;
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        return new FakeChunkProvider();
    }

    @Override
    public Chunk getChunkFromChunkCoords(int n, int n2) {
        return null;
    }

    protected static class FakeChunkProvider
    implements IChunkProvider {
        protected FakeChunkProvider() {
        }

        public Chunk getLoadedChunk(int n, int n2) {
            return null;
        }

        @Override
        public Chunk provideChunk(int n, int n2) {
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

        public boolean func_191062_e(int n, int n2) {
            return true;
        }

        @Override
        public boolean chunkExists(int n, int n2) {
            return false;
        }

        @Override
        public void populate(IChunkProvider iChunkProvider, int n, int n2) {
        }

        @Override
        public boolean populateChunk(IChunkProvider iChunkProvider, Chunk chunk, int n, int n2) {
            return false;
        }

        @Override
        public boolean saveChunks(boolean bl, IProgressUpdate iProgressUpdate) {
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
        public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumCreatureType, BlockPos blockPos) {
            return null;
        }

        @Override
        public BlockPos getStrongholdGen(World world, String string, BlockPos blockPos) {
            return null;
        }

        @Override
        public Chunk provideChunk(BlockPos blockPos) {
            return null;
        }

        @Override
        public void recreateStructures(Chunk chunk, int n, int n2) {
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
        public IChunkLoader getChunkLoader(WorldProvider worldProvider) {
            return null;
        }

        @Override
        public void saveWorldInfoWithPlayer(WorldInfo worldInfo, NBTTagCompound nBTTagCompound) {
        }

        @Override
        public void saveWorldInfo(WorldInfo worldInfo) {
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
        public File getMapFileFromName(String string) {
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
        public boolean doesXZShowFog(int n, int n2) {
            return false;
        }

        public void setDimension(int n) {
        }

        public String getSaveFolder() {
            return null;
        }

        public BlockPos getRandomizedSpawnPoint() {
            return new BlockPos(0, 64, 0);
        }

        public boolean shouldMapSpin(String string, double d, double d2, double d3) {
            return false;
        }

        public int getRespawnDimension(EntityPlayerMP entityPlayerMP) {
            return 0;
        }

        public BiomeGenBase getBiomeForCoords(BlockPos blockPos) {
            return new BiomeGenPlains(0);
        }

        public boolean isDaytime() {
            return true;
        }

        public void setAllowedSpawnTypes(boolean bl, boolean bl2) {
        }

        public void calculateInitialWeather() {
        }

        public void updateWeather() {
        }

        public boolean canBlockFreeze(BlockPos blockPos, boolean bl) {
            return false;
        }

        public boolean canSnowAt(BlockPos blockPos, boolean bl) {
            return false;
        }

        public long getSeed() {
            return 1L;
        }

        public long getWorldTime() {
            return 1L;
        }

        public void setWorldTime(long l2) {
        }

        public boolean canMineBlock(EntityPlayer entityPlayer, BlockPos blockPos) {
            return false;
        }

        public boolean isBlockHighHumidity(BlockPos blockPos) {
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
        public boolean canCoordinateBeSpawn(int n, int n2) {
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


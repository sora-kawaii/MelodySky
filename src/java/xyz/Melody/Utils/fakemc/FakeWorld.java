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
        this.field_73011_w.func_76558_a(this);
    }

    protected boolean func_175680_a(int n, int n2, boolean bl) {
        return false;
    }

    public BlockPos func_175672_r(BlockPos blockPos) {
        return new BlockPos(blockPos.func_177958_n(), 63, blockPos.func_177952_p());
    }

    public boolean func_175623_d(BlockPos blockPos) {
        return blockPos.func_177956_o() > 63;
    }

    public boolean func_175656_a(BlockPos blockPos, IBlockState iBlockState) {
        return true;
    }

    public boolean func_175698_g(BlockPos blockPos) {
        return true;
    }

    public void func_175646_b(BlockPos blockPos, TileEntity tileEntity) {
    }

    public void notifyBlockUpdate(BlockPos blockPos, IBlockState iBlockState, IBlockState iBlockState2, int n) {
    }

    public boolean func_175655_b(BlockPos blockPos, boolean bl) {
        return this.func_175623_d(blockPos);
    }

    public void func_175695_a(BlockPos blockPos, Block block, EnumFacing enumFacing) {
    }

    public void notifyNeighborsRespectDebug(BlockPos blockPos, Block block, boolean bl) {
    }

    public void markAndNotifyBlock(BlockPos blockPos, Chunk chunk, IBlockState iBlockState, IBlockState iBlockState2, int n) {
    }

    public void func_72975_g(int n, int n2, int n3, int n4) {
    }

    public void func_147458_c(int n, int n2, int n3, int n4, int n5, int n6) {
    }

    public boolean func_175691_a(BlockPos blockPos, Block block) {
        return false;
    }

    public int func_175671_l(BlockPos blockPos) {
        return 14;
    }

    public int func_175721_c(BlockPos blockPos, boolean bl) {
        return 14;
    }

    public int func_175699_k(BlockPos blockPos) {
        return 14;
    }

    public int func_175642_b(EnumSkyBlock enumSkyBlock, BlockPos blockPos) {
        return 14;
    }

    public int func_175705_a(EnumSkyBlock enumSkyBlock, BlockPos blockPos) {
        return 14;
    }

    public boolean func_175710_j(BlockPos blockPos) {
        return blockPos.func_177956_o() > 62;
    }

    public BlockPos func_175645_m(BlockPos blockPos) {
        return new BlockPos(blockPos.func_177958_n(), 63, blockPos.func_177952_p());
    }

    public int func_82734_g(int n, int n2) {
        return 63;
    }

    protected void func_147456_g() {
    }

    public void func_175704_b(BlockPos blockPos, BlockPos blockPos2) {
    }

    public void func_175653_a(EnumSkyBlock enumSkyBlock, BlockPos blockPos, int n) {
    }

    public float func_175724_o(BlockPos blockPos) {
        return 1.0f;
    }

    public float getSunBrightnessFactor(float f) {
        return 1.0f;
    }

    public float func_72971_b(float f) {
        return 1.0f;
    }

    public float getSunBrightnessBody(float f) {
        return 1.0f;
    }

    public boolean func_72935_r() {
        return true;
    }

    public boolean func_72942_c(Entity entity) {
        return false;
    }

    public boolean func_72838_d(Entity entity) {
        return false;
    }

    public void func_72923_a(Entity entity) {
    }

    public void func_72847_b(Entity entity) {
    }

    public void func_72900_e(Entity entity) {
    }

    public void removeEntityDangerously(Entity entity) {
    }

    public int func_72967_a(float f) {
        return 6;
    }

    public void func_180497_b(BlockPos blockPos, Block block, int n, int n2) {
    }

    public void func_72939_s() {
    }

    public void func_72866_a(Entity entity, boolean bl) {
        if (bl) {
            ++entity.field_70173_aa;
        }
    }

    public boolean func_72855_b(AxisAlignedBB axisAlignedBB) {
        return true;
    }

    public boolean func_72917_a(AxisAlignedBB axisAlignedBB, Entity entity) {
        return true;
    }

    public boolean func_72829_c(AxisAlignedBB axisAlignedBB) {
        return false;
    }

    public boolean containsAnyLiquid(AxisAlignedBB axisAlignedBB) {
        return false;
    }

    public boolean func_72918_a(AxisAlignedBB axisAlignedBB, Material material, Entity entity) {
        return false;
    }

    public boolean func_72875_a(AxisAlignedBB axisAlignedBB, Material material) {
        return false;
    }

    public TileEntity func_175625_s(BlockPos blockPos) {
        return null;
    }

    public boolean func_175719_a(EntityPlayer entityPlayer, BlockPos blockPos, EnumFacing enumFacing) {
        return true;
    }

    public String func_72981_t() {
        return "";
    }

    public String func_72827_u() {
        return "";
    }

    public void func_175690_a(BlockPos blockPos, TileEntity tileEntity) {
    }

    public void func_175713_t(BlockPos blockPos) {
    }

    public void func_147457_a(TileEntity tileEntity) {
    }

    public boolean func_175677_d(BlockPos blockPos, boolean bl) {
        return true;
    }

    public void func_72835_b() {
    }

    protected void func_72979_l() {
    }

    public void updateWeatherBody() {
    }

    public boolean func_175675_v(BlockPos blockPos) {
        return false;
    }

    public boolean func_175662_w(BlockPos blockPos) {
        return false;
    }

    public boolean func_175670_e(BlockPos blockPos, boolean bl) {
        return false;
    }

    public boolean canBlockFreezeBody(BlockPos blockPos, boolean bl) {
        return false;
    }

    public boolean func_175708_f(BlockPos blockPos, boolean bl) {
        return false;
    }

    public boolean canSnowAtBody(BlockPos blockPos, boolean bl) {
        return false;
    }

    public boolean func_72955_a(boolean bl) {
        return false;
    }

    public List func_72920_a(Chunk chunk, boolean bl) {
        return null;
    }

    public Entity func_72857_a(Class clazz, AxisAlignedBB axisAlignedBB, Entity entity) {
        return null;
    }

    public int func_72907_a(Class clazz) {
        return 0;
    }

    public int func_175676_y(BlockPos blockPos) {
        return 0;
    }

    public int func_175627_a(BlockPos blockPos, EnumFacing enumFacing) {
        return 0;
    }

    public boolean func_175709_b(BlockPos blockPos, EnumFacing enumFacing) {
        return false;
    }

    public int func_175651_c(BlockPos blockPos, EnumFacing enumFacing) {
        return 0;
    }

    public boolean func_175640_z(BlockPos blockPos) {
        return false;
    }

    public int func_175687_A(BlockPos blockPos) {
        return 0;
    }

    public void func_72906_B() throws MinecraftException {
    }

    public long func_72905_C() {
        return 1L;
    }

    public long func_82737_E() {
        return 1L;
    }

    public long func_72820_D() {
        return 1L;
    }

    public void func_72877_b(long l2) {
    }

    public BlockPos func_175694_M() {
        return new BlockPos(0, 64, 0);
    }

    public void func_72897_h(Entity entity) {
    }

    public boolean func_175678_i(BlockPos blockPos) {
        return blockPos.func_177956_o() > 62;
    }

    public boolean canMineBlockBody(EntityPlayer entityPlayer, BlockPos blockPos) {
        return false;
    }

    public void func_72960_a(Entity entity, byte by) {
    }

    public float func_72819_i(float f) {
        return 0.0f;
    }

    public void func_175641_c(BlockPos blockPos, Block block, int n, int n2) {
    }

    public void func_72854_c() {
    }

    @Override
    public boolean isRainingAt(BlockPos blockPos) {
        return false;
    }

    public void func_147442_i(float f) {
    }

    public float func_72867_j(float f) {
        return 0.0f;
    }

    public void func_72894_k(float f) {
    }

    public boolean func_72911_I() {
        return false;
    }

    public boolean func_72896_J() {
        return false;
    }

    public boolean func_180502_D(BlockPos blockPos) {
        return false;
    }

    public void func_72823_a(String string, WorldSavedData worldSavedData) {
    }

    public void func_175669_a(int n, BlockPos blockPos, int n2) {
    }

    public void playEvent(EntityPlayer entityPlayer, int n, BlockPos blockPos, int n2) {
    }

    public void playEvent(int n, BlockPos blockPos, int n2) {
    }

    public int func_72800_K() {
        return 256;
    }

    public int func_72940_L() {
        return 256;
    }

    public void func_92088_a(double d, double d2, double d3, double d4, double d5, double d6, NBTTagCompound nBTTagCompound) {
    }

    public boolean func_175700_a(TileEntity tileEntity) {
        return true;
    }

    public boolean isSideSolid(BlockPos blockPos, EnumFacing enumFacing) {
        return blockPos.func_177956_o() <= 63;
    }

    public boolean isSideSolid(BlockPos blockPos, EnumFacing enumFacing, boolean bl) {
        return blockPos.func_177956_o() <= 63;
    }

    public int countEntities(EnumCreatureType enumCreatureType, boolean bl) {
        return 0;
    }

    protected IChunkProvider func_72970_h() {
        return new FakeChunkProvider();
    }

    public Chunk func_72964_e(int n, int n2) {
        return null;
    }

    protected static class FakeChunkProvider
    implements IChunkProvider {
        protected FakeChunkProvider() {
        }

        public Chunk getLoadedChunk(int n, int n2) {
            return null;
        }

        public Chunk func_73154_d(int n, int n2) {
            return null;
        }

        public String func_73148_d() {
            return null;
        }

        public boolean func_73156_b() {
            return false;
        }

        public boolean func_191062_e(int n, int n2) {
            return true;
        }

        public boolean func_73149_a(int n, int n2) {
            return false;
        }

        public void func_73153_a(IChunkProvider iChunkProvider, int n, int n2) {
        }

        public boolean func_177460_a(IChunkProvider iChunkProvider, Chunk chunk, int n, int n2) {
            return false;
        }

        public boolean func_73151_a(boolean bl, IProgressUpdate iProgressUpdate) {
            return false;
        }

        public boolean func_73157_c() {
            return false;
        }

        public int func_73152_e() {
            return 0;
        }

        public void func_104112_b() {
        }

        public List<BiomeGenBase.SpawnListEntry> func_177458_a(EnumCreatureType enumCreatureType, BlockPos blockPos) {
            return null;
        }

        public BlockPos func_180513_a(World world, String string, BlockPos blockPos) {
            return null;
        }

        public Chunk func_177459_a(BlockPos blockPos) {
            return null;
        }

        public void func_180514_a(Chunk chunk, int n, int n2) {
        }
    }

    protected static class FakeSaveHandler
    implements ISaveHandler {
        protected FakeSaveHandler() {
        }

        public WorldInfo func_75757_d() {
            return null;
        }

        public void func_75762_c() {
        }

        public IChunkLoader func_75763_a(WorldProvider worldProvider) {
            return null;
        }

        public void func_75755_a(WorldInfo worldInfo, NBTTagCompound nBTTagCompound) {
        }

        public void func_75761_a(WorldInfo worldInfo) {
        }

        public IPlayerFileData func_75756_e() {
            return null;
        }

        public void func_75759_a() {
        }

        public File func_75765_b() {
            return null;
        }

        public File func_75758_b(String string) {
            return null;
        }

        public String func_75760_g() {
            return null;
        }
    }

    protected static class FakeWorldProvider
    extends WorldProvider {
        protected FakeWorldProvider() {
        }

        public boolean func_76569_d() {
            return true;
        }

        public boolean func_76567_e() {
            return true;
        }

        public int func_76557_i() {
            return 63;
        }

        public boolean func_76568_b(int n, int n2) {
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

        public boolean func_76566_a(int n, int n2) {
            return true;
        }

        public String func_80007_l() {
            return null;
        }

        public String func_177498_l() {
            return null;
        }
    }
}


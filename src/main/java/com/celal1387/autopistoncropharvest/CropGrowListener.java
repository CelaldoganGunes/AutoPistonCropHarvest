package com.celal1387.autopistoncropharvest;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CropGrowListener implements Listener {

    private List<BlockFace> facesToCheck;
    private Material harvester;
    private Plugin plugin;
    private List<String> blockGrowList, blockSpreadList;
    private Random r = new Random();
    private HashMap<String, String> dropList;

    public CropGrowListener(){
        plugin = AutoPistonCropHarvest.getPlugin();
        blockGrowList  = new ArrayList<>();
        blockSpreadList  = new ArrayList<>();
        facesToCheck = new ArrayList<>();
        dropList = new HashMap<>();

        blockGrowList = plugin.getConfig().getStringList("blockGrowList");
        blockSpreadList = plugin.getConfig().getStringList("blockSpreadList");

        for(String direction: plugin.getConfig().getStringList("directions")){
            facesToCheck.add(BlockFace.valueOf(direction));
        }

        harvester = Material.getMaterial(plugin.getConfig().getString("harvester"));

        for(String key: plugin.getConfig().getConfigurationSection("dropList").getKeys(true)){
            dropList.put(key,plugin.getConfig().getString("dropList."+key));
        }
    }


    @EventHandler
    public void onBlockGrow(BlockGrowEvent e){
        Block block = null;
        String dropName = null;
        if (blockGrowList.contains(e.getNewState().getType().name())){
            block = e.getNewState().getBlock();
            dropName = e.getNewState().getType().name();
        }

        if (HarvestTheCrop(block, dropName) == true)
        {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent e){
        Block block = null;
        String dropName = null;

        if (blockSpreadList.contains(e.getNewState().getType().name())){
            block = e.getNewState().getBlock();
            dropName = e.getNewState().getType().name();
        }

        if (HarvestTheCrop(block, dropName) == true)
        {
            e.setCancelled(true);
        }
    }

    private Boolean HarvestTheCrop(Block block, String dropname)
    {
        if(block != null){
            for(BlockFace face : facesToCheck) {
                if(block.getRelative(face).getType() == harvester){

                    Block relativeBlock = block.getRelative(face);
                    Directional directional = (Directional) relativeBlock.getBlockData();

                    if (directional.getFacing() == block.getFace(relativeBlock).getOppositeFace()){

                        int dropAmount = (dropname == "MELON") ? r.nextInt(5)+3 : 1;
                        DropWithVelocity(block, dropAmount, dropname, directional);
                        
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void DropWithVelocity(Block block, int dropAmount, String dropname, Directional directional)
    {
        Material dropType = Material.matchMaterial(dropList.get(dropname));
        Item drop = block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(dropType, dropAmount));
        drop.setVelocity(directional.getFacing().getDirection().multiply(0.2));
    }

}

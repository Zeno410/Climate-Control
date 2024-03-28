
package com.Zeno410Utils;

/**
 *
 * @author Zeno410
 * based on code Copyright (c) 2011 Joran de Raaff, www.joranderaaff.nl
 * used with permission
/*/


import com.Zeno410Utils.PlaneLocation;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.world.chunk.storage.RegionFile;
//import net.minecraft.world.chunk.storage.;

public class ChunkLister {

        private Map<String, Boolean> markedSafe;

        public ArrayList<PlaneLocation> savedChunks(String levelPathString) {

            ArrayList<PlaneLocation> result = new ArrayList<PlaneLocation>();

                ArrayList<RegionFileInfo> regionFileInfos = new ArrayList<RegionFileInfo>();

                File levelPath = new File(levelPathString, "region");

                if(!levelPath.exists()) {
                        System.out.println("This path does not exist: " + levelPath.getAbsolutePath());
                }

                String[] files = levelPath.list();

                if (files != null){
                    for (int i=0; i < files.length; i++){
                        // Get filename of file or directory
                        String fileName = files[i];
                        //total filepath
                        File filePath = new File(levelPath.toString(), files[i]);

                        if (filePath.isFile() && filePath.toString().endsWith("mca")) {
                                // Get filename parts
                                String[] fileNameParts = fileName.split("\\.");
                                // Get region coordinates from filename
                                int regionX = Integer.parseInt(fileNameParts[1]);
                                int regionZ = Integer.parseInt(fileNameParts[2]);

                                RegionFileInfo info = new RegionFileInfo(filePath, regionX, regionZ);
                                regionFileInfos.add(info);
                        }
                    }
                }


                System.out.println("-----------------------------------");

                for (RegionFileInfo info: regionFileInfos) {
                    result.addAll(info.savedChunks());
                }

                return result;
        }



        private class RegionFileInfo {
                public File filePath;
                public int regionX;
                public int regionZ;

                public RegionFileInfo(File filePath, int regionX, int regionZ) {
                        this.filePath = filePath;
                        this.regionX = regionX;
                        this.regionZ = regionZ;
                }

         ArrayList<PlaneLocation> savedChunks() {
               ArrayList<PlaneLocation> result = new ArrayList<PlaneLocation>();
               RegionFile regionFile = new RegionFile(filePath);
               for (int x = 0; x < 32; x++){
                    for (int z = 0; z < 32; z++){
                            if(regionFile.isChunkSaved(x, z)){
                                    int chunkX = (regionX * 32 + x);
                                    int chunkZ = (regionZ * 32 + z);
                                    result.add(new PlaneLocation(chunkX,chunkZ));
                            }
                    }

               }
            try {
                regionFile.close();
            } catch (IOException ex) {
                Logger.getLogger(ChunkLister.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
            return result;
        }
       }
}

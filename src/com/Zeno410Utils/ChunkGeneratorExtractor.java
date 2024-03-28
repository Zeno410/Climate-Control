
package com.Zeno410Utils;

import java.lang.reflect.Field;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.structure.MapGenVillage;

/**
 *
 * @author Zeno410
 */
public class ChunkGeneratorExtractor {

    private AccessChunkProviderServer accessChunkProviderServer = new AccessChunkProviderServer();

    private Accessor<ChunkProviderServer,IChunkProvider> providerFromChunkServer =
            new Accessor<ChunkProviderServer,IChunkProvider>("field_73246_d");

    private Accessor<ChunkGeneratorOverworld,MapGenVillage> villageGeneratorFromVillage =
            new Accessor<ChunkGeneratorOverworld,MapGenVillage>("field_73224_v");

    public IChunkProvider extractFrom(WorldServer world) {
        ChunkProviderServer server = accessChunkProviderServer.chunkProviderServer(world);
        IChunkProvider provider = providerFromChunkServer.get(server);
        if (!(provider instanceof ChunkGeneratorOverworld)){
            /*if (provider instanceof ChunkProviderWrapper) {
                provider = ((ChunkProviderWrapper)provider).wrappee();
            }*/
        }
        return provider;
    }

    public void impose(WorldServer world, MapGenVillage mapGen) {
        IChunkProvider provider = extractFrom(world);
        if (provider instanceof ChunkGeneratorOverworld) {
           villageGeneratorFromVillage.setField((ChunkGeneratorOverworld)provider, mapGen);
        }
    }

    static class AccessChunkProviderServer{
        Field iChunkProviderServerField;
        AccessChunkProviderServer() {
            try {setIChunkProviderField();}
            catch(IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
        private void setIChunkProviderField() throws IllegalAccessException{
            Field [] fields = WorldServer.class.getDeclaredFields();
            for (int i = 0; i < fields.length;i ++) {
                if (fields[i].getName().contains("field_73059_b")) {
                    iChunkProviderServerField = fields[i];
                    iChunkProviderServerField.setAccessible(true);
                }
            }
        }

        public ChunkProviderServer chunkProviderServer(WorldServer world) {
            try {
                //logger.info( "providing chunk "+world.getClass().getName());
                 return (ChunkProviderServer)(iChunkProviderServerField.get(world));
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }

        public void setIChunkProvider(WorldServer world,ChunkProviderServer chunkProvider) {
            try {
                //logger.info( "saving provider "+world.getClass().getName()+ " "+ chunkProvider.getClass().getName());
                iChunkProviderServerField.set(world, chunkProvider);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


}

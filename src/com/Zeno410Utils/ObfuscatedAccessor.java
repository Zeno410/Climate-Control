
package com.Zeno410Utils;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.common.patcher.ClassPatchManager;
import org.objectweb.asm.commons.Remapper;

/**
 *
 * @author Zeno410
 */
public class ObfuscatedAccessor<ObjectType,FieldType> extends Accessor<ObjectType,FieldType>{
    private Accessor<Remapper,Map<String,Map<String,String>>> fieldAccessor=
            new Accessor<Remapper,Map<String,Map<String,String>>>("fieldNameMaps");
    public static Logger logger = new Zeno410Logger("ObfuscatedAccessor").logger();
    Remapper remapper;

    private Accessor<ObjectType,FieldType> deobfuscated;
    private final String fieldName;
    private String deobfuscatedName;
    public ObfuscatedAccessor(String _fieldName) {
        super(_fieldName);
        fieldName = _fieldName;
    }

    private Accessor<ObjectType,FieldType> getDeobfuscated(ObjectType objectType) {
        Class objectClass = objectType.getClass();
        Set<String> obfedClasses = FMLDeobfuscatingRemapper.INSTANCE.getObfedClasses();
        for (String readableName: obfedClasses) {
            String obfedName = FMLDeobfuscatingRemapper.INSTANCE.map(readableName);
            logger.info(readableName+" to "+obfedName);
            remapper = FMLDeobfuscatingRemapper.INSTANCE;
            Map<String,String> fieldMap = this.fieldAccessor.get(remapper).get(readableName);
            if (fieldMap== null) continue;
            for (String key: fieldMap.keySet()) {
                logger.info("     "+key + " " + fieldMap.get(key));
            }
        }

        while ((objectClass != null)&&(objectClass != Object.class)) {
            String deobfuscatedClassName = FMLDeobfuscatingRemapper.INSTANCE.unmap(objectClass.getCanonicalName());
            if (deobfuscatedClassName == null) continue;
            if (deobfuscatedClassName.equals("")) continue;
            deobfuscatedName = FMLDeobfuscatingRemapper.INSTANCE.mapFieldName(deobfuscatedClassName, fieldName, fieldName);
            deobfuscated = new Accessor<ObjectType,FieldType>(deobfuscatedName);
        }

        throw new RuntimeException("Could not remap field "+fieldName+ " in class "+objectType.getClass().getCanonicalName());
    }
    private Accessor<ObjectType,FieldType> deobfuscated(ObjectType objectType) {
        if (deobfuscated == null) {
            deobfuscated = getDeobfuscated(objectType) ;
        }
        return deobfuscated;
    }

    public FieldType get(ObjectType object) {
        return deobfuscated(object).get(object);
    }

    public void setField(ObjectType object,FieldType fieldValue) {
        deobfuscated(object).setField(object, fieldValue);
    }


}

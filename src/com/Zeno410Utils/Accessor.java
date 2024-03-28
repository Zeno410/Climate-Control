package com.Zeno410Utils;

import java.lang.reflect.*;
import java.util.logging.Logger;
/**
 *
 * @author Zeno410
 */
public class Accessor<ObjectType,FieldType>{
   public static final Zeno410Logger logger = new Zeno410Logger("Accessor");
        private Field field;
        private final String fieldName;
        public Accessor(String _fieldName) {
            fieldName = _fieldName;
        }

        private Field field(ObjectType example) {
            Class classObject = example.getClass();
            if (field == null) {
                try {setField(classObject);}
                catch(IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            return field;
        }

        private void setField(Class classObject) throws IllegalAccessException{
            // hunts through the class object and all superclasses looking for the field name
            Field [] fields;
            StringBuilder builder = new StringBuilder();
            do {
                fields = classObject.getDeclaredFields();
                for (int i = 0; i < fields.length;i ++) {
                    //logger.logger().info("Fieldname "+fields[i].getName());
                    //builder.append(fields[i].getName());
                    //builder.append(";");
                    if (fields[i].getName().contains(fieldName)) {
                        field = fields[i];
                        field.setAccessible(true);
                        return;
                    }
                }
                classObject = classObject.getSuperclass();
            } while (classObject != Object.class);
            //logger.info(fieldName +" not found in class "+classObject.getName());\
            throw new RuntimeException(fieldName +" not found in class "+classObject.getName()+ " " + builder.toString());
        }

        public FieldType get(ObjectType object) {
            try {
                 return (FieldType)(field(object).get(object));
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }

        public void setField(ObjectType object,FieldType fieldValue) {
            try {

                //logger.info(" setting to ");
                //logger.info(fieldValue.toString());
                field(object).set(object, fieldValue);
                //logger.info("set");
            } catch (IllegalArgumentException ex) {
                //logger.info("illegal argument");
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                //logger.info("illegal access");
                throw new RuntimeException(ex);
            }
        }

    }

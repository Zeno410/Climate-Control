
package com.Zeno410Utils;
import java.lang.reflect.*;

/**
 *
 * @author Zeno410
 */
public class AccessLong <ObjectType>{
        private Field field;
        private final String fieldName;
        public AccessLong(String _fieldName) {
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
            do {
                fields = classObject.getDeclaredFields();
                for (int i = 0; i < fields.length;i ++) {
                    if (fields[i].getName().contains(fieldName)) {
                        field = fields[i];
                        field.setAccessible(true);
                        return;
                    }
                }
                classObject = classObject.getSuperclass();
            } while (classObject != Object.class);
            throw new RuntimeException(fieldName +" not found in class "+classObject.getName());
        }

        public long get(ObjectType object) {
            try {
                 return (field(object).getLong(object));
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }

        public void setField(ObjectType object,long fieldValue) {
            try {
                field(object).setLong(object, fieldValue);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }

    }
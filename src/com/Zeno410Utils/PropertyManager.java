
package com.Zeno410Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager
{
    private final Properties properties = new Properties();

    /** Reference to the logger. */
    private final File associatedFile;

    public PropertyManager(File par1File)
    {
        this.associatedFile = par1File;

        if (par1File.exists())
        {
            FileInputStream fileinputstream = null;

            try
            {
                fileinputstream = new FileInputStream(par1File);
                this.properties.load(fileinputstream);
            }
            catch (Exception exception)
            {
                this.logMessageAndSave();
            }
            finally
            {
                if (fileinputstream != null)
                {
                    try
                    {
                        fileinputstream.close();
                    }
                    catch (IOException ioexception)
                    {
                        ;
                    }
                }
            }
        }
        else
        {
            this.logMessageAndSave();
        }
    }

    /**
     * logs an info message then calls saveSettingsToFile Yes this appears to be a potential stack overflow - these 2
     * functions call each other repeatdly if an exception occurs.
     */
    public void logMessageAndSave()
    {
        this.saveProperties();
    }

    /**
     * Writes the properties to the properties file.
     */
    public void saveProperties()
    {
        FileOutputStream fileoutputstream = null;

        try
        {
            fileoutputstream = new FileOutputStream(this.associatedFile);
            this.properties.store(fileoutputstream, "Minecraft server properties");
        }
        catch (Exception exception)
        {
            this.logMessageAndSave();
        }
        finally
        {
            if (fileoutputstream != null)
            {
                try
                {
                    fileoutputstream.close();
                }
                catch (IOException ioexception)
                {
                    ;
                }
            }
        }
    }

    /**
     * Returns this PropertyManager's file object used for property saving.
     */
    public File getPropertiesFile()
    {
        return this.associatedFile;
    }

    /**
     * Gets a property. If it does not exist, set it to the specified value.
     */
    public String getProperty(String par1Str, String par2Str)
    {
        if (!this.properties.containsKey(par1Str))
        {
            this.properties.setProperty(par1Str, par2Str);
            this.saveProperties();
        }

        return this.properties.getProperty(par1Str, par2Str);
    }

    /**
     * Gets an integer property. If it does not exist, set it to the specified value.
     */
    public int getIntProperty(String par1Str, int par2)
    {
        try
        {
            return Integer.parseInt(this.getProperty(par1Str, "" + par2));
        }
        catch (Exception exception)
        {
            this.properties.setProperty(par1Str, "" + par2);
            return par2;
        }
    }

    /**
     * Gets a boolean property. If it does not exist, set it to the specified value.
     */
    public boolean getBooleanProperty(String par1Str, boolean par2)
    {
        try
        {
            return Boolean.parseBoolean(this.getProperty(par1Str, "" + par2));
        }
        catch (Exception exception)
        {
            this.properties.setProperty(par1Str, "" + par2);
            return par2;
        }
    }

    /**
     * Saves an Object with the given property name.
     */
    public void setProperty(String par1Str, Object par2Obj)
    {
        this.properties.setProperty(par1Str, "" + par2Obj);
    }
}
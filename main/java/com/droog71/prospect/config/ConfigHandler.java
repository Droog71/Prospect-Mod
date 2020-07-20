package com.droog71.prospect.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ConfigHandler 
{
	public static boolean getSporesEnabled()
	{
		File configFile = new File(System.getProperty("user.dir")+"/config/prospect.cfg");	
        if (configFile.exists())
        {
			Scanner configFileScanner;
			try 
			{
				configFileScanner = new Scanner(configFile);
				String configFileContents = configFileScanner.useDelimiter("\\Z").next();				
				configFileScanner.close();
				String configValue = configFileContents.split(":")[1].toLowerCase();
				if (configValue.equals("true"))
				{
					return true;
				}
			} 
			catch (FileNotFoundException e) 
			{
				System.out.println("Reactor turbines mod failed to find config file!");
				e.printStackTrace();
			}			
        }
        else
        {
        	createConfigFile();
        }
        return false;
	}
	
	public static void createConfigFile()
	{
		try 
    	{
			File configFile = new File(System.getProperty("user.dir")+"/config/prospect.cfg");
			if (!configFile.exists())
	        {
				configFile.createNewFile();
				FileWriter f;    			
			    try 
			    {
			        f = new FileWriter(configFile,false);
			        f.write("toxic_spores_enabled:true");
			        f.close();
			    } 
			    catch (IOException ioe) 
			    {
			    	System.out.println("Prospect mod failed to write to config file!");
			        ioe.printStackTrace();
			    } 
	        }			
		} 
    	catch (IOException e) 
    	{
    		System.out.println("Prospect mod failed to create config file!");
			e.printStackTrace();
		}
	}
}
package com.droog71.prospect.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.droog71.prospect.items.LaunchPadItem;
import com.droog71.prospect.items.ReplicatorItem;

public class ConfigHandler 
{	
	// Gets the value of toxic_spores_enabled from the config file
	public static boolean toxicSporesEnabled()
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
				String configValue = configFileContents.split(">")[1].split(":")[1].toLowerCase().trim();
				if (configValue.equals("true"))
				{
					return true;
				}
			} 
			catch (FileNotFoundException e) 
			{
				System.out.println("Prospect mod failed to find config file!");
				e.printStackTrace();
			}			
        }
        else
        {
        	createConfigFile();
        }
        return false;
	}
	
	// Gets the value of purifier_particle_effects from the config file
	public static boolean purifierParticleEffectsEnabled()
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
				String configValue = configFileContents.split(">")[2].split(":")[1].toLowerCase().trim();
				if (configValue.equals("true"))
				{
					return true;
				}
			} 
			catch (FileNotFoundException e) 
			{
				System.out.println("Prospect mod failed to find config file!");
				e.printStackTrace();
			}			
        }
        else
        {
        	createConfigFile();
        }
        return false;
	}
	
	// Gets launch pad items from config file
	public static List<LaunchPadItem> launchPadItems()
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
				String[] launchPadItemsArray = configFileContents.split(">")[3].split("}")[1].split(",");
				if (launchPadItemsArray.length > 0)
				{
					List<LaunchPadItem> items = new ArrayList<LaunchPadItem>();
					for (int i = 0; i < launchPadItemsArray.length; i++)
					{
						String name = launchPadItemsArray[i].split("=")[0].toLowerCase().trim();
						int worth = Integer.parseInt(launchPadItemsArray[i].split("=")[1].toLowerCase().trim());
						worth = worth > 64 ? 64 : worth < 0 ? 0 : worth;
						LaunchPadItem launchPadItem = new LaunchPadItem(name,worth);
						items.add(launchPadItem);
					}
					return items;
				}
			} 
			catch (FileNotFoundException e) 
			{
				System.out.println("Prospect mod failed to find config file!");
				e.printStackTrace();
			}			
        }
        else
        {
        	createConfigFile();
        }
        return null;
	}
	
	// Gets replicator items from config file
	public static List<ReplicatorItem> replicatorItems()
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
				String[] replicatorItemsArray = configFileContents.split(">")[4].split("}")[1].split(",");
				if (replicatorItemsArray.length > 0)
				{
					List<ReplicatorItem> items = new ArrayList<ReplicatorItem>();
					for (int i = 0; i < replicatorItemsArray.length; i++)
					{
						String name = replicatorItemsArray[i].split("=")[0].toLowerCase().trim();
						int worth = Integer.parseInt(replicatorItemsArray[i].split("=")[1].toLowerCase().trim());
						worth = worth > 64 ? 64 : worth < 0 ? 0 : worth;
						ReplicatorItem replicatorItem = new ReplicatorItem(name,worth);
						items.add(replicatorItem);
					}
					return items;
				}
			} 
			catch (FileNotFoundException e) 
			{
				System.out.println("Prospect mod failed to find config file!");
				e.printStackTrace();
			}			
        }
        else
        {
        	createConfigFile();
        }
        return null;
	}
	
	// Gets protective armor items from config file
	public static String[] protectiveArmorItems()
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
				return configFileContents.split(">")[5].split("}")[1].split(",");
			} 
			catch (FileNotFoundException e) 
			{
				System.out.println("Prospect mod failed to find config file!");
				e.printStackTrace();
			}			
        }
        else
        {
        	createConfigFile();
        }
        return null;
	}
	
	// Gets the value of ore_generation_enabled from the config file
	public static boolean oreGenEnabled()
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
				String configValue = configFileContents.split(">")[6].split(":")[1].toLowerCase().trim();
				if (configValue.equals("true"))
				{
					return true;
				}
			} 
			catch (FileNotFoundException e) 
			{
				System.out.println("Prospect mod failed to find config file!");
				e.printStackTrace();
			}			
        }
        else
        {
        	createConfigFile();
        }
        return false;
	}
	
	// Creates the config file
	public static void createConfigFile()
	{
		try 
    	{
			File configFile = new File(System.getProperty("user.dir")+"/config/prospect.cfg");
			if (!configFile.exists())
	        {
				configFile.createNewFile();
				writeToConfigFile(configFile);
	        }	
			else
			{
				Scanner configFileScanner;
				try 
				{
					configFileScanner = new Scanner(configFile);
					String configFileContents = configFileScanner.useDelimiter("\\Z").next();				
					configFileScanner.close();
					if (configFileContents.split(">").length < 7)
					{
						writeToConfigFile(configFile);
					}
				} 
				catch (FileNotFoundException e)
				{
					System.out.println("Prospect mod failed to find config file!");
					e.printStackTrace();
				}	
			}
		} 
    	catch (IOException e) 
    	{
    		System.out.println("Prospect mod failed to create config file!");
			e.printStackTrace();
		}
	}
	
	public static void writeToConfigFile(File configFile)
	{
		FileWriter f;    			
	    try 
	    {
	        f = new FileWriter(configFile,false);
	        f.write(">toxic_spores_enabled:true\n" + 
	        		">purifier_particle_effects:true\n" + 
	        		">launch_pad_items}minecraft:piston=4,minecraft:hopper=2\n" + 
	        		">replicator_items}minecraft:apple=1,minecraft:feather=1\n" +
	        		">spore_armor}minecraft:diamond_helmet,minecraft:diamond_chestplate,minecraft:diamond_leggings,minecraft:diamond_boots\n" +
	        		">ore_generation_enabled:true");
	        f.close();
	    } 
	    catch (IOException ioe) 
	    {
	    	System.out.println("Prospect mod failed to write to config file!");
	        ioe.printStackTrace();
	    }
	}
	
	// Sets the toxic_spores_enabled value to false in config
	public static void disableToxicSpores()
	{
		try 
    	{
			File configFile = new File(System.getProperty("user.dir")+"/config/prospect.cfg");
			if (!configFile.exists())
	        {
				configFile.createNewFile();
	        }   			
			writeToConfigFile(configFile);		
		} 
    	catch (IOException e) 
    	{
    		System.out.println("Prospect mod failed to create config file!");
			e.printStackTrace();
		}
	}
}
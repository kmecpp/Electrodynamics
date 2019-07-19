package mcp.mobius.waila.api.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import mcp.mobius.waila.api.IWailaBlockDecorator;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.api.IWailaFMPDecorator;
import mcp.mobius.waila.api.IWailaFMPProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.IWailaSummaryProvider;
import mcp.mobius.waila.api.IWailaTooltipRenderer;
import mcp.mobius.waila.cbcore.LangUtil;
import mcp.mobius.waila.utils.Constants;

@SuppressWarnings("rawtypes")
public class ModuleRegistrar implements IWailaRegistrar {

	private static ModuleRegistrar														instance				= null;

	public LinkedHashMap<Class, ArrayList<IWailaDataProvider>>							headBlockProviders		= new LinkedHashMap<>();
	public LinkedHashMap<Class, ArrayList<IWailaDataProvider>>							bodyBlockProviders		= new LinkedHashMap<>();
	public LinkedHashMap<Class, ArrayList<IWailaDataProvider>>							tailBlockProviders		= new LinkedHashMap<>();

	public LinkedHashMap<Class, ArrayList<IWailaDataProvider>>							stackBlockProviders		= new LinkedHashMap<>();
	public LinkedHashMap<Class, ArrayList<IWailaDataProvider>>							NBTDataProviders		= new LinkedHashMap<>();

	public LinkedHashMap<Class, ArrayList<IWailaBlockDecorator>>						blockClassDecorators	= new LinkedHashMap<>();

	public LinkedHashMap<Class, ArrayList<IWailaEntityProvider>>						headEntityProviders		= new LinkedHashMap<>();
	public LinkedHashMap<Class, ArrayList<IWailaEntityProvider>>						bodyEntityProviders		= new LinkedHashMap<>();
	public LinkedHashMap<Class, ArrayList<IWailaEntityProvider>>						tailEntityProviders		= new LinkedHashMap<>();
	public LinkedHashMap<Class, ArrayList<IWailaEntityProvider>>						overrideEntityProviders	= new LinkedHashMap<>();
	public LinkedHashMap<Class, ArrayList<IWailaEntityProvider>>						NBTEntityProviders		= new LinkedHashMap<>();

	public LinkedHashMap<String, ArrayList<IWailaFMPProvider>>							headFMPProviders		= new LinkedHashMap<>();
	public LinkedHashMap<String, ArrayList<IWailaFMPProvider>>							bodyFMPProviders		= new LinkedHashMap<>();
	public LinkedHashMap<String, ArrayList<IWailaFMPProvider>>							tailFMPProviders		= new LinkedHashMap<>();

	public LinkedHashMap<String, ArrayList<IWailaFMPDecorator>>							FMPClassDecorators		= new LinkedHashMap<>();

	public LinkedHashMap<Class, HashSet<String>>										syncedNBTKeys			= new LinkedHashMap<>();

	public LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>>	wikiDescriptions		= new LinkedHashMap<>();
	public LinkedHashMap<Class, ArrayList<IWailaSummaryProvider>>						summaryProviders		= new LinkedHashMap<>();

	public LinkedHashMap<String, String>												IMCRequests				= new LinkedHashMap<>();

	public LinkedHashMap<String, IWailaTooltipRenderer>									tooltipRenderers		= new LinkedHashMap<>();

	private ModuleRegistrar() {
		instance = this;
	}

	public static ModuleRegistrar instance()
	{
		if (ModuleRegistrar.instance == null)
		{
			ModuleRegistrar.instance = new ModuleRegistrar();
		}
		return ModuleRegistrar.instance;
	}

	/* IMC HANDLING */
	public void addIMCRequest(String method, String modname)
	{
		IMCRequests.put(method, modname);
	}

	/* CONFIG HANDLING */
	@Override
	public void addConfig(String modname, String key, String configname)
	{
		this.addConfig(modname, key, configname, Constants.CFG_DEFAULT_VALUE);
	}

	@Override
	public void addConfigRemote(String modname, String key, String configname)
	{
		this.addConfigRemote(modname, key, configname, Constants.CFG_DEFAULT_VALUE);
	}

	@Override
	public void addConfig(String modname, String key)
	{
		this.addConfig(modname, key, Constants.CFG_DEFAULT_VALUE);
	}

	@Override
	public void addConfigRemote(String modname, String key)
	{
		this.addConfigRemote(modname, key, Constants.CFG_DEFAULT_VALUE);
	}

	@Override
	public void addConfig(String modname, String key, String configname, boolean defvalue)
	{
		ConfigHandler.instance().addConfig(modname, key, LangUtil.translateG(configname), defvalue);
	}

	@Override
	public void addConfigRemote(String modname, String key, String configname, boolean defvalue)
	{
		ConfigHandler.instance().addConfigServer(modname, key, LangUtil.translateG(configname), defvalue);
	}

	@Override
	public void addConfig(String modname, String key, boolean defvalue)
	{
		ConfigHandler.instance().addConfig(modname, key, LangUtil.translateG("option." + key), defvalue);
	}

	@Override
	public void addConfigRemote(String modname, String key, boolean defvalue)
	{
		ConfigHandler.instance().addConfigServer(modname, key, LangUtil.translateG("option." + key), defvalue);
	}

	/* REGISTRATION METHODS */
	@Override
	public void registerHeadProvider(IWailaDataProvider dataProvider, Class block)
	{
		this.registerProvider(dataProvider, block, headBlockProviders);
	}

	@Override
	public void registerBodyProvider(IWailaDataProvider dataProvider, Class block)
	{
		this.registerProvider(dataProvider, block, bodyBlockProviders);
	}

	@Override
	public void registerTailProvider(IWailaDataProvider dataProvider, Class block)
	{
		this.registerProvider(dataProvider, block, tailBlockProviders);
	}

	@Override
	public void registerStackProvider(IWailaDataProvider dataProvider, Class block)
	{
		this.registerProvider(dataProvider, block, stackBlockProviders);
	}

	@Override
	public void registerNBTProvider(IWailaDataProvider dataProvider, Class entity)
	{
		this.registerProvider(dataProvider, entity, NBTDataProviders);
	}

	@Override
	public void registerHeadProvider(IWailaEntityProvider dataProvider, Class entity)
	{
		this.registerProvider(dataProvider, entity, headEntityProviders);
	}

	@Override
	public void registerBodyProvider(IWailaEntityProvider dataProvider, Class entity)
	{
		this.registerProvider(dataProvider, entity, bodyEntityProviders);
	}

	@Override
	public void registerTailProvider(IWailaEntityProvider dataProvider, Class entity)
	{
		this.registerProvider(dataProvider, entity, tailEntityProviders);
	}

	@Override
	public void registerNBTProvider(IWailaEntityProvider dataProvider, Class entity)
	{
		this.registerProvider(dataProvider, entity, NBTEntityProviders);
	}

	@Override
	public void registerHeadProvider(IWailaFMPProvider dataProvider, String name)
	{
		this.registerProvider(dataProvider, name, headFMPProviders);
	}

	@Override
	public void registerBodyProvider(IWailaFMPProvider dataProvider, String name)
	{
		this.registerProvider(dataProvider, name, bodyFMPProviders);
	}

	@Override
	public void registerTailProvider(IWailaFMPProvider dataProvider, String name)
	{
		this.registerProvider(dataProvider, name, tailFMPProviders);
	}

	@Override
	public void registerOverrideEntityProvider(IWailaEntityProvider dataProvider, Class entity)
	{
		this.registerProvider(dataProvider, entity, overrideEntityProviders);
	}

	/*
	 * @Override public void registerShortDataProvider(IWailaSummaryProvider
	 * dataProvider, Class item) { this.registerProvider(dataProvider, item,
	 * this.summaryProviders); }
	 */

	@Override
	public void registerDecorator(IWailaBlockDecorator decorator, Class block)
	{
		this.registerProvider(decorator, block, blockClassDecorators);
	}

	@Override
	public void registerDecorator(IWailaFMPDecorator decorator, String name)
	{
		this.registerProvider(decorator, name, FMPClassDecorators);
	}

	private <T, V> void registerProvider(T dataProvider, V clazz, LinkedHashMap<V, ArrayList<T>> target)
	{
		if (clazz == null || dataProvider == null)
		{
			throw new RuntimeException(
					String.format("Trying to register a null provider or null block ! Please check the stacktrace to know what was the original registration method. [Provider : %s, Target : %s]", dataProvider.getClass().getName(), clazz));
		}

		if (!target.containsKey(clazz))
		{
			target.put(clazz, new ArrayList<T>());
		}

		ArrayList<T> providers = target.get(clazz);
		if (providers.contains(dataProvider))
		{
			return;
		}

		target.get(clazz).add(dataProvider);
	}

	@Deprecated
	@Override
	public void registerSyncedNBTKey(String key, Class target)
	{
		if (!syncedNBTKeys.containsKey(target))
		{
			syncedNBTKeys.put(target, new HashSet<String>());
		}

		syncedNBTKeys.get(target).add(key);
	}

	@Override
	public void registerTooltipRenderer(String name, IWailaTooltipRenderer renderer)
	{
		if (!tooltipRenderers.containsKey(name))
		{
			tooltipRenderers.put(name, renderer);
		}
	}

	/* PROVIDER GETTERS */

	public Map<Integer, List<IWailaDataProvider>> getHeadProviders(Object block)
	{
		return getProviders(block, headBlockProviders);
	}

	public Map<Integer, List<IWailaDataProvider>> getBodyProviders(Object block)
	{
		return getProviders(block, bodyBlockProviders);
	}

	public Map<Integer, List<IWailaDataProvider>> getTailProviders(Object block)
	{
		return getProviders(block, tailBlockProviders);
	}

	public Map<Integer, List<IWailaDataProvider>> getStackProviders(Object block)
	{
		return getProviders(block, stackBlockProviders);
	}

	public Map<Integer, List<IWailaDataProvider>> getNBTProviders(Object block)
	{
		return getProviders(block, NBTDataProviders);
	}

	public Map<Integer, List<IWailaEntityProvider>> getHeadEntityProviders(Object entity)
	{
		return getProviders(entity, headEntityProviders);
	}

	public Map<Integer, List<IWailaEntityProvider>> getBodyEntityProviders(Object entity)
	{
		return getProviders(entity, bodyEntityProviders);
	}

	public Map<Integer, List<IWailaEntityProvider>> getTailEntityProviders(Object entity)
	{
		return getProviders(entity, tailEntityProviders);
	}

	public Map<Integer, List<IWailaEntityProvider>> getOverrideEntityProviders(Object entity)
	{
		return getProviders(entity, overrideEntityProviders);
	}

	public Map<Integer, List<IWailaEntityProvider>> getNBTEntityProviders(Object entity)
	{
		return getProviders(entity, NBTEntityProviders);
	}

	public Map<Integer, List<IWailaFMPProvider>> getHeadFMPProviders(String name)
	{
		return getProviders(name, headFMPProviders);
	}

	public Map<Integer, List<IWailaFMPProvider>> getBodyFMPProviders(String name)
	{
		return getProviders(name, bodyFMPProviders);
	}

	public Map<Integer, List<IWailaFMPProvider>> getTailFMPProviders(String name)
	{
		return getProviders(name, tailFMPProviders);
	}

	public Map<Integer, List<IWailaSummaryProvider>> getSummaryProvider(Object item)
	{
		return getProviders(item, summaryProviders);
	}

	public Map<Integer, List<IWailaBlockDecorator>> getBlockDecorators(Object block)
	{
		return getProviders(block, blockClassDecorators);
	}

	public Map<Integer, List<IWailaFMPDecorator>> getFMPDecorators(String name)
	{
		return getProviders(name, FMPClassDecorators);
	}

	public IWailaTooltipRenderer getTooltipRenderer(String name)
	{
		return tooltipRenderers.get(name);
	}

	private <T> Map<Integer, List<T>> getProviders(Object obj, LinkedHashMap<Class, ArrayList<T>> target)
	{
		Map<Integer, List<T>> returnList = new TreeMap<>();
		Integer index = 0;

		for (Class clazz : target.keySet())
		{
			if (clazz.isInstance(obj))
			{
				returnList.put(index, target.get(clazz));
			}

			index++;
		}

		return returnList;
	}

	private <T> Map<Integer, List<T>> getProviders(String name, LinkedHashMap<String, ArrayList<T>> target)
	{
		Map<Integer, List<T>> returnList = new TreeMap<>();
		returnList.put(0, target.get(name));
		return returnList;
	}

	@Deprecated
	public HashSet<String> getSyncedNBTKeys(Object target)
	{
		HashSet<String> returnList = new HashSet<>();
		for (Class clazz : syncedNBTKeys.keySet())
		{
			if (clazz.isInstance(target))
			{
				returnList.addAll(syncedNBTKeys.get(clazz));
			}
		}

		return returnList;
	}

	/* HAS METHODS */

	public boolean hasStackProviders(Object block)
	{
		return hasProviders(block, stackBlockProviders);
	}

	public boolean hasHeadProviders(Object block)
	{
		return hasProviders(block, headBlockProviders);
	}

	public boolean hasBodyProviders(Object block)
	{
		return hasProviders(block, bodyBlockProviders);
	}

	public boolean hasTailProviders(Object block)
	{
		return hasProviders(block, tailBlockProviders);
	}

	public boolean hasNBTProviders(Object block)
	{
		return hasProviders(block, NBTDataProviders);
	}

	public boolean hasHeadEntityProviders(Object entity)
	{
		return hasProviders(entity, headEntityProviders);
	}

	public boolean hasBodyEntityProviders(Object entity)
	{
		return hasProviders(entity, bodyEntityProviders);
	}

	public boolean hasTailEntityProviders(Object entity)
	{
		return hasProviders(entity, tailEntityProviders);
	}

	public boolean hasOverrideEntityProviders(Object entity)
	{
		return hasProviders(entity, overrideEntityProviders);
	}

	public boolean hasNBTEntityProviders(Object entity)
	{
		return hasProviders(entity, NBTEntityProviders);
	}

	public boolean hasHeadFMPProviders(String name)
	{
		return hasProviders(name, headFMPProviders);
	}

	public boolean hasBodyFMPProviders(String name)
	{
		return hasProviders(name, bodyFMPProviders);
	}

	public boolean hasTailFMPProviders(String name)
	{
		return hasProviders(name, tailFMPProviders);
	}

	public boolean hasBlockDecorator(Object block)
	{
		return hasProviders(block, blockClassDecorators);
	}

	public boolean hasFMPDecorator(String name)
	{
		return hasProviders(name, FMPClassDecorators);
	}

	private <T> boolean hasProviders(Object obj, LinkedHashMap<Class, ArrayList<T>> target)
	{
		for (Class clazz : target.keySet())
		{
			if (clazz.isInstance(obj))
			{
				return true;
			}
		}
		return false;
	}

	private <T> boolean hasProviders(String name, LinkedHashMap<String, ArrayList<T>> target)
	{
		return target.containsKey(name);
	}

	public boolean hasSummaryProvider(Class item)
	{
		return summaryProviders.containsKey(item);
	}

	@Deprecated
	public boolean hasSyncedNBTKeys(Object target)
	{
		for (Class clazz : syncedNBTKeys.keySet())
		{
			if (clazz.isInstance(target))
			{
				return true;
			}
		}
		return false;
	}

	/* ----------------- */
	/*
	 * @Override public void registerDocTextFile(String filename) { List<String[]>
	 * docData = null; int nentries = 0;
	 * 
	 * 
	 * try{ docData = this.readFileAsString(filename); } catch (IOException e){
	 * Waila.log.log(Level.WARN, String.format("Error while accessing file %s : %s",
	 * filename, e)); return; }
	 * 
	 * for (String[] ss : docData){ String modid = ss[0]; String name = ss[1];
	 * String meta = ss[2]; String desc = ss[5].replace('$', '\n'); if
	 * (!(desc.trim().equals(""))){ if (!this.wikiDescriptions.containsKey(modid))
	 * this.wikiDescriptions.put(modid, new LinkedHashMap <String, LinkedHashMap
	 * <String, String>>()); if
	 * (!this.wikiDescriptions.get(modid).containsKey(name))
	 * this.wikiDescriptions.get(modid).put(name, new LinkedHashMap<String,
	 * String>());
	 * 
	 * this.wikiDescriptions.get(modid).get(name).put(meta, desc);
	 * System.out.printf("Registered %s %s %s\n", modid, name, meta); nentries += 1;
	 * } }
	 * 
	 * 
	 * // String[] sections = docData.split(">>>>"); // for (String s : sections){
	 * // s.trim(); // if (!s.equals("")){ // try{ // String name =
	 * s.split("\r?\n",2)[0].trim(); // String desc = s.split("\r?\n",2)[1].trim();
	 * // if (!this.wikiDescriptions.containsKey(modid)) //
	 * this.wikiDescriptions.put(modid, new LinkedHashMap <String, String>()); //
	 * this.wikiDescriptions.get(modid).put(name, desc); // nentries += 1; // }catch
	 * (Exception e){ // System.out.printf("%s\n", e); // } // } // }
	 * 
	 * Waila.log.log(Level.INFO, String.format("Registered %s entries from %s",
	 * nentries, filename)); }
	 */

	public boolean hasDocTextModID(String modid)
	{
		return wikiDescriptions.containsKey(modid);
	}

	public boolean hasDocTextItem(String modid, String item)
	{
		if (hasDocTextModID(modid))
		{
			return wikiDescriptions.get(modid).containsKey(item);
		}
		return false;
	}

	public boolean hasDocTextMeta(String modid, String item, String meta)
	{
		if (hasDocTextItem(modid, item))
		{
			return wikiDescriptions.get(modid).get(item).containsKey(meta);
		}
		return false;
	}

	public LinkedHashMap<String, String> getDocText(String modid, String name)
	{
		return wikiDescriptions.get(modid).get(name);
	}

	public String getDocText(String modid, String name, String meta)
	{
		return wikiDescriptions.get(modid).get(name).get(meta);
	}

	public boolean hasDocTextSpecificMeta(String modid, String name, String meta)
	{
		for (String s : this.getDocText(modid, name).keySet())
		{
			if (s.equals(meta))
			{
				return true;
			}
		}
		return false;
	}

	public String getDoxTextWildcardMatch(String modid, String name)
	{
		Set<String> keys = wikiDescriptions.get(modid).keySet();
		for (String s : keys)
		{
			String regexed = s;
			regexed = regexed.replace(".", "\\.");
			regexed = regexed.replace("*", ".*");

			if (name.matches(s))
			{
				return s;
			}
		}
		return null;
	}
}

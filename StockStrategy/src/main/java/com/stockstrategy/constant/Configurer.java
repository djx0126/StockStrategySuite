package com.stockstrategy.constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Configurer {
	private static Properties myProperties;
	private static final String FILENAME = "myConfigs";
	protected static Configurer myConfigurer;
	
	public static Configurer setThreads(int num){
		Constant.THREADNUM = num;
		return myConfigurer;
	}
	
	public static Configurer enableSZ(){
		Constant.ENABLE_SZ=true;
		return myConfigurer;
	}
	
	public static Configurer disableSZ(){
		Constant.ENABLE_SZ=false;
		return myConfigurer;
	}
	
	public static Configurer enableSH(){
		Constant.ENABLE_SH=true;
		return myConfigurer;
	}
	
	public static Configurer disableSH(){
		Constant.ENABLE_SH=false;
		return myConfigurer;
	}
	
	public static Configurer addStrategy(String strategyToAdd){
		List<String> strategyList = new ArrayList<>();
		strategyList.addAll(Arrays.asList(Constant.CUSTOMSTATISTICSTYPES));

		if (!strategyList.contains(strategyToAdd)){
			strategyList.add(strategyToAdd);
		}
		
		String[] newStrategies = strategyList.toArray(new String[0]);
		Constant.CUSTOMSTATISTICSTYPES = newStrategies;
		return myConfigurer;
	}

	public static Configurer addAggregatedStrategy(String strategyToAdd){
		List<String> strategyList = new ArrayList<>();
		strategyList.addAll(Arrays.asList(Constant.AGGREGATEDSTATISTICSTYPES));

		if (!strategyList.contains(strategyToAdd)){
			strategyList.add(strategyToAdd);
		}

		String[] newStrategies = strategyList.toArray(new String[0]);
		Constant.AGGREGATEDSTATISTICSTYPES = newStrategies;
		return myConfigurer;
	}
	
	public static  Configurer setMgmtDir(String pMgmtDir){
		Constant.MGMT_DIR = pMgmtDir;
		Configurer.getInstance().load();
		return myConfigurer;
	}
	
	public static Configurer setShDataDir(String pShDataDir){
		Constant.DATA_DIR_SH = pShDataDir;
		return myConfigurer;
	}

	public static Configurer setSzDataDir(String pSzDataDir){
		Constant.DATA_DIR_SZ = pSzDataDir;
		return myConfigurer;
	}
	
	
	public static Configurer getInstance(){
		if (myConfigurer==null){
			myConfigurer= new Configurer();
		}
		return myConfigurer;
	}
	protected Configurer(){
		init();
	};
	
	private void init(){
		myProperties=new Properties();
		load();
	}

	private synchronized boolean load(){
		boolean result = false;
		try {
			myProperties.load(new FileInputStream(getFile()));
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private synchronized  boolean save(){
		boolean result = false;
		try {
			myProperties.store(new FileOutputStream(getFile()), "Changeable Configs");
			result = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public synchronized boolean setConfig(String name, String value){
		if (myProperties==null){
			System.out.println("NuLL properties");
		}
		myProperties.setProperty(name, value);
		boolean result = save();
		return result;
	}
	
	public String getConfig(String name){
		return myProperties.getProperty(name);
	}
	
	public String getConfig(String name, String defaultValue){
		String result = defaultValue;
		String tmp = myProperties.getProperty(name);
		if (tmp !=null && tmp.trim().length()>0){
			result = tmp;
		}
		return result;
	}
	
	private  File getFile() throws IOException{
		File file = new File(Constant.MGMT_DIR+Constant.SP+FILENAME);
		if (!file.exists()){
			file.createNewFile();
		}
		return file;
	}
}

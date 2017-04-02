package com.stockstrategy.constant;



public class Constant
{
	public static String DATA_DIR_SH = "C:/dave/new_yhzq_v21/vipdoc/sh/lday";
	public static String DATA_DIR_SZ = "C:/dave/new_yhzq_v21/vipdoc/sz/lday";
	public static String MGMT_DIR = "F:/documents/android/SimulatorDesignFiles/mgmt";

	public static String getResultsDir() {
		return MGMT_DIR + "/results";
	}

	public static String getStrategyDir() {
		return MGMT_DIR + "/strategies";
	}
	
	public static boolean ENABLE_SH=true;
	public static boolean ENABLE_SZ=true;
	
	public static final String START = "20080101";
	public static final String END   = "20111231";

	public static final String SP = "/";
	public static final String STOCK_LIST = "stocklist.txt";
	public static String STOCKCODE_PREFIX = "";
	
	public static final String PREFIX_SH = "sh";
	public static final String PREFIX_SZ = "sz";
	public static final String SUFFIX = ".day";

	public static int THREADNUM=1;

	public static String DATA_STORE_ADDRESS = "http://127.0.0.1:8012/StockDataStore";
	
	public static final String OPEN = "OPEN";
	public static final String CLOSE = "CLOSE";
	public static final String HIGH = "HIGH";
	public static final String LOW = "LOW";
	public static final String VOL = "VOL";
	public static final String [] rawTypes = {OPEN,CLOSE,HIGH,LOW ,VOL };

	
	public static final String MA3 = "MA3";
	public static final String MA5 = "MA5";
	public static final String MA10 = "MA10";
	public static final String MA20 = "MA20";
	public static final String MA30 = "MA30";
	public static final String MA60 = "MA60";
	public static final String MA120 = "MA120";
	public static final String MA200 = "MA200";
	public static final String MA250 = "MA250";
	public static final String MAVOL5 = "MAVOL5";// should be exactly the same with the class name
	public static final String MAVOL10 = "MAVOL10";
	public static final String MAVOL20 = "MAVOL20";
	public static final String MAVOL30 = "MAVOL30";
	public static final String MAVOL60 = "MAVOL60";
	public static final String MAVOL120 = "MAVOL120";
	public static final String LargeVol = "LargeVol";
	public static final String LargeVol30 = "LargeVol30";
	public static final String PACKAGENAME = Constant.class.getPackage().getName();
	public static final String PCKAGEPREFIX = PACKAGENAME.substring(0, PACKAGENAME.lastIndexOf("constant"));
	public static final String STATISTICCLASSPREFIX =PCKAGEPREFIX+"statistic.data.";
	//public static final String STATISTICCLASSPREFIX ="com.stockstrategy.statistic.data.";
	public static final String [] BASICSTATISTICSTYPES = { MA3,MA5,MA10,MA20,MA30, MA60,MA120,MA200,MA250, MAVOL5,MAVOL10,MAVOL20,MAVOL30,MAVOL60,MAVOL120, LargeVol, LargeVol30};
	public static final String [] MASTATISTICSTYPES = { MA3, MA5,MA10,MA20,MA30, MA60,MA120,MA200,MA250};
	public static final String [] VOLMASTATISTICTYPES = {MAVOL5,MAVOL10,MAVOL20,MAVOL30,MAVOL60,MAVOL120, LargeVol, LargeVol30};
	
	
	
	public static final String MACDDIF = "MACDDIF";
	public static final String MACDDEA = "MACDDEA";
	public static final String MACD = "MACD";
	
	public static final String KDJRSV = "KDJRSV";
	public static final String KDJK = "KDJK";
	public static final String KDJD = "KDJD";
	public static final String KDJJ = "KDJJ";
	
	public static final String Percentage3OneDayOrClose = "Percentage3OneDayOrClose";
	public static final String ForceBuyPrice = "ForceBuyPrice";
	public static final String RiseStopPrice = "RiseStopPrice";
		
	public static final String [] ADVANCEDTATISTICSTYPES = {MACDDIF, MACDDEA, MACD, KDJRSV,KDJK,KDJD,KDJJ, Percentage3OneDayOrClose, RiseStopPrice, ForceBuyPrice};
	
	
	
	
	public static final String SCROSS510 = "SCROSS510"; //ma5 and ma10 cross 
	public static final String SCROSS510UP = "SCROSS510UP"; //buy: ma5up and ma10up cross; sell: ma5 and ma10 cross 
	public static final String SCROSS510UP30UP = "SCROSS510UP30UP";//buy: ma5up and ma10up cross, 30up; sell: ma5 and ma10 cross 
	public static final String SC510L30V16 = "SC510L30V16"; 
	public static final String SC510MA530V23 = "SC510MA530V23";
	public static final String SV23A = "SV23A";
	public static final String SC510MA530V23a="SC510MA530V23a";
	public static final String SC510MA530V23b = "SC510MA530V23b";
	public static final String SC510MA530V23c = "SC510MA530V23c";
	public static final String SC510MA530V23d="SC510MA530V23d";
	public static final String SLevel1="SLevel1";
	public static final String SLevel1b="SLevel1b";
	public static final String SLevel3="SLevel3";
	public static final String SLevel4="SLevel4";
	public static final String SLevel4HisTester="SLevel4HisTester";
	public static final String SLevel5="SLevel5";
	public static final String SLevel5HisTester="SLevel5HisTester";
	public static final String SBreak="SBreak";
	public static final String SBreakB="SBreakB";
	
	
	public static final String Stesta = "Stesta";
	public static final String Stestb = "Stestb";
	public static final String Stestc = "Stestc";
	public static final String Stestd = "Stestd";
	public static final String Steste = "Steste";
	public static final String StestNaiveBayse = "StestNaiveBayse";
	public static final String StestNaiveBayse2 = "StestNaiveBayse2";
	public static final String StestSGD = "StestSGD";
	
	
	public static final String SPre30Gain10_01 = "Pre30Gain10.S_01";
	public static final String SPre30Gain10_02 = "Pre30Gain10.S_02";
	public static final String SPre30Gain10_03 = "Pre30Gain10.S_03";
	public static final String SPre30Gain10_04 = "Pre30Gain10.S_04";
	public static final String SPre30Gain10_05 = "Pre30Gain10.S_05";
	public static final String SPre30Gain10_06 = "Pre30Gain10.S_06";
	public static final String SPre30Gain10_07 = "Pre30Gain10.S_07";
	public static final String SPre30Gain10_08 = "Pre30Gain10.S_08";
	public static final String SPre30Gain10_09 = "Pre30Gain10.S_09";
	public static final String SPre30Gain10_10 = "Pre30Gain10.S_10";
	public static final String SPre30Gain10_11 = "Pre30Gain10.S_11";
	public static final String SPre30Gain10_12 = "Pre30Gain10.S_12";
	public static final String SPre30Gain10_13 = "Pre30Gain10.S_13";
	public static final String SPre30Gain10_14 = "Pre30Gain10.S_14";
	public static final String SPre30Gain10_15 = "Pre30Gain10.S_15";
	public static final String SPre30Gain10_16 = "Pre30Gain10.S_16";
	public static final String SPre30Gain10_17 = "Pre30Gain10.S_17";
	public static final String SPre30Gain10_18 = "Pre30Gain10.S_18";
	public static final String SPre30Gain10_19 = "Pre30Gain10.S_19";
	public static final String SPre30Gain10 = "Pre30Gain10.SAggregated";
	public static final String SPre30Gain10RealBuy = "Pre30Gain10.SPre30Gain10RealBuy";
	
	
	
	public static final String SPre30Gain5I = "Pre30Gain5.S_01";
	public static final String SPre30Gain5II = "Pre30Gain5.S_02";
	public static final String SPre30Gain5III = "Pre30Gain5.S_03";
	public static final String SPre30Gain5IV = "Pre30Gain5.S_04";
	public static final String SPre30Gain5V = "Pre30Gain5.S_05";
	public static final String SPre30Gain5VI = "Pre30Gain5.S_06";
	public static final String SPre30Gain5VII = "Pre30Gain5.S_07";
	public static final String SPre30Gain5VIII = "Pre30Gain5.S_08";
	public static final String SPre30Gain5IX = "Pre30Gain5.S_09";
	public static final String SPre30Gain5X = "Pre30Gain5.S_10";
	public static final String SPre30Gain5_11 = "Pre30Gain5.S_11";
	public static final String SPre30Gain5_12 = "Pre30Gain5.S_12";
	public static final String SPre30Gain5_13 = "Pre30Gain5.S_13";
	public static final String SPre30Gain5_14 = "Pre30Gain5.S_14";
	public static final String SPre30Gain5_15 = "Pre30Gain5.S_15";
	public static final String SPre30Gain5_16 = "Pre30Gain5.S_16";
	public static final String SPre30Gain5_17 = "Pre30Gain5.S_17";
	public static final String SPre30Gain5_18 = "Pre30Gain5.S_18";
	public static final String SPre30Gain5_19 = "Pre30Gain5.S_19";
	public static final String SPre30Gain5_20 = "Pre30Gain5.S_20";
	public static final String SPre30Gain5_21 = "Pre30Gain5.S_21";
	public static final String SPre30Gain5_22 = "Pre30Gain5.S_22";
	public static final String SPre30Gain5_23 = "Pre30Gain5.S_23";
	public static final String SPre30Gain5_24 = "Pre30Gain5.S_24";
	public static final String SPre30Gain5_25 = "Pre30Gain5.S_25";
	public static final String SPre30Gain5_26 = "Pre30Gain5.S_26";
	public static final String SPre30Gain5_27 = "Pre30Gain5.S_27";
	public static final String SPre30Gain5_28 = "Pre30Gain5.S_28";
	public static final String SPre30Gain5_29 = "Pre30Gain5.S_29";
	public static final String SPre30Gain5_30 = "Pre30Gain5.S_30";
	public static final String SPre30Gain5_31 = "Pre30Gain5.S_31";
	public static final String SPre30Gain5_32 = "Pre30Gain5.S_32";
	public static final String SPre30Gain5_33 = "Pre30Gain5.S_33";
	public static final String SPre30Gain5_34 = "Pre30Gain5.S_34";
	public static final String SPre30Gain5 = "Pre30Gain5.SAggregated";
//	public static final String SPre30Gain5 = "Pre30Gain5.SPreGainAggregated";
	public static final String SPre30Gain5RealBuy = "Pre30Gain5.SPre30Gain5RealBuy";
	public static final String SPre30Gain5SellAtPer3 = "Pre30Gain5.SPre30Gain5SellAtPer3";
	
	
	public static final String SPre20Gain5_01 = "Pre20Gain5.S_01";
	public static final String SPre20Gain5_02 = "Pre20Gain5.S_02";
	public static final String SPre20Gain5_03 = "Pre20Gain5.S_03";
	public static final String SPre20Gain5_04 = "Pre20Gain5.S_04";
	public static final String SPre20Gain5_05 = "Pre20Gain5.S_05";
	public static final String SPre20Gain5_06 = "Pre20Gain5.S_06";
	public static final String SPre20Gain5_07 = "Pre20Gain5.S_07";
	public static final String SPre20Gain5_08 = "Pre20Gain5.S_08";
	public static final String SPre20Gain5_09 = "Pre20Gain5.S_09";
	public static final String SPre20Gain5_10 = "Pre20Gain5.S_10";
	public static final String SPre20Gain5_11 = "Pre20Gain5.S_11";
	public static final String SPre20Gain5_12 = "Pre20Gain5.S_12";
	public static final String SPre20Gain5 = "Pre20Gain5.SAggregated";
	
	public static final String SPre10Gain5_01 = "Pre10Gain5.S_01";
	public static final String SPre10Gain5_02 = "Pre10Gain5.S_02";
	public static final String SPre10Gain5_03 = "Pre10Gain5.S_03";
	public static final String SPre10Gain5_04 = "Pre10Gain5.S_04";
	public static final String SPre10Gain5_05 = "Pre10Gain5.S_05";
	public static final String SPre10Gain5_06 = "Pre10Gain5.S_06";
	public static final String SPre10Gain5_07 = "Pre10Gain5.S_07";
	public static final String SPre10Gain5_08 = "Pre10Gain5.S_08";
	public static final String SPre10Gain5_09 = "Pre10Gain5.S_09";
	public static final String SPre10Gain5_10 = "Pre10Gain5.S_10";
	public static final String SPre10Gain5_11 = "Pre10Gain5.S_11";
	public static final String SPre10Gain5_12 = "Pre10Gain5.S_12";
	public static final String SPre10Gain5_13 = "Pre10Gain5.S_13";
	public static final String SPre10Gain5_14 = "Pre10Gain5.S_14";
	public static final String SPre10Gain5_15 = "Pre10Gain5.S_15";
	public static final String SPre10Gain5_16 = "Pre10Gain5.S_16";
	public static final String SPre10Gain5_17 = "Pre10Gain5.S_17";
	public static final String SPre10Gain5_18 = "Pre10Gain5.S_18";
	public static final String SPre10Gain5_19 = "Pre10Gain5.S_19";
	public static final String SPre10Gain5_20 = "Pre10Gain5.S_20";
	public static final String SPre10Gain5_21 = "Pre10Gain5.S_21";
	public static final String SPre10Gain5_22 = "Pre10Gain5.S_22";
	public static final String SPre10Gain5_23 = "Pre10Gain5.S_23";
	public static final String SPre10Gain5_24 = "Pre10Gain5.S_24";
	public static final String SPre10Gain5_25 = "Pre10Gain5.S_25";
	public static final String SPre10Gain5_26 = "Pre10Gain5.S_26";
	public static final String SPre10Gain5_27 = "Pre10Gain5.S_27";
	public static final String SPre10Gain5_28 = "Pre10Gain5.S_28";
	public static final String SPre10Gain5_29 = "Pre10Gain5.S_29";
	public static final String SPre10Gain5_30 = "Pre10Gain5.S_30";
	public static final String SPre10Gain5_31 = "Pre10Gain5.S_31";
	public static final String SPre10Gain5_32 = "Pre10Gain5.S_32";
	public static final String SPre10Gain5_33 = "Pre10Gain5.S_33";
	public static final String SPre10Gain5_34 = "Pre10Gain5.S_34";
	public static final String SPre10Gain5_35 = "Pre10Gain5.S_35";
	public static final String SPre10Gain5_36 = "Pre10Gain5.S_36";
	public static final String SPre10Gain5_37 = "Pre10Gain5.S_37";
	public static final String SPre10Gain5 = "Pre10Gain5.SAggregated";
	
	
	public static final String Stesta_5 = "Stesta_5";
	public static final String Stestb_5 = "Stestb_5";
	public static final String Stestc_5 = "Stestc_5";
	public static final String Stestd_5 = "Stestd_5";
	public static final String Steste_5 = "Steste_5";
	
	public static String [] CUSTOMSTATISTICSTYPES = {};

	public static String [] AGGREGATEDSTATISTICSTYPES = {};

	
	///////////////////////  Delay not lager than 5 !
	public static final int DELAY0  = 0; //action buy or sell delayed by 1 day ---- 
	public static final int ACCOUNTINITMONEY = 1000000; //Initial money for the account
	
	public static boolean debug = false;
	
}
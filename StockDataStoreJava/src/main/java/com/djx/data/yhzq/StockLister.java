package com.djx.data.yhzq;

import com.stockstrategy.http.StockListItem;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.tools.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StockLister {
	public static final String SH000001 = "999999";
	public static final String SZ000001 = "399001";
	public static final String CY000001 = "399006";

	protected static final HashMap<String, String> stockFilePathMap = new HashMap<>();
	
	
	public List<StockListItem> getSharedStockList(){
		List<StockListItem> stockCodes = new ArrayList<>();
		stockCodes.add(new StockListItem(SH000001));
		stockCodes.add(new StockListItem(SZ000001));
		stockCodes.add(new StockListItem(CY000001));
		return stockCodes;
	} 
	
    public List<StockListItem> getStockList() {
        List<StockListItem> stockCodes = new ArrayList<>();
        if (Constant.ENABLE_SH){
        	stockCodes.addAll(getFileList(Constant.DATA_DIR_SH));
        }
        if (Constant.ENABLE_SZ){
        	stockCodes.addAll(getFileList(Constant.DATA_DIR_SZ));
        }
        
        return stockCodes;
    }
    
    protected List<StockListItem> getFileList(String dir){
    	List<StockListItem> list = new ArrayList<>();
    	File[] files = new File(dir).listFiles();
        for (File file : files) {
            String name = file.getName();
            if (name.startsWith(Constant.PREFIX_SH)){
            	name = name.substring(2);
                if (name.endsWith(Constant.SUFFIX)) {
                    String code = name.substring(0, name.length() - 4);
                    //black list
                    if (code.equals("600369") || code.equals("600757")
                            || code.equals("600553") || code.equals("600001")
                            || code.equals("600455") || code.equals("600003")) {
                        continue;
                    }
                    //white list
                    if (
                            code.startsWith("600") ||
                                    code.startsWith("601")||
                                    code.startsWith("603")
                           ) {
						list.add(new StockListItem(code, getCompositeCode(code)));
						stockFilePathMap.put(code, file.getAbsolutePath());
                    }
                }
            }else if (name.startsWith(Constant.PREFIX_SZ)){
            	name = name.substring(2);
                if (name.endsWith(Constant.SUFFIX)) {
                    String code = name.substring(0, name.length() - 4);
                    //black list
                    //white list
                    if (code.startsWith("002") || code.startsWith("300")
                            || code.startsWith("000")
                    		) {
						list.add(new StockListItem(code, getCompositeCode(code)));
						stockFilePathMap.put(code, file.getAbsolutePath());
                    }
                }
            }
            
        }
		return list;
    }
    
    public String getFilePathByStockCode(String stockCode){
    	if (stockFilePathMap.isEmpty()) {
			getStockList();
		}

    	String filePath = stockFilePathMap.containsKey(stockCode)?stockFilePathMap.get(stockCode):null;
    	if (filePath == null || !FileUtils.exist(filePath)){
    		filePath = Constant.DATA_DIR_SH + "/" + Constant.PREFIX_SH + stockCode + Constant.SUFFIX;
    	}

		if (!FileUtils.exist(filePath)) {
    		filePath = Constant.DATA_DIR_SZ + "/" + Constant.PREFIX_SZ + stockCode + Constant.SUFFIX;
        }
    	
    	if (filePath != null && FileUtils.exist(filePath)){
    		return filePath;
    	}else{
    		return null;
    	}
    }

	protected static String getCompositeCode(String stockCode) {
		String StockCode001 = SH000001;
		if (stockCode.startsWith("600") ||
				stockCode.startsWith("601")||
				stockCode.startsWith("603")) {
			StockCode001 = SH000001;
		} else if (stockCode.startsWith("000") || stockCode.startsWith("002")) {
			StockCode001 = SZ000001;
		} else if (stockCode.startsWith("300")) {
			StockCode001 = CY000001;
		}
		return StockCode001;
	}
}

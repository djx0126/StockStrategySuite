package com.stockstrategy.file;

import com.stockstrategy.constant.Constant;
import com.stockstrategy.http.HttpClient;
import com.stockstrategy.http.StockListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StockLister {
	private static List<String> compositeCodeList = new ArrayList<>();
	private static HashMap<String, String> compositeIndexCodeMap = new HashMap<>();

	private static List<String> loadSharedList() {
		String url = Constant.DATA_STORE_ADDRESS + "/shared_list";
		List<StockListItem> sharedStockListItemList = HttpClient.getInstance().getList(url, StockListItem.class);
		return sharedStockListItemList.stream().map(StockListItem::getCode).collect(Collectors.toList());
	}

	public static List<String> getSharedStockList(){
		return loadSharedList();
	}

	public static String getCompositeIndexCode(String code) {
		return compositeIndexCodeMap.get(code);
	}
	
    public static List<String> getStockList() {
		compositeCodeList = new ArrayList<>();
		compositeIndexCodeMap = new HashMap<>();
		String url = Constant.DATA_STORE_ADDRESS + "/list";
		List<StockListItem> stockListItems = HttpClient.getInstance().getList(url, StockListItem.class);

		if (!Constant.STOCKCODE_PREFIX.isEmpty()) {
			String[] prefixStrs = Constant.STOCKCODE_PREFIX.split(",");
			stockListItems = stockListItems.stream().filter(li -> Arrays.stream(prefixStrs).anyMatch(pre->li.getCode().startsWith(pre))).collect(Collectors.toList());
		}

		stockListItems.stream().forEach(stockListItem -> {
			compositeCodeList.add(stockListItem.getCompositeIndexCode());
			compositeIndexCodeMap.put(stockListItem.getCode(), stockListItem.getCompositeIndexCode());
		});

		return stockListItems.stream().map(StockListItem::getCode).collect(Collectors.toList());
    }
}

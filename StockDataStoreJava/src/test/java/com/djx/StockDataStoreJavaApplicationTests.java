package com.djx;

import com.djx.job.list.ListJobController;
import com.djx.data.yhzq.StockLister;
import com.stockstrategy.http.RawDayDataItem;
import com.stockstrategy.http.StockListItem;
import com.stockstrategy.http.HttpClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StockDataStoreJavaApplicationTests {
	@LocalServerPort
	int port;

	@Test
	public void testLister() {
		System.out.println("Test on port: " + port);

		String baseUrl = "http://127.0.0.1:" + port + "/StockDataStore";

		String url = baseUrl + ListJobController.LIST_PATH;
		List<StockListItem> stockListItemList = HttpClient.getInstance().getList(url, StockListItem.class);
		System.out.println(stockListItemList.size());
		Assert.assertTrue(stockListItemList.size() > 0);

		Assert.assertTrue(stockListItemList.stream().filter(s -> s.getCode().equals("600000")).findAny().isPresent());
        String compositeIndexCode = stockListItemList.stream().filter(s -> s.getCode().equals("600000")).findAny().get().getCompositeIndexCode();
        Assert.assertTrue(compositeIndexCode.equals("999999"));

        Assert.assertTrue(stockListItemList.stream().filter(s -> s.getCode().equals("000002")).findAny().isPresent());
        compositeIndexCode = stockListItemList.stream().filter(s -> s.getCode().equals("000002")).findAny().get().getCompositeIndexCode();
        Assert.assertTrue(compositeIndexCode.equals("399001"));

        Assert.assertTrue(stockListItemList.stream().filter(s -> s.getCode().equals("300059")).findAny().isPresent());
        compositeIndexCode = stockListItemList.stream().filter(s -> s.getCode().equals("300059")).findAny().get().getCompositeIndexCode();
        Assert.assertTrue(compositeIndexCode.equals("399006"));

		String sharedListPath = ListJobController.SHARED_LIST_PATH;
		url = baseUrl + sharedListPath;
		List<StockListItem> sharedStockListItemList = HttpClient.getInstance().getList(url, StockListItem.class);
		System.out.println(sharedStockListItemList);
		StockLister lister = new StockLister();
		Assert.assertTrue(sharedStockListItemList.size() == lister.getSharedStockList().size());

	}

    @Test
    public void testDataReader() {
        System.out.println("Test on port: " + port);

        String baseUrl = "http://127.0.0.1:" + port + "/StockDataStore";

        String url = baseUrl + ListJobController.DATA_PATH;
        Map<String, Object> params = new HashMap<>();
        params.put("code", "999999");
        params.put("start", "20161114");
        params.put("end", "20161228");
        List<RawDayDataItem> stockListItemList = HttpClient.getInstance().getList(url, params, RawDayDataItem.class);
        System.out.println(stockListItemList.size());
        Assert.assertTrue(stockListItemList.size() == 33 );

        Assert.assertTrue(stockListItemList.get(0).get("date").equals("20161114"));
        Assert.assertTrue(String.valueOf(stockListItemList.get(0).get("CLOSE")).equals("3210.37"));
        Assert.assertTrue(String.valueOf(stockListItemList.get(32).get("CLOSE")).equals("3102.24"));
    }

}

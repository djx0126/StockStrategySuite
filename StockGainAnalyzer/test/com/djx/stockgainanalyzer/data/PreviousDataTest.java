/**
 * 
 */
package com.djx.stockgainanalyzer.data;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author dave
 *
 */
public class PreviousDataTest {

	@Test
	public void testGet() {
		PreviousData pData = new PreviousData();
		pData.setClose(1.01f);
		double price = pData.getClose();
		assertEquals(1.01, price, 1e-6f);
	}
	
	@Test
	public void testSet() {
		PreviousData pData = new PreviousData();
		pData.setClose(1.01f);
		double price = pData.getClose();
		assertEquals(1.01, price, 1e-6f);
	}
	
	@Test
	public void testToString() {
		PreviousData pData = new PreviousData();
		pData.setClose(1.01f);
		pData.setCloseOpen(2.03f);
		pData.setVol(3.003f);
		StockGainData data = new StockGainData("000001", "20131018", 1.002f);
		data.setId(56);
		String pDataString = pData.toString();
		assertTrue(pDataString.contains("close"));
		assertTrue(pDataString.contains("1.0"));
		assertTrue(pDataString.contains("closeOpen"));
		assertTrue(pDataString.contains("2.0"));
		assertTrue(pDataString.contains("vol"));
		assertTrue(pDataString.contains("3.0"));
	}

}

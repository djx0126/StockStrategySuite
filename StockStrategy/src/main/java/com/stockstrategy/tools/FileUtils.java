package com.stockstrategy.tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class FileUtils {
	
	public static File tryGetFile(String dirName, String fileName) throws IOException{
		File retFile = null;
		if (exist(dirName, fileName)){
			retFile = getFile(dirName, fileName);
		}
		return retFile;
	}
	
	public static File getFile (String dirName, String fileName) throws IOException{
		return prepareFile(dirName, fileName, false);
	}
	
	public static File prepareFile (String dirName, String fileName) throws IOException{
		return prepareFile(dirName, fileName, true);
	}
	
	public static File prepareFile (String dirName, String fileName, boolean toCreate) throws IOException{
		File resultFile = null;
		File stockFile = new File(dirName+"/"+fileName);
		if(stockFile.exists() && stockFile.isFile() && stockFile.canRead()){
			resultFile = stockFile;
		}else if (!stockFile.exists() && toCreate){
			stockFile.createNewFile();
			resultFile = stockFile;
		}
		return resultFile;
	}
	
	public static void writeFile(File outputFile, String source){
		OutputStream os = null;
		try {
			os = new FileOutputStream(outputFile);
			writeFile(os,  source);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			if (os!=null){
				try {
					os.flush();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void writeFile(OutputStream os, String source){
		BufferedOutputStream bos = new BufferedOutputStream(os);
		PrintWriter pw = new PrintWriter(bos);
		pw.append(source);
		pw.flush();
	}
	
	public static boolean exist(String dirName, String fileName){
		return exist(dirName+"/"+fileName);
	}
	
	public static boolean exist(String filePath){
		File file = new File(filePath);
		return file.exists()&&file.isFile();
	}
}

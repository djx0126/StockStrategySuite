package com.djx.stockgainanalyzer;

import com.djx.stockgainanalyzer.data.Field;
import com.stockstrategy.constant.Constant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by Administrator on 2015/6/28.
 */
public class FileHelper {

    public static final File prepareDir(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String today = formatter.format(new Date());

        File dir = new File(Constant.getResultsDir().replace("\\", "/") + "/" + today);
        if (!dir.exists()){
            dir.mkdir();
        }
        return dir;
    }

    public static final void writeResult(int pre, int gain, Field[] params, double[][] offsetParams, double[][] scaleParams, Result result, String log){
        File dir = prepareDir();
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
        String nowTime = formatter.format(new Date());
        String today = dir.getName();
        String fileName = dir.getAbsolutePath().replace("\\", "/") + "/" + nowTime +".txt";

        try (PrintWriter writer = new PrintWriter(new File(fileName))){
            writer.println("#" + result.toString());
            writer.println("type=ConfigBasedPreGainStrategy");
            writer.println("pre=" + String.valueOf(pre));
            writer.println("gain=" + String.valueOf(gain));
            writer.println("#creationDate=" + today);

            for(Field field:params){
                writer.println(field.getName()+"Offset" + "=" + Arrays.toString(offsetParams[field.getIdx()]).replace("[", "").replace("]", ""));
            }
            for(Field field:params){
                writer.println(field.getName()+"Scale" + "=" + Arrays.toString(scaleParams[field.getIdx()]).replace("[", "").replace("]", ""));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static final void writeLog(String log){
        String nowTime = Utils.nowStr();
        writeLog(nowTime, log);
    }

    public static final void writeLog(String fileName, String log){
        File dir = prepareDir();
        fileName = dir.getAbsolutePath().replace("\\", "/") + "/" + fileName;

        if (!fileName.matches(".*\\.[a-z]{3}$")) { // if no suffix, add '.txt'
            fileName = fileName + ".txt";
        }

        File file = new File(fileName);

        boolean appendMode = false;
        if (file.exists()) {
            appendMode = true;
        }

        try (FileWriter writer = new FileWriter(file, appendMode)){
            if (appendMode) {
                writer.append(log + "\n");
            } else {
                writer.write(log + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

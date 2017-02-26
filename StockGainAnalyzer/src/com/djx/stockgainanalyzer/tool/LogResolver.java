package com.djx.stockgainanalyzer.tool;


import com.stockstrategy.tools.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogResolver {
    public static int pre = 3;
    public static int gain = 2;
    public static int index = 0;


    public static String date;
    public static double accuracy = 0d;
    public static String endingLog;
    public static Map<String, String> params;

    public static void main(String[] args) {
        System.out.println("start");
        File resultsFile = new File("results.txt");
        if (!resultsFile.exists()) {
            System.out.println("file not exist!" + resultsFile.getAbsolutePath());
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(resultsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("end");
    }

    private static void processLine(String line) {
        if (line.startsWith("Enter the end date")) {
            processDate(line);
        } else if (line.contains("end run with")) {
            processEndLine(line);
        } else if (line.startsWith("private static double")) {
            processParams(line);
        } else if (line.startsWith("pre_num=")) {
            pre = Integer.parseInt(line.replace("pre_num=", ""));
        } else if (line.startsWith("gain_num=")) {
            gain = Integer.parseInt(line.replace("gain_num=", ""));
        }
    }

    private static void processParams(String line) {
        Pattern pattern = Pattern.compile("private static double\\[\\] (.*)=\\{(.*)\\}");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String fieldName = matcher.group(1);
            String fieldParam = matcher.group(2);

            if (fieldName.equals("closeOffset")) {
                params = new HashMap<>();
            }

            params.put(fieldName, fieldParam);

            if (fieldName.equals("volScale") && accuracy >= 70.0d) {
                writeResult();
            }
        }
    }

    private static void writeResult() {
        File dir = new File(String.valueOf(pre) + "_" + String.valueOf(gain) + "_" + date);
        if (!dir.exists()) {
            dir.mkdir();
            index = 1;
        } else if (index == 0) {
            File[] childrenFiles = dir.listFiles();
            if (childrenFiles.length > 0) {
                int maxInt = Arrays.stream(childrenFiles).map(f -> f.getName().replace(".txt", "")).mapToInt(s -> Integer.parseInt(s)).max().getAsInt();
                index = maxInt + 1;
            } else {
                index = 1;
            }

        }
        String fileName = String.format("%04d", index) + ".txt";

        try (PrintWriter writer = new PrintWriter(new File(dir.getPath() + "/" + fileName))) {
            writer.println("#" + endingLog);
            writer.println("type=ConfigBasedPreGainStrategy");
            writer.println("pre=" + String.valueOf(pre));
            writer.println("gain=" + String.valueOf(gain));
            writer.println("#creationDate=" + date);

            params.forEach((k, v) -> writer.println(k + "=" + v));
        } catch (IOException e) {
            e.printStackTrace();
        }

        index++;
        System.out.println("write to file: " + fileName);
    }

    private static void processEndLine(String line) {
        endingLog = line;
        Pattern pattern = Pattern.compile("accuracy=(.*)\\)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            accuracy = Double.parseDouble(matcher.group(1));
        } else {
            System.out.println("not found");
        }
    }

    private static void processDate(String line) {
        String today = Utils.today();
        date = today;
        System.out.println("date = " + date);

//        Pattern pattern = Pattern.compile("Enter the end date.*\\[(\\d+)");
//        Matcher matcher = pattern.matcher(line);
//        if (matcher.find()) {
//            date = matcher.group(1);
//            System.out.println("date = " + date);
//        } else {
//            System.out.println("not found");
//        }
    }


}


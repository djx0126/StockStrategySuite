package com.stockstrategy.statistic.data;

import com.stockstrategy.constant.Configurer;
import com.stockstrategy.constant.Constant;

import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2015/6/16.
 */
public class ConfigBasedStrategyFactory {
    public static Map<String, Properties> strategies;
    public static ArrayList<String> strategyNames;
    public static final String CONFIG_BASED_PRE_GAIN_STRATEGY = "ConfigBasedPreGainStrategy";
    public static final String CONFIG_BASED_AGGREGATED_PRE_GAIN_STRATEGY = "ConfigBasedAggregatedPreGainStrategy";
    public static final String CONFIG_ARRAY_PRE_GAIN_STRATEGY = "ConfigArrayPreGainStrategy";
    public static final String[] CONFIG_STRATEGY_TYPES = {CONFIG_BASED_PRE_GAIN_STRATEGY, CONFIG_BASED_AGGREGATED_PRE_GAIN_STRATEGY, CONFIG_ARRAY_PRE_GAIN_STRATEGY};


    public static String[] getStrategyNames(){
        if (strategies == null){
            return new String[0];
        }else{
            String[] strArr = new String[strategyNames.size()];
            strategyNames.toArray(strArr);
            return strArr;
        }
    }

    public final static void loadStrategyProperties(){
        loadStrategyProperties(null);
    }

    private final static void loadStrategyProperties(String rootFolderPath){
        if (rootFolderPath == null){
            strategies = new HashMap<>();
            strategyNames = new ArrayList<>();
            rootFolderPath = Constant.getStrategyDir();
        }
        File rootFolder = new File(rootFolderPath);
        final String rootPath = rootFolderPath.replace("\\", "/");

        if (!rootFolder.exists()){
            return;
        }

        File[] children = rootFolder.listFiles();
        Arrays.stream(children).sorted((o1, o2) -> {
            boolean o1Dir = o1.isDirectory(); // dir < file, load dir before file
            boolean o2Dir = o2.isDirectory();
            if (o1Dir && !o2Dir){
                return -1;
            }else if (o2Dir && !o1Dir){
                return 1;
            }
            return o1.getName().compareTo(o2.getName());
        }).forEach(file -> {
            if (file.isDirectory()) {
                loadStrategyProperties(rootPath + "/" + file.getName());
            } else {
                String fileFullPath = rootPath + "/" + file.getName();
                Properties properties = new Properties();
                try {
                    properties.load(new FileReader(fileFullPath));
                    String activeStr = properties.getProperty("active");
                    if (activeStr != null && activeStr.toUpperCase().equals("N")) {
                        return;
                    }

                    String type = properties.getProperty("type");
                    if (type == null || Arrays.stream(CONFIG_STRATEGY_TYPES).noneMatch(s -> s.equals(type))) {
                        return;
                    }

                    String strategyName = properties.getProperty("name");
                    strategyName = strategyName != null ? strategyName : fileFullPath.replace(Constant.getStrategyDir() + "/", "").replace(".txt", "").replace("/", ".");
                    System.out.println("Strategy " + strategyName + " loaded from file: " + fileFullPath);
                    properties.setProperty("fileFullPath", fileFullPath);
                    strategies.put(strategyName, properties);
                    strategyNames.add(strategyName);
                    Configurer.addStrategy(strategyName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public final synchronized static AbstractStatisticData buildConfigBasedStrategy(String statisticType){
        AbstractStatisticData strategy = null;
        Properties properties;
        if (strategies.containsKey(statisticType)){
            properties = strategies.get(statisticType);
        }else{
            if (Constant.debug == true) {
                System.out.println("build config based strategy:" + statisticType);
            }
            String fileFullPath = Constant.getStrategyDir() + "/" + statisticType;
            fileFullPath = fileFullPath.replace(".", "/") + ".txt";
            properties = new Properties();
            try {
                properties.load(new FileReader(fileFullPath));
                properties.setProperty("fileFullPath", fileFullPath);
                strategies.put(statisticType, properties);
            } catch (IOException e) {
                System.err.println("Failed to load properties from file: "+ fileFullPath);
                e.printStackTrace();
            }
        }

        String type = properties.getProperty("type");

        if (properties.getProperty("name") == null) {
            properties.setProperty("name", statisticType);
        }

        if (type.equals(CONFIG_BASED_PRE_GAIN_STRATEGY)){
            strategy = ConfigBasedPreGainStrategy.build(properties);
        }else if (type.equals(CONFIG_BASED_AGGREGATED_PRE_GAIN_STRATEGY)){
            strategy = ConfigBasedAggregatedPreGainStrategy.build(properties);
        }else if (type.equals(CONFIG_ARRAY_PRE_GAIN_STRATEGY)) {
            strategy = ConfigArrayPreGainStrategy.build(properties);
        }

        String creationDate = properties.getProperty("creationDate");
        if (creationDate != null){
            strategy.setStartDate(creationDate);
        }

        return strategy;
    }

    private static double[] parseDoubleArrayFromString(String doubleArrayString){
        if (doubleArrayString == null){
            return null;
        }
        String[] doubleStringArray = doubleArrayString.split(",");
        double[] doubleArray = Arrays.stream(doubleStringArray).mapToDouble(s -> Double.parseDouble(s.trim())).toArray();
        return doubleArray;
    }

    private static boolean parseBooleanFromString(String booleanString) {
        return parseBooleanFromString(booleanString, false);
    }

    private static boolean parseBooleanFromString(String booleanString, boolean defaultValue) {
        if ("true".equalsIgnoreCase(booleanString) || "y".equalsIgnoreCase(booleanString)) {
            return true;
        } else if ("false".equalsIgnoreCase(booleanString) || "n".equalsIgnoreCase(booleanString)) {
            return false;
        }
        return defaultValue;
    }

    private static int[] parseIntArrayFromString(String intArrayString){
        if (intArrayString == null){
            return null;
        }
        String[] intStringArray = intArrayString.split(",");
        int[] intArray = Arrays.stream(intStringArray).map(String::trim).filter(s->s.length()>0).mapToInt(s -> Integer.parseInt(s)).toArray();
        return intArray;
    }

    private static String[] parseStringArrayFromString(String cvs){
        if (cvs == null){
            return null;
        }
        String[] stringArray = cvs.split(",");
        Arrays.stream(stringArray).forEach(s -> s = s.trim());
        return stringArray;
    }

    private static double getLimitProperty(Properties properties){
        String limitStr = properties.getProperty("limit");
        if (limitStr != null ){
            return Double.parseDouble(limitStr);
        }else if (properties.getProperty("offsets")!=null) {
            double[] offsets = parseDoubleArrayFromString(properties.getProperty("offsets"));
            return offsets.length;
        }else {
            return Double.parseDouble(properties.getProperty("pre"));
        }
    }

    public static Map<String, Properties> getStrategyProperties() {
        return strategies;
    }

    public static void saveStrategyProperties(String strategyName, Map<String, String> newProperties) {
        Properties cachedProperties = strategies.get(strategyName);
        if (cachedProperties == null) {
            return;
        }
        newProperties.forEach((key, value) -> cachedProperties.setProperty(key, value));

        String fileFullPath = cachedProperties.getProperty("fileFullPath");

        saveProperties(fileFullPath, newProperties);
    }

    private static void saveProperties(String fileFullPath, Map<String, String> newProperties) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileFullPath))) {
            String line;
            while (( line = br.readLine())!=null){
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> comments = new ArrayList<>();
        Map<String, String> properties = new HashMap<>();
        for (String line: lines) {
            if (line.startsWith("#")) {
                comments.add(line);
            } else {
                String[] tokens = line.split("=");
                if (tokens.length == 2 && tokens[0].trim().length() > 0) {
                    properties.put(tokens[0].trim(), tokens[1].trim());
                }
            }
        }

        // merge properties
        newProperties.forEach((key,value)-> properties.put(key,value));

        properties.remove("fileFullPath");

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileFullPath))) {
            comments.stream().forEach(comment-> writer.println(comment));
            properties.forEach((key, value)-> writer.println(key+"="+value) );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /********************      sub classes           ************************/

    private static class ConfigBasedPreGainStrategy{
        public static AbstractSPreGain build(Properties properties){
            AbstractSPreGain strategy = new AbstractSPreGain(
                    properties.getProperty("name"),
                    Integer.parseInt(properties.getProperty("pre")),
                    Integer.parseInt(properties.getProperty("gain")),
                    getLimitProperty(properties),
                    parseDoubleArrayFromString(properties.getProperty("closeOffset")),
                    parseDoubleArrayFromString(properties.getProperty("volOffset")),
                    parseDoubleArrayFromString(properties.getProperty("openOffset")),
                    parseDoubleArrayFromString(properties.getProperty("highOffset")),
                    parseDoubleArrayFromString(properties.getProperty("lowOffset")),
                    parseDoubleArrayFromString(properties.getProperty("closeScale")),
                    parseDoubleArrayFromString(properties.getProperty("volScale")),
                    parseDoubleArrayFromString(properties.getProperty("openScale")),
                    parseDoubleArrayFromString(properties.getProperty("highScale")),
                    parseDoubleArrayFromString(properties.getProperty("lowScale"))
            ){};

            parseSkipBigChangeProperty(properties, strategy);
            parseForceBuyProperty(properties, strategy);
            strategy.setAvgByDay(parseBooleanFromString(properties.getProperty("avgByDay")));

            return strategy;
        }
    }

    private static class ConfigBasedAggregatedPreGainStrategy{
        public static AbstractAggregatedPreGain build(Properties properties){
            String strategies = properties.getProperty("strategies");
            String prefix = properties.getProperty("strategyprefix");
            String[] strategyArray;

            if (strategies != null){
                strategyArray = parseStringArrayFromString(properties.getProperty("strategies"));
            }else if (prefix != null){
                strategyArray = Arrays.stream(getStrategyNames()).filter(strategy->strategy.startsWith(prefix)).toArray(String[]::new);
            }else{
                return null;
            }

            AbstractAggregatedPreGain strategy =  new AbstractAggregatedPreGain(
                    properties.getProperty("name"),
                    Integer.parseInt(properties.getProperty("pre")),
                    Integer.parseInt(properties.getProperty("gain")),
                    strategyArray
            ){};

            parseSkipBigChangeProperty(properties, strategy);
            parseForceBuyProperty(properties, strategy);
            strategy.setAvgByDay(parseBooleanFromString(properties.getProperty("avgByDay")));

            return strategy;
        }
    }

    private static class ConfigArrayPreGainStrategy {
        public static AbstractSPreGain build(Properties properties){
            AbstractSPreGain strategy = new AbstractSPreGain(properties.getProperty("name"),
                    Integer.parseInt(properties.getProperty("pre")),
                    Integer.parseInt(properties.getProperty("gain")),
                    getLimitProperty(properties)){};
            double[] mean = parseDoubleArrayFromString(properties.getProperty("mean"));
            double[] stdV = parseDoubleArrayFromString(properties.getProperty("stdV"));
            strategy.setMeanAndStdV(mean, stdV);
            int[] dayFields = parseIntArrayFromString(properties.getProperty("dayFields"));
            int[] maFields = parseIntArrayFromString(properties.getProperty("maFields"));
            int[] overAllmaFields = parseIntArrayFromString(properties.getProperty("overAllmaFields"));
            double[] offsets = parseDoubleArrayFromString(properties.getProperty("offsets"));
            double[] scales = parseDoubleArrayFromString(properties.getProperty("scales"));
            strategy.setConfigArray(offsets, scales, dayFields, maFields, overAllmaFields);

            parseSkipBigChangeProperty(properties, strategy);
            parseForceBuyProperty(properties, strategy);

            strategy.setAvgByDay(parseBooleanFromString(properties.getProperty("avgByDay")));

            return strategy;
        }
    }

    private static void parseForceBuyProperty(Properties properties, AbstractSPreGain strategy) {
        String forceBuyStr = properties.getProperty("forceBuy");
        if (forceBuyStr != null) {
            strategy.setForceBuy(parseBooleanFromString(forceBuyStr));
        }
    }

    private static void parseSkipBigChangeProperty(Properties properties, AbstractSPreGain strategy) {
        String skipBigChangeStr = properties.getProperty("skipBigChange");
        if (skipBigChangeStr != null) {
            strategy.setSkipBigChange(parseBooleanFromString(skipBigChangeStr));
        }
    }
}

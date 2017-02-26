package com.stockstrategy.constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dave on 2016/5/30.
 */
public class ArgParser {
    private static String INIT_CONFIG_FILE_NAME = "configures.txt";

    public static void loadInitConfigures(String[] args, Class clazz, String... argNamesToConfig) {
        Properties staticConfigures = new Properties();

        Arrays.stream(args).forEach(s -> parseArgsString(s.toLowerCase(), staticConfigures));

        tryLoadFromLocalFile(staticConfigures);

        if (argNamesToConfig.length == 0) {
            argNamesToConfig = Arrays.stream(clazz.getDeclaredFields()).map(f->f.getName()).toArray(String[]::new);
        }

        Arrays.stream(argNamesToConfig).forEach(name -> {
            String value = (String)staticConfigures.get(name.toLowerCase());
            if (value != null) {
                try {
                    Field field = clazz.getDeclaredField(name);
                    field.setAccessible(true);
                    String fieldTypeName = field.getType().getSimpleName();
                    if (fieldTypeName.endsWith("[]")) {
                        // array
                        String fieldType = fieldTypeName.substring(0, fieldTypeName.indexOf('['));
                        String[] values = parseArrayString(value);
                        if (fieldType.equalsIgnoreCase("int")) {
                            int[] intArray = Arrays.stream(values).mapToInt(Integer::parseInt).toArray();
                            field.set(clazz, intArray);
                        } else if (fieldType.equalsIgnoreCase("double")) {
                            double[] doubleArray = Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
                            field.set(clazz, doubleArray);
                        } else if (fieldType.equalsIgnoreCase("string")) {
                            field.set(clazz, values);
                        }
                    } else if (fieldTypeName.equalsIgnoreCase("int")) {
                        field.setInt(clazz, Integer.parseInt(value));
                    } else if (fieldTypeName.equalsIgnoreCase("double")) {
                        field.setDouble(clazz, Double.parseDouble(value));
                    } else if (fieldTypeName.equalsIgnoreCase("float")) {
                        field.setFloat(clazz, Float.parseFloat(value));
                    } else if (fieldTypeName.equalsIgnoreCase("boolean")) {
                        field.setBoolean(clazz, Boolean.parseBoolean(value));
                    } else if (fieldTypeName.equalsIgnoreCase("string")) {
                        field.set(clazz, value);
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static String[] parseArrayString(String str) {
        Pattern pattern = Pattern.compile("\\{(.*)\\}");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            str = matcher.group(1);
        }
        return str.split(",");
    }

    private static void tryLoadFromLocalFile(Properties staticConfigures) {
        String initConfigFilePath = staticConfigures.getProperty("initConfigFilePath");
        if (initConfigFilePath == null) {
            initConfigFilePath = "./" + INIT_CONFIG_FILE_NAME;
        }

        File file = new File(initConfigFilePath);
        if (file.exists() && file.isFile()) {
            Properties propertiesFromFile = new Properties();
            try {
                propertiesFromFile.load(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            propertiesFromFile.forEach((key, value) -> staticConfigures.setProperty(key.toString().toLowerCase(), value.toString()));
            staticConfigures.remove("initConfigFilePath");
        }
    }

    private static void parseArgsString(String argString, Properties properties) {
        String[] propertyPair = argString.split("=");
        if (propertyPair.length == 2) {
            properties.put(propertyPair[0], propertyPair[1]);
        }
    }


}

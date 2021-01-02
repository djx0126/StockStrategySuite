package djx.stockdataanalyzer.tools;

import com.djx.stockgainanalyzer.StrategyHelper;
import com.stockstrategy.constant.ArgParser;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.CollectMacdATRDataStrategy;
import com.stockstrategy.tools.Utils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class MacdATRDataTester {

    private static List<CollectMacdATRDataStrategy.MacdData> dataList = new ArrayList<>();

    private static List<PartMacdData> partMacdData = new ArrayList<>();


    public static synchronized void addData(CollectMacdATRDataStrategy.MacdData data) {
        dataList.add(data);
    }

    private static synchronized void addPartData(PartMacdData data) {
        partMacdData.add(data);
    }

    public static void main(String[] args) {
        System.out.println("Start");
        long startTime = System.currentTimeMillis();
        ArgParser.loadInitConfigures(args, Constant.class);

        StrategyHelper.generateData(CollectMacdATRDataStrategy.NAME);

        System.out.printf("total %d loaded\n", dataList.size());
        if (dataList.isEmpty()) {
            return;
        }

        Map<String, List<CollectMacdATRDataStrategy.MacdData>> dataByDate = dataList.stream().collect(Collectors.groupingBy(CollectMacdATRDataStrategy.MacdData::getDate));
        System.out.println("size by date:" + dataByDate.size());

        double totalAvgGainByDay = dataByDate.keySet().stream().parallel().mapToDouble(k -> dataByDate.get(k).stream().parallel().mapToDouble(CollectMacdATRDataStrategy.MacdData::getGain).average().getAsDouble()).average().getAsDouble();
        double gainAll = accumulateGain(dataByDate);
        System.out.println("totalAvgGainByDay: " + Utils.format3(totalAvgGainByDay) + ", total gain: " + Utils.format3(gainAll));

        double dif_max = dataList.stream().parallel().mapToDouble(CollectMacdATRDataStrategy.MacdData::getDif).max().getAsDouble();
        double dif_min = dataList.stream().parallel().mapToDouble(CollectMacdATRDataStrategy.MacdData::getDif).min().getAsDouble();
        double maxDif_max = dataList.stream().parallel().mapToDouble(CollectMacdATRDataStrategy.MacdData::getMaxDif).max().getAsDouble();
        double maxDif_min = dataList.stream().parallel().mapToDouble(CollectMacdATRDataStrategy.MacdData::getMaxDif).min().getAsDouble();
        double dif2_max = dataList.stream().parallel().mapToDouble(CollectMacdATRDataStrategy.MacdData::getLastGoldenCrossDif).max().getAsDouble();
        double dif2_min = dataList.stream().parallel().mapToDouble(CollectMacdATRDataStrategy.MacdData::getLastGoldenCrossDif).min().getAsDouble();
        double sizeRate_max = dataList.stream().parallel().mapToDouble(CollectMacdATRDataStrategy.MacdData::getSizeRate).max().getAsDouble();
        double sizeRate_min = dataList.stream().parallel().mapToDouble(CollectMacdATRDataStrategy.MacdData::getSizeRate).min().getAsDouble();

        System.out.println("dif_max: " + Utils.format3(dif_max) + " dif_min: " + Utils.format3(dif_min) + " maxDif_max: " + Utils.format3(maxDif_max) +
                " maxDif_min: " + Utils.format3(maxDif_min) + " dif2_max: " + Utils.format3(dif2_max) + " dif2_min: " + Utils.format3(dif2_min)
                + " sizeRate_min: "+ Utils.format3(sizeRate_min) + " sizeRate_max: " + Utils.format3(sizeRate_max));

        int stepNum = 4;
        Map<Integer, Double> difSteps = split(stepNum, dif_min, dif_max);
        Map<Integer, Double> maxDifSteps = split(stepNum, maxDif_min, maxDif_max);
        Map<Integer, Double> dif2Steps = split(stepNum, dif2_min, dif2_max);
        Map<Integer, Double> sizeRateSteps = split(stepNum, sizeRate_min, sizeRate_max);

        System.out.println("difSteps:" + difSteps + ", maxDifSteps:" + maxDifSteps + ", dif2Steps" + dif2Steps + ", sizeRateSteps:" + sizeRateSteps);


        scan(dif_min, dif_max, difSteps, (lDif, rDif) -> {
            List<CollectMacdATRDataStrategy.MacdData> byDif = dataList.stream().parallel().filter(d -> d.getDif() >= lDif && d.getDif() < rDif).collect(Collectors.toList());
//            scan(maxDif_min, maxDif_max, maxDifSteps, (lMaxDif, rMaxDif) -> {
//                List<CollectMacdATRDataStrategy.MacdData> byMaxDif = byDif.stream().parallel().filter(d -> d.getMaxDif() >= lMaxDif && d.getMaxDif() < rMaxDif).collect(Collectors.toList());
                scan(dif2_min, dif2_max, dif2Steps, (lDif2, rDif2) -> {
                    List<CollectMacdATRDataStrategy.MacdData> byDif2 = byDif.stream().parallel().filter(d -> d.getLastGoldenCrossDif() >= lDif2 && d.getLastGoldenCrossDif() < rDif2).collect(Collectors.toList());
//                    scan(sizeRate_min, sizeRate_max, sizeRateSteps, (lSizeRate, rSizeRate) -> {
//                        List<CollectMacdATRDataStrategy.MacdData> bySizeRate = byDif2.stream().parallel().filter(d -> d.getSizeRate() >= sizeRate_min && d.getSizeRate() < sizeRate_max).collect(Collectors.toList());
                        if (!byDif2.isEmpty()) {
                            Map<String, List<CollectMacdATRDataStrategy.MacdData>> byDate = byDif2.stream().parallel().collect(Collectors.groupingBy(CollectMacdATRDataStrategy.MacdData::getDate));

                            if (byDate.size() > 20) {
                                PartMacdData partMacdData = new PartMacdData();
                                partMacdData.lDif = lDif;
                                partMacdData.rDif = rDif;
//                                partMacdData.lMaxDif = lMaxDif;
//                                partMacdData.rMaxDif = rMaxDif;
                                partMacdData.lDif2 = lDif2;
                                partMacdData.rDif2 = rDif2;
//                                partMacdData.lSizeRate = lSizeRate;
//                                partMacdData.rSizeRate = rSizeRate;

                                partMacdData.addDataByDay(byDate);
                                addPartData(partMacdData);
                            }
                        }
//                    } );


//                });
            });
        });

        System.out.printf("total %d partMacdData loaded\n", partMacdData.size());

        List<PartMacdData> topGainPartData = partMacdData.stream().sorted((o1, o2) -> {
            if (o1.avgGain > o2.avgGain) {
                return -1;
            } else if (o1.avgGain < o2.avgGain) {
                return 1;
            }
            return 0;
        }).limit(100).collect(Collectors.toList());

        topGainPartData.forEach(System.out::println);


        long endTime = System.currentTimeMillis();
        System.out.println("time passed:" + (endTime - startTime) / 1000 + " seconds.");
        System.out.println("End at " + new Date());
    }

    private static double accumulateGain(Map<String, List<CollectMacdATRDataStrategy.MacdData>> data) {
        List<Double> gainsByDay = data.keySet().stream().parallel().map(k -> data.get(k).stream().parallel().mapToDouble(CollectMacdATRDataStrategy.MacdData::getGain).average().getAsDouble()).collect(Collectors.toList());
        double gain = 1.0;
        for (Double g: gainsByDay) {
            gain = gain *(100.0 + g)/100.0;
        }
        return gain;
    }

    private static void scan(double min, double max, Map<Integer, Double> steps, BiConsumer<Double, Double> consumer) {
        int stepCount = steps.size() + 1;
        for (int i = 0; i < stepCount; i++) {
            double l = min - 1e-5;
            double r = max + 1e-5;
            if (i > 0) {
                l = steps.get(i);
            }
            for (int j = i + 1; j <= stepCount; j++) {
                if (j < stepCount) {
                    r = steps.get(j);
                }
                if (j == stepCount) {
                    r = max + 1e-5;
                }

                consumer.accept(l, r);

            }
        }
    }

    private static Map<Integer, Double> split(int stepCount, double min, double max) {
        Map<Integer, Double> steps = new HashMap<>();
        double delta = (max - min)/stepCount;
        for (int i = 1; i <stepCount; i++) {
            steps.put(i, min + i * delta);
        }
        return steps;
    }

    private static class PartMacdData {
        private double lDif;
        private double rDif;
        private double lMaxDif;
        private double rMaxDif;
        private double lDif2;
        private double rDif2;
        private double lSizeRate;
        private double rSizeRate;
        private double avgGain;
        private double count;

        private void addDataByDay(Map<String, List<CollectMacdATRDataStrategy.MacdData>> byDate) {
            avgGain = accumulateGain(byDate);
            count = byDate.size();
        }

        private void addData(List<CollectMacdATRDataStrategy.MacdData> data) {
            avgGain = data.stream().parallel().mapToDouble(CollectMacdATRDataStrategy.MacdData::getGain).average().getAsDouble();
            count = data.size();
        }

        @Override
        public String toString() {
            return "PartMacdData{" +
                    "avgGain=" + avgGain +
                    ", count=" + count +
                    ", lDif=" + lDif +
                    ", rDif=" + rDif +
                    ", lMaxDif=" + lMaxDif +
                    ", rMaxDif=" + rMaxDif +
                    ", lDif2=" + lDif2 +
                    ", rDif2=" + rDif2 +
                    ", lSizeRate=" + lSizeRate +
                    ", rSizeRate=" + rSizeRate +
                    '}';
        }

    }
}

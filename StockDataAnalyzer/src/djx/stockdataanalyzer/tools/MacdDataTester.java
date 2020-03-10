package djx.stockdataanalyzer.tools;

import com.djx.stockgainanalyzer.StrategyHelper;
import com.stockstrategy.constant.ArgParser;
import com.stockstrategy.constant.Constant;
import com.stockstrategy.statistic.data.CollectMacdDataStrategy;
import com.stockstrategy.tools.Utils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class MacdDataTester {

    private static List<CollectMacdDataStrategy.MacdData> dataList = new ArrayList<>();

    private static List<PartMacdData> partMacdData = new ArrayList<>();


    public static synchronized void addData(CollectMacdDataStrategy.MacdData data) {
        dataList.add(data);
    }

    public static synchronized void addPartData(PartMacdData data) {
        partMacdData.add(data);
    }

    public static void main(String[] args) {
        System.out.println("Start");
        long startTime = System.currentTimeMillis();
        ArgParser.loadInitConfigures(args, Constant.class);

        StrategyHelper.generateData(CollectMacdDataStrategy.NAME);

        System.out.printf("total %d loaded\n", dataList.size());
        if (dataList.isEmpty()) {
            return;
        }

        Map<String, List<CollectMacdDataStrategy.MacdData>> dataByDate = dataList.stream().collect(Collectors.groupingBy(CollectMacdDataStrategy.MacdData::getDate));
        System.out.println("size by date:" + dataByDate.size());

        double totalAvgGainByDay = dataByDate.keySet().stream().mapToDouble(k -> dataByDate.get(k).stream().mapToDouble(CollectMacdDataStrategy.MacdData::getGain).average().getAsDouble()).average().getAsDouble();
        List<Double> totalGainsByDay = dataByDate.keySet().stream().map(k -> dataByDate.get(k).stream().mapToDouble(CollectMacdDataStrategy.MacdData::getGain).average().getAsDouble()).collect(Collectors.toList());
        double gainAll = 1.0;
        for (Double g: totalGainsByDay) {
            gainAll = gainAll *(100.0 + g)/100.0;
        }
        System.out.println("totalAvgGainByDay: " + Utils.format3(totalAvgGainByDay) + ", total gain: " + Utils.format3(gainAll));

        double dif_max = dataList.stream().parallel().mapToDouble(CollectMacdDataStrategy.MacdData::getDif).max().getAsDouble();
        double dif_min = dataList.stream().parallel().mapToDouble(CollectMacdDataStrategy.MacdData::getDif).min().getAsDouble();
        double maxDif_max = dataList.stream().parallel().mapToDouble(CollectMacdDataStrategy.MacdData::getMaxDif).max().getAsDouble();
        double maxDif_min = dataList.stream().parallel().mapToDouble(CollectMacdDataStrategy.MacdData::getMaxDif).min().getAsDouble();
        double dif2_max = dataList.stream().parallel().mapToDouble(CollectMacdDataStrategy.MacdData::getLastGoldenCrossDif).max().getAsDouble();
        double dif2_min = dataList.stream().parallel().mapToDouble(CollectMacdDataStrategy.MacdData::getLastGoldenCrossDif).min().getAsDouble();

        System.out.println("dif_max: " + Utils.format3(dif_max) + " dif_min: " + Utils.format3(dif_min) + " maxDif_max: " + Utils.format3(maxDif_max) +
                " maxDif_min: " + Utils.format3(maxDif_min) + " dif2_max: " + Utils.format3(dif2_max) + " dif2_min: " + Utils.format3(dif2_min));

        int stepNum = 10;
        Map<Integer, Double> difSteps = split(stepNum, dif_min, dif_max);
        Map<Integer, Double> maxDifSteps = split(stepNum, maxDif_min, maxDif_max);
        Map<Integer, Double> dif2Steps = split(stepNum, dif2_min, dif2_max);

        System.out.println("difSteps:" + difSteps + ", maxDifSteps:" + maxDifSteps + ", dif2Steps" + dif2Steps);


        scan(dif_min, dif_max, difSteps, (lDif, rDif) -> {
            List<CollectMacdDataStrategy.MacdData> byDif = dataList.stream().parallel().filter(d -> d.getDif() >= lDif && d.getDif() < rDif).collect(Collectors.toList());
            scan(maxDif_min, maxDif_max, maxDifSteps, (lMaxDif, rMaxDif) -> {
                List<CollectMacdDataStrategy.MacdData> byMaxDif = byDif.stream().parallel().filter(d -> d.getMaxDif() >= lMaxDif && d.getMaxDif() < rMaxDif).collect(Collectors.toList());
                scan(dif2_min, dif2_max, dif2Steps, (lDif2, rDif2) -> {
                    List<CollectMacdDataStrategy.MacdData> byDif2 = byMaxDif.stream().parallel().filter(d -> d.getLastGoldenCrossDif() >= lDif2 && d.getLastGoldenCrossDif() < rDif2).collect(Collectors.toList());
                    if (!byDif2.isEmpty()) {
//                        System.out.println("dif=[" + lDif + "," + rDif + "] maxDif=[" + lMaxDif + "," + rMaxDif + "] dif2=[" + lDif2 + "," + rDif2 + "]");
                        Map<String, List<CollectMacdDataStrategy.MacdData>> byDate = byDif2.stream().parallel().collect(Collectors.groupingBy(CollectMacdDataStrategy.MacdData::getDate));

                        if (byDate.size() > 20) {
//                            double avgGainByDay = byDate.keySet().stream().mapToDouble(k -> dataByDate.get(k).stream().mapToDouble(CollectMacdDataStrategy.MacdData::getGain).average().getAsDouble()).average().getAsDouble();
                            List<Double> gainsByDay = byDate.keySet().stream().map(k -> dataByDate.get(k).stream().mapToDouble(CollectMacdDataStrategy.MacdData::getGain).average().getAsDouble()).collect(Collectors.toList());
                            double gain = 1.0;
                            for (Double g: gainsByDay) {
                                gain = gain *(100.0 + g)/100.0;
                            }
//                        System.out.println("avgGainByDay: " + Utils.format3(avgGainByDay));

                            PartMacdData partMacdData = new PartMacdData();
                            partMacdData.lDif = lDif;
                            partMacdData.rDif = rDif;
                            partMacdData.lMaxDif = lMaxDif;
                            partMacdData.rMaxDif = rMaxDif;
                            partMacdData.lDif2 = lDif2;
                            partMacdData.rDif2 = rDif2;
                            partMacdData.avgGainByDay = gain;
                            partMacdData.count = byDate.size();
                            addPartData(partMacdData);
                        }
                    }
                });
            });
        });

        System.out.printf("total %d partMacdData loaded\n", partMacdData.size());

        List<PartMacdData> topGainPartData = partMacdData.stream().sorted((o1, o2) -> {
            if (o1.avgGainByDay > o2.avgGainByDay) {
                return -1;
            } else if (o1.avgGainByDay < o2.avgGainByDay) {
                return 1;
            }
            return 0;
        }).limit(100).collect(Collectors.toList());

        topGainPartData.stream().forEach(System.out::println);


        long endTime = System.currentTimeMillis();
        System.out.println("time passed:" + (endTime - startTime) / 1000 + " seconds.");
        System.out.println("End at " + new Date());
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
        private double avgGainByDay;
        private double count;

        @Override
        public String toString() {
            return "PartMacdData{" +
                    "avgGainByDay=" + avgGainByDay +
                    ", count=" + count +
                    ", lDif=" + lDif +
                    ", rDif=" + rDif +
                    ", lMaxDif=" + lMaxDif +
                    ", rMaxDif=" + rMaxDif +
                    ", lDif2=" + lDif2 +
                    ", rDif2=" + rDif2 +
                    '}';
        }

    }
}

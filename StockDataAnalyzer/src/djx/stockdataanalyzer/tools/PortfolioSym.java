package djx.stockdataanalyzer.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class PortfolioSym {

    public static void main(String[] args) throws IOException {
        String path = "C:\\dave\\workspace\\StockStrategySuite\\StockDataAnalyzer\\sample.txt";

        List<Tran> tranList = loadData(path);

        tranList.stream().forEach(System.out::println);

        double initCash =1000000.0;

        Account acc = new Account(initCash);

        int shareCount = 15;
        int mode = 0;
        double fixAmount = acc.cash/shareCount;

        double totalGain = 0.0;
        int tCount = 1;
        for (int i = 0; i<tranList.size(); i++) {
            Tran t = tranList.get(i);

            switch (mode) {
                case 0:
                    acc.buy(fixAmount, t);//end:1079244.5, gain:107.92445
                    break;
                case -1:
                    acc.buy(fixAmount * (totalGain/tCount + 100) / 100, t); //end:1064061.1063760635, gain:106.40611063760635
                    break;
                case 1:
                    acc.buy(acc.value()/shareCount, t); //end:1079435.488273037, gain:107.94354882730369
                    break;
                case -2:
//                    acc.buy(acc.cash/shareCount, t); //end:1064061.1063760635, gain:106.40611063760635
                    break;
            }

            List<Tran> toSell = acc.checkSell(t.buyDate);
            totalGain += toSell.stream().mapToDouble(st -> st.gain).sum();
            tCount += toSell.size();
            if (!toSell.isEmpty()) {
                if (toSell.get(0).gain < 0) {
                    mode = -1;
                } else if ( toSell.get(0).gain > 0 ) {
                    mode = 1;
                }
            }

            mode = 1;
        }
        acc.clear();

        System.out.println("end:" + acc.cash + ", gain:" + (acc.cash / initCash) * 100  ) ;
    }

    private static List<Tran> loadData(String path) throws IOException {
        List<String> lines = Files.lines(Paths.get(path)).collect(Collectors.toList());

        List<Tran> tranList = new ArrayList<>();

        for (int i= 0; i<lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("Buy")) {
                String[] items = line.split(" ");
                String buyDate = items[1].replace("date:", "");
                int count = Integer.parseInt(items[3]);
                double gain = Double.parseDouble(items[8]);

                String lastSellDate = null;
                for (int j = 0; j<count; j++) {
                    line = lines.get(++i);
                    String sellDate = line.split(" ")[5].replace("date:", "");
                    if (lastSellDate == null || sellDate.compareTo(lastSellDate) > 0) {
                        lastSellDate = sellDate;
                    }
                }

                Tran t = new Tran();
                t.buyDate = buyDate;
                t.sellDate = lastSellDate;
                t.gain = gain;
                t.count = count;

                tranList.add(t);
            }
        }

        tranList = tranList.stream().sorted(Comparator.comparing(o -> o.buyDate)).collect(Collectors.toList());

        return  tranList;
    }

    private static class Tran {
        String buyDate;
        String sellDate;
        double gain;
        int count;
        double amount;

        @Override
        public String toString() {
            return "Tran{" +
                    "buyDate='" + buyDate + '\'' +
                    ", sellDate='" + sellDate + '\'' +
                    ", gain=" + gain +
                    ", count=" + count +
                    '}';
        }
    }

    private static class Account {
        double cash;
        List<Tran> inhandTrans = new ArrayList<>();

        private Account(double initCash) {
            this.cash = initCash;
        }

        private boolean buy(double amount, Tran t) {
            if (cash > amount) {
                cash -= amount;
                inhandTrans.add(t);
                t.amount = amount;
                System.out.println("inhandCount=" + inhandTrans.size() + ", t:" + t);
                return true;
            }
            System.out.println("buy out!" + t + ", value= " + this);
            return false;
        }

        private List<Tran> checkSell(String date) {
            List<Tran> toSell = inhandTrans.stream().filter(t -> t.sellDate.compareTo(date) < 0).collect(Collectors.toList());
            toSell.forEach(t -> {
                cash += t.amount * (t.gain + 100.0) / 100.0;
                inhandTrans.remove(t);
            });
            return toSell;
        }

        private void clear() {
            this.checkSell("99999999");
        }

        private double value() {
            return cash + inhandTrans.stream().mapToDouble(t -> t.amount).sum();
        }

        @Override
        public String toString() {
            return "{" + value() + ", inhand count = " + inhandTrans.size() + "}";
        }

    }
}

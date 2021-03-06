package example;

import lombok.Value;
import pl.edu.pw.elka.pszt.SpamFilter;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class KCross extends DataLoader {
    private final static String DATA_PATH = "example/data/";

    public static void main(String[] args) {
        System.out.println(IntStream.range(0, 2).mapToDouble(e -> {
            try {
                return new KCross().run(5);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return 0;
        }).average().orElse(0));
    }

    public double run(final int k) throws IOException {
        File file = new File(DATA_PATH);
        //load data
        List<String> ham = loadData(file, "/ham");
        List<String> spam = loadData(file, "/spam");

        //shuffle data
        Collections.shuffle(spam);
        Collections.shuffle(ham);

        //divide into subsets
        List<Subset> subsets = divide(k, spam, ham);

        return calculate(subsets);
    }

    private List<Subset> divide(final int k, List<String> spam, List<String> ham) {
        List<Subset> list = new LinkedList<>();
        for (int i = 0; i < k; ++i) {
            list.add(new Subset(ham.subList(i * ham.size() / k, (i + 1) * ham.size() / k),
                    spam.subList(i * spam.size() / k, (i + 1) * spam.size() / k)));
        }
        return list;
    }

    private double calculate(final List<Subset> list) {
        return list.stream().mapToDouble(i -> {
            final SpamFilter spamFilter = new SpamFilter();
            //learn
            list.stream().filter(j -> i != j).forEach(j -> {
                j.ham.forEach(mail -> spamFilter.learn(mail, false));
                j.spam.forEach(mail -> spamFilter.learn(mail, true));
            });
            spamFilter.update();
            final int counter = i.spam.stream().mapToInt(spam -> spamFilter.isSpam(spam) ? 0 : 1).sum() +
                    i.ham.stream().mapToInt(ham -> spamFilter.isSpam(ham) ? 1 : 0).sum();

            spamFilter.clear();

            return counter / (double) (i.ham.size() + i.spam.size());
        }).average().orElse(Double.POSITIVE_INFINITY);
    }

    @Value
    private static class Subset {
        private List<String> ham;
        private List<String> spam;
    }
}
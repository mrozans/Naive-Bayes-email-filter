import lombok.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Example {
    private final static String DATA_PATH = "example/data/";

    public static void main(String[] args) throws IOException {
        Example example = new Example();
        final DTO dto = example.example(0.5);
        System.out.println(dto);
    }

    public DTO example(final double value) throws IOException {
        SpamFilter spamFilter = new SpamFilter();

        File file = new File(DATA_PATH);
        String[] folders = file.list();

        List<String> hams = new LinkedList<>();
        List<String> spams = new LinkedList<>();

        //load data
        for (int i = 0; i < Objects.requireNonNull(file.list()).length; ++i) {

            assert folders != null;
            spams.addAll(getFileValues(DATA_PATH + "/" + folders[i] + "/spam"));
            hams.addAll(getFileValues(DATA_PATH + "/" + folders[i] + "/ham"));
        }
        //shuffle data
        Collections.shuffle(hams);
        Collections.shuffle(spams);
        //learn
        for (int i = 0; i < hams.size() * value; ++i) {
            spamFilter.learn(hams.get(i), false);
        }
        for (int i = 0; i < spams.size() * value; ++i) {
            spamFilter.learn(spams.get(i), true);
        }
        //calculate
        spamFilter.update();

        //test
        int hamsSuccess = 0, hamsFails = 0;
        int spamsSuccess = 0, spamsFails = 0;
        final long before = System.currentTimeMillis();
        for (int i = (int) (hams.size() * value); i < hams.size(); ++i) {
            if (!spamFilter.isHam(hams.get(i)))
                hamsFails++;
            else
                ++hamsSuccess;
        }
        int c = 0;
        for (int i = (int) (spams.size() * value); i < spams.size(); ++i, ++c) {
            if (!spamFilter.isSpam(spams.get(i)))
                spamsFails++;
            else
                ++spamsSuccess;
        }
        final DTO dto = new DTO(hamsSuccess,
                hamsFails,
                spamsSuccess,
                spamsFails,
                (System.currentTimeMillis() - before) / (hamsSuccess + hamsFails + spamsSuccess + spamsFails));
        return dto;
    }

    private ArrayList<String> getFileValues(final String path) throws IOException {
        File folder = new File(path);
        ArrayList<String> arrayList = new ArrayList<>();
        for (String file : Objects.requireNonNull(folder.list())) {
            arrayList.add(new String(Files.readAllBytes(Paths.get(path + "/" + file))));
        }
        return arrayList;
    }
}

@Value
class DTO {
    int hamsSuccess, hamsFails;
    int spamsSuccess, spamsFails;
    long averageTime;

    @Override
    public String toString() {
        return String.format("hamsSuccess: %d, hamTests: %d, success percentage: %d\n" +
                        "spamsSuccess: %d, spamTests: %d, success percentage: %d\n" +
                        "averageTime[ms]: %d\n",
                hamsSuccess,
                hamsSuccess + hamsFails,
                (int) ((double) hamsSuccess * 100 / (double) (hamsSuccess + hamsFails)),
                spamsSuccess,
                spamsSuccess + spamsFails,
                (int) ((double) spamsSuccess * 100 / (double) (spamsSuccess + spamsFails)),
                averageTime
        );
    }
}

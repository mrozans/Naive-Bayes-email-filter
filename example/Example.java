import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class Example {
    private final static int LEARN_FOLDERS = 5;
    private final static String DATA_PATH = "example/data/";

    public static void main(String[] args) throws IOException {
        SpamFilter spamFilter = new SpamFilter();

        File file = new File(DATA_PATH);
        String[] folders = file.list();

        for (int i = 0; i < LEARN_FOLDERS && i < Objects.requireNonNull(file.list()).length; ++i) {
            assert folders != null;

            String[] spams = getFileValues(DATA_PATH + "/" + folders[i] + "/spam");
            String[] hams = getFileValues(DATA_PATH + "/" + folders[i] + "/ham");

            for (String spam : spams) {
                spamFilter.learn(spam, true);
            }
            for (String ham : hams) {
                spamFilter.learn(ham, false);
            }

        }
        int spamsFails = 0;
        int hamsFails = 0;
        int spamCounter = 0,hamCounter = 0;

        for (int i = LEARN_FOLDERS; i < Objects.requireNonNull(file.list()).length; ++i) {
            assert folders != null;

            String[] spams = getFileValues(DATA_PATH + "/" + folders[i] + "/spam");
            for (String spam : spams) {
                if(!spamFilter.isSpam(spam))
                    spamsFails++;
            }
            spamCounter += spams.length;
            String[] hams = getFileValues(DATA_PATH + "/" + folders[i] + "/ham");
            for (String ham : hams) {
                if(!spamFilter.isHam(ham))
                    hamsFails++;
            }
            hamCounter += hams.length;
        }
        System.out.println("spams fails: " + spamsFails + "in: " + spamCounter + "; % = " + 100 * spamsFails/ spamCounter);
        System.out.println("hams fails: " + hamsFails + "in: " + hamCounter + "; % = " + 100 * hamsFails/ hamCounter);
    }

    private static String[] getFileValues(final String path) throws IOException {
        File folder = new File(path);
        ArrayList<String> arrayList = new ArrayList<>();
        for (String file : Objects.requireNonNull(folder.list())) {
            arrayList.add(getFileValue(path + "/" + file));
        }
        return arrayList.toArray(new String[0]);
    }

    private static String getFileValue(final String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

}

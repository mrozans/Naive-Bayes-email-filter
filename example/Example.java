import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Example {
    private final static double value = 0.5;
    private final static String DATA_PATH = "example/data/";

    public static void main(String[] args) throws IOException {
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
        for(int i=0; i<hams.size() * value; ++i){
            spamFilter.learn(hams.get(i), false);
        }
        for(int i=0; i<spams.size() * value; ++i){
            spamFilter.learn(spams.get(i), true);
        }
        //calculate
        spamFilter.update();

        //test
        int spamsFails = 0, hamsFails = 0;
        for(int i = (int) (hams.size() * value); i<hams.size(); ++i){
            if(!spamFilter.isHam(hams.get(i)))
                hamsFails++;
        }
        for(int i = (int) (spams.size() * value); i<spams.size(); ++i){
            if(!spamFilter.isSpam(spams.get(i)))
                spamsFails++;
        }
        System.out.println("spams fails: " + spamsFails + " in: " + spams.size() + "; % = " + 100 * spamsFails/ spams.size());
        System.out.println("hams fails: " + hamsFails + " in: " + hams.size() + "; % = " + 100 * hamsFails/ hams.size());
    }

    private static ArrayList<String> getFileValues(final String path) throws IOException {
        File folder = new File(path);
        ArrayList<String> arrayList = new ArrayList<>();
        for (String file : Objects.requireNonNull(folder.list())) {
            arrayList.add(new String(Files.readAllBytes(Paths.get(path + "/" + file))));
        }
        return arrayList;
    }

}

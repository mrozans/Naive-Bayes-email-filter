import model.Word;

import java.util.Set;
import java.util.TreeSet;

public class SpamFilter {
    Set<Word> set = new TreeSet<>();
    void learn(String string, boolean isSpam){
        //ToDo
    }

    boolean isSpam(String string){
        return true;
        //ToDo
    }

    boolean isHam(String string){
        return !isSpam(string);
    }
}

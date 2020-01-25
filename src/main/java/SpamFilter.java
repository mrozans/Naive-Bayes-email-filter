import model.Word;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class SpamFilter {
    private Map<String, Word> map = new TreeMap<>();
    public void learn(String sentence, boolean isSpam){
        for (String wordString : sentence.split(" ")) {
            Word wordClass = new Word(wordString);

            if(!Objects.isNull(map.putIfAbsent(wordString, wordClass))){
                wordClass = map.get(wordString);
            }
            if(isSpam){
                wordClass.incrementSpam();
            } else {
                wordClass.incrementHam();
            }
        }
    }

    public boolean isSpam(String string){
        return true;
        //ToDo
    }

    public boolean isHam(String string){
        return !isSpam(string);
    }
}

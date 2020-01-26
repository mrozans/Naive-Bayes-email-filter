import algorithm.NaiveBayes;
import model.Word;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class SpamFilter {
    public String REGEX = " |\r\n|\r|\n";
    private Map<String, Word> map;
    private int hams = 0, spams = 0;
    private NaiveBayes naiveBayes;

    SpamFilter(){
        this.map = new TreeMap<>();
        this.naiveBayes = new NaiveBayes(map);
    }

    public void learn(String sentence, boolean isSpam){
        for (String wordString : filter(sentence.split(REGEX))) {
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
        if(isSpam){
            ++spams;
        } else {
            ++hams;
        }
    }

    public void update(){
        naiveBayes.setTotalHamEmails(hams);
        naiveBayes.setTotalSpamEmails(spams);
        map.forEach((key, value) -> {
            value.setProbabilityOfHam(naiveBayes.probabilityOfHamWord(hams, value));
            value.setProbabilityOfSpam(naiveBayes.probabilityOfSpamWord(spams, value));
        });
    }

    public boolean isSpam(String string){
        return naiveBayes.isSpamEmail(filter(string.split(REGEX)));
    }

    public boolean isHam(String string){
        return !isSpam(string);
    }

    private String[] filter(String[] strings) {
        return Arrays.stream(strings)
                .dropWhile(e -> e.length() == 1 && !Character.isLetter(e.charAt(0)))
                .dropWhile(e -> {
                    if (e.length() == 1)
                        return false;
                    long count = e.chars().filter(c -> !Character.isLetter(c)).count();
                    if (count > 2)
                        return true;
                    if (count == 0)
                        return false;
                    return count == 1 && Character.isLetter(e.charAt(e.length() - 1));
                })
                .map(String::toLowerCase)
                .map(e -> {
                    if (e.length() > 0 && !Character.isLetter(e.charAt(e.length() - 1)))
                        return e.substring(0, e.length() - 1);
                    return e;
                })
                .filter(e-> !NumberUtils.isNumber(e))
                .filter(e->!e.equals(""))
                .toArray(String[]::new);
    }
}

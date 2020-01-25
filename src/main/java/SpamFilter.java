import algorithm.NaiveBayes;
import model.Word;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.stream.Collectors;

public class SpamFilter {
    private Map<String, Word> map = new TreeMap<>();
    private int hams = 0, spams = 0;
    private NaiveBayes naiveBayes = new NaiveBayes();
    final private String REGEX = " |\r\n|\r|\n";
    public void learn(String sentence, boolean isSpam){
        //System.out.println(String.join(" ",filter(sentence.split(REGEX))));
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

    public boolean isSpam(String string){
        return naiveBayes.isSpamEmail(spams, hams, filter(string.split(REGEX)), map);
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
                    if (count == 1 && Character.isLetter(e.charAt(e.length() - 1))) {
                        return true;
                    }
                    return false;
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

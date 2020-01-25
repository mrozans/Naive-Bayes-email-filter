package algorithm;

import model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NaiveBayes
{
    public boolean isSpamEmail(int spam, int ham, String[] words, Map<String, Word> map)
    {
        List arrayList = new ArrayList<Word>();
        for (String word : words) {
            arrayList.add(map.get(word));
        }
        //
        double value = Math.log10((double)spam/(double)ham);
        for (String word : words) {
            if(map.get(word) == null) continue;
            double v = Math.log10(probabilityOfSpamWord(word, spam, map) / probabilityOfHamWord(word, ham, map));
            value += v;
        }
        return value > 0;
    }

    private double probabilityOfSpamWord(String word, int spam, Map<String, Word> map)
    {
        if(map.get(word) == null) return 1;
        return (double)map.get(word).getSpam() / (double)spam;
    }

    private double probabilityOfHamWord(String word, int ham, Map<String, Word> map)
    {
        if(map.get(word) == null) return 1;
        return (double)map.get(word).getHam() / (double)ham;
    }
}

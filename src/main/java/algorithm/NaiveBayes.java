package algorithm;

import model.Word;

import java.util.Map;

public class NaiveBayes
{
    public boolean isSpamEmail(int spam, int ham, String[] words, Map<String, Word> map)
    {
        double value = Math.log10(spam/ham);
        for (String word : words) {
            if(map.get(word).getSpam() > 0 && map.get(word).getHam() == 0) return true;
            value += Math.log10(probabilityOfSpamWord(word, spam, map) / probabilityOfHamWord(word, ham, map));
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

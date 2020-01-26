package algorithm;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import model.Word;

import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
public class NaiveBayes
{
    private final Map<String, Word> map;
    @Setter
    double totalSpamEmails = 0, totalHamEmails = 0;

    public boolean isSpamEmail(String[] words)
    {
        return calculateSpamProbability(totalSpamEmails / totalHamEmails, words) > 0;
    }

    public double probabilityOfSpamWord(int spam, Word word)
    {
        return probability(word.getSpam(), spam);
    }

    public double probabilityOfHamWord(int ham, Word word)
    {
        return probability(word.getHam(), ham);
    }

    private double calculateSpamProbability(double initValue, String[] words){
        return Arrays.stream(words).mapToDouble(e->{
            final Word wordClass = map.get(e);
            if(wordClass == null)
                return 0;
            return Math.log10(wordClass.getProbabilityOfSpam() / wordClass.getProbabilityOfHam());
        }).sum() + Math.log10(initValue);
    }

    private double probability(double nominator, double denominator){
        return denominator == 0 ? Double.POSITIVE_INFINITY : nominator / denominator;
    }
}

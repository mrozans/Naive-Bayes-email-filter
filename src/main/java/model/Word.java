package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Word implements Comparable<Word> {
    final private String name;
    private int ham = 0, spam = 0;

    public Word(final String name) {
        this.name = name;
    }

    public void increaseHam(final int newHam){
        this.ham += newHam;
    }

    public void increaseSpam(final int newSpam) {
        this.spam += newSpam;
    }
    public void incrementSpam(){
        ++this.spam;
    }
    public void incrementHam(){
        ++this.ham;
    }

    @Override
    public int compareTo(Word word) {
        return this.name.compareTo(word.getName());
    }
}

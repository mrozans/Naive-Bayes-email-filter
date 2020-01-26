package pl.edu.pw.elka.pszt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Word {
    final private String name;
    private int ham = 0, spam = 0;
    @Setter
    private double probabilityOfSpam = 0, probabilityOfHam = 0;

    public Word(final String name) {
        this.name = name;
    }
    public void incrementSpam(){
        ++this.spam;
    }
    public void incrementHam(){
        ++this.ham;
    }
}
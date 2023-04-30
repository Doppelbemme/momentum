package de.doppelbemme.momentum;

import de.doppelbemme.momentum.Word;

import java.util.List;

// Klasse für die gesamte JSON-Datei
public class Words {
    private List<Word> words;

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }
}

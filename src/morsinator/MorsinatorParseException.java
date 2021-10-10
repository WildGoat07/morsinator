package morsinator;

import morsinator.text.TextPosition;

public class MorsinatorParseException extends Exception {
    private TextPosition textPos;

    public MorsinatorParseException(String message, TextPosition textPos) {
        super(message);
        this.textPos = textPos;
    }

    public TextPosition getTextPos() {
        return textPos;
    }
}
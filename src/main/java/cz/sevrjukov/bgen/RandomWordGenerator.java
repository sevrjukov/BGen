/*
 * Copyright (c) 2017 BSC Praha, spol. s r.o.
 */

package cz.sevrjukov.bgen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import lombok.Builder;
import lombok.Data;

/**
 * Generator of random words.
 *
 * @author <a href="mailto:alexandr.sevrjukov@bsc-ideas.com">Alexandr Sevrjukov</a>
 */
public class RandomWordGenerator {

    private final Random random = new Random();

    // These are lookup tables for random character lookup of a given class.
    // They may contain multiple entries for each characters, more entries for more frequently
    // used characters.
    private List<Character> syllabicConsonants = new ArrayList<>();
    private List<Character> allConsonants = new ArrayList<>();
    private List<Character> vowels = new ArrayList<>();

    // settings
    private static final int MIN_SYLLABLES_COUNT = 2;
    private final Settings settings;


    /**
     * Static settings class with default values.
     */
    @Data
    @Builder
    public static class Settings {
        @Builder.Default
        private int maxSyllablesCount = 5;
        @Builder.Default
        private int consonantBasedSyllablesPercentage = 15;
        @Builder.Default
        private int threeCharSyllabelsPercentage = 69;
        @Builder.Default
        private int thirdCharVowelPercentage = 10;
        @Builder.Default
        private boolean allowDiacritics = false;
        @Builder.Default
        private boolean debugMode = false;
    }


    /**
     * Constructor allowing to init custom settings.
     *
     * @param settings generator settings
     */
    public RandomWordGenerator(final Settings settings) {
        this.settings = settings;
        init();
    }

    /**
     * Default constructor, uses default setting values.
     */
    public RandomWordGenerator() {
        this.settings = Settings.builder().build();
        init();
    }

    /**
     * Initialization routines
     */
    private void init() {
        populateCharSelectionArray(CharacterTables.SYLLABIC_CONSONANT_DENSITIES, syllabicConsonants);
        populateCharSelectionArray(CharacterTables.VOWEL_DENSITIES, vowels);
        populateCharSelectionArray(CharacterTables.ALL_CONSONANT_DENSITIES, allConsonants);
    }

    /**
     * Fills in random character selection arrays based on natural-language character usage frequency.
     */
    private void populateCharSelectionArray(CharacterDensity[] densities, final List<Character> tagetList) {
        Arrays.stream(densities).forEach(
                density -> {
                    if (!density.isDiacritics() || settings.isAllowDiacritics()) {
                        for (int i = 0; i < density.getDensity(); i++) {
                            tagetList.add(density.getCharacter());
                        }
                    }
                }
        );
    }

    /**
     * Generates a random word starting with the given character.
     */
    public String generateRandomWord(final Character startingWith) {

        int syllableCount = random.nextInt(MIN_SYLLABLES_COUNT) + settings.getMaxSyllablesCount() - MIN_SYLLABLES_COUNT;

        final StringBuilder resultWord = new StringBuilder();

        Syllable lastSyllable;
        if (startingWith != null) {
            lastSyllable = new Syllable(String.valueOf(startingWith));
        } else {
            lastSyllable = new Syllable("");
        }
        resultWord.append(lastSyllable.getValue());

        for (int i = 0; i < syllableCount; i++) {
            log("->");
            final Syllable syllable = constructSyllable(lastSyllable);
            lastSyllable = syllable;
            resultWord.append(syllable.getValue());
        }
        return resultWord.toString();
    }

    /**
     * Generates a random word.
     */
    public String generateRandomWord() {
        return generateRandomWord(null);
    }


    /**
     * <p>
     * Generates a random syllable.
     * <p>
     * Generation strategy:<br>
     * Each syllable may contain 1-3 characters. The first is a consonant, the second
     * a syllable-generating consonant or a vowel, and the third is a consonant or a vowel.
     * Depending on the previous syllable, the first character may be omitted. The second
     * character is always present. The third one may be omitted by chance or based on other rules.
     * <p>
     * Various other rules are applied to mimic human-produced syllabic groups as accurately as possible.
     *
     * @param previousSyllable previous syllable
     * @return newly generated syllable
     */
    private Syllable constructSyllable(Syllable previousSyllable) {

        assert previousSyllable != null;

        final StringBuilder value = new StringBuilder();
        boolean consonantBased = false;

        final Character firstChar;
        // randomly choose first consonant, unless the previous syllable already ends with a consonant
        if (!previousSyllable.endsWithConsonant() || previousSyllable.endsWithSyllabicConsonant()) {
            firstChar = getRandomChar(allConsonants, previousSyllable.getLastChar());
            value.append(firstChar);
            log("Selected first consonant");
        } else {
            // this is to avid duplication when choosing the seconds character
            log("First char omitted");
            firstChar = previousSyllable.getLastChar();
        }

        // append syllable core character - either a vowel or a syllabic consonant
        final Character secondChar;
        // disallow consecutive consonant-based syllables
        if (!previousSyllable.isConsonantBased() && biasedBoolean(settings.getConsonantBasedSyllablesPercentage())) {
            // disallow characters repetition
            secondChar = getRandomChar(syllabicConsonants, firstChar);
            consonantBased = true;
            log("Selected second syllabic consonant");
        } else {
            secondChar = getRandomChar(vowels);
            log("Selected second vowel");
        }
        value.append(secondChar);

        // only append the third in some cases
        if (biasedBoolean(settings.getThreeCharSyllabelsPercentage())) {
            final Character thirdChar;
            if (biasedBoolean(settings.getThirdCharVowelPercentage()) || consonantBased) {
                thirdChar = getRandomChar(vowels, secondChar);
                log("Selected third vowel");
            } else {
                thirdChar = getRandomChar(allConsonants, secondChar);
                log("Selected third consonant");
            }
            value.append(thirdChar);
        } else {
            log("Third char omitted");
        }
        return new Syllable(value.toString());
    }


    /**
     * Helper class representing a generated syllable.
     */
    private class Syllable {
        private final String value;

        public Syllable(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public Character getLastChar() {
            if (value.length() > 0) {
                return value.charAt(value.length() - 1);

            } else {
                return null;
            }
        }

        public boolean isConsonantBased() {
            return ((value.length() >= 2) && listContainsChar(syllabicConsonants, value.charAt(1)));
        }

        public boolean endsWithConsonant() {
            return listContainsChar(allConsonants, getLastChar());
        }

        public boolean endsWithSyllabicConsonant() {
            return listContainsChar(syllabicConsonants, getLastChar());
        }

        private boolean listContainsChar(List<Character> list, Character character) {
            return list.contains(character);
        }
    }


    /**
     * Randomly selects a  character from a list
     *
     * @param list list of chars
     * @return randomly selected character
     */
    private Character getRandomChar(List<Character> list) {
        return getRandomChar(list, null);
    }


    /**
     * Randomly selects a  character from a list
     *
     * @param list list of chars
     * @param excludedChar character which shall not be returned
     * @return randomly selected character
     */
    private Character getRandomChar(List<Character> list, Character excludedChar) {
        if (excludedChar == null) {
            return list.get(random.nextInt(list.size()));
        } else {
            Character result;
            do {
                result = list.get(random.nextInt(list.size()));
            } while (excludedChar.equals(result));
            return result;
        }
    }


    /**
     * Returned a biased boolean value based on the given percentage chance.
     *
     * @param percentage bias value
     * @return true or false
     */
    private boolean biasedBoolean(final int percentage) {
        return random.nextInt(100) <= percentage;
    }

    /**
     * Logs to standard output
     */
    private void log(final String string) {
        if (settings.isDebugMode()) {
            System.out.println(string);
        }
    }
}

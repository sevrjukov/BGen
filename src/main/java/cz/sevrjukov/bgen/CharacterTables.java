/*
 * Copyright (c) 2017 BSC Praha, spol. s r.o.
 */

package cz.sevrjukov.bgen;

/**
 * This static class holds a representation of the used alphabet, including
 * relative usage frequency for each character.
 *
 * @author <a href="mailto:alexandr.sevrjukov@bsc-ideas.com">Alexandr Sevrjukov</a>
 */
public final class CharacterTables {

    public static CharacterDensity[] SYLLABIC_CONSONANT_DENSITIES = {
            new CharacterDensity('r', 37, false),
            new CharacterDensity('ř', 12, true),
            new CharacterDensity('l', 38, false),
            new CharacterDensity('n', 12, false)
    };


    public static CharacterDensity[] VOWEL_DENSITIES = {
            new CharacterDensity('o', 86, false),
            new CharacterDensity('a', 62, false),
            new CharacterDensity('e', 76, false),
            new CharacterDensity('u', 31, false),
            new CharacterDensity('i', 43, false),
            new CharacterDensity('í', 32, true),
            new CharacterDensity('y', 19, false),
            new CharacterDensity('ý', 10, true),

    };

    public static CharacterDensity[] ALL_CONSONANT_DENSITIES = {
            new CharacterDensity('b', 16, false),
            new CharacterDensity('c', 17, false),
            new CharacterDensity('č', 9, true),
            new CharacterDensity('d', 36, false),
            new CharacterDensity('f', 3, false),

            new CharacterDensity('g', 3, false),
            new CharacterDensity('h', 13, false),
            new CharacterDensity('j', 21, false),
            new CharacterDensity('k', 37, false),

            new CharacterDensity('l', 38, false),
            new CharacterDensity('m', 32, false),
            new CharacterDensity('n', 65, false),
            new CharacterDensity('p', 34, false),

            new CharacterDensity('q', 1, false),
            new CharacterDensity('r', 37, false),
            new CharacterDensity('ř', 12, true),
            new CharacterDensity('s', 45, false),
            new CharacterDensity('š', 8, true),
            new CharacterDensity('t', 57, false),

            new CharacterDensity('v', 46, false),
            new CharacterDensity('w', 1, false),
            new CharacterDensity('x', 1, false),
            new CharacterDensity('z', 21, false),
            new CharacterDensity('ž', 1, true),
    };


    private CharacterTables() {
        // prevent instantiation
    }
}

/*
 * Copyright (c) 2017 BSC Praha, spol. s r.o.
 */

package cz.sevrjukov.bgen;

import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link RandomWordGenerator}
 *
 * @author <a href="mailto:alexandr.sevrjukov@bsc-ideas.com">Alexandr Sevrjukov</a>
 */
public class WordGeneratorTest {


    @Test
    public void testFreeGeneration() {

        final RandomWordGenerator.Settings settings = RandomWordGenerator.Settings
                .builder()
                .allowDiacritics(true)
                .maxSyllablesCount(5)
                .build();

        final RandomWordGenerator tested = new RandomWordGenerator(settings);

        for (int i = 0; i < 1000; i++) {
            String word = tested.generateRandomWord();
            System.out.println(word);
        }

    }


    @Test
    public void testBGeneration() {

        final RandomWordGenerator.Settings settings = RandomWordGenerator.Settings
                .builder()
                .allowDiacritics(false)
                .build();

        final RandomWordGenerator tested = new RandomWordGenerator(settings);

        for (int i = 0; i < 50; i++) {
            String word = tested.generateRandomWord('b');
            System.out.println(word);
        }

    }

    @Test
    public void searchTest() {

        final RandomWordGenerator.Settings settings = RandomWordGenerator.Settings
                .builder()
                .allowDiacritics(true)
                .build();
        final RandomWordGenerator tested = new RandomWordGenerator(settings);

        boolean finished = false;
        for (int i = 1; i <= 1000; i++) {

            if (finished) {
                break;
            }

            System.out.println(i);

            for (int k = 0; k < 1000; k++) {

                String word = tested.generateRandomWord('b');
                if (word.startsWith("bumč")) {
                    System.out.println(word);
                    finished = true;
                    break;
                }
            } // for k

        } // for i
    } // method

}

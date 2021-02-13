/*
 * Copyright (c) 2017 BSC Praha, spol. s r.o.
 */

package cz.sevrjukov.bgen;

import lombok.Data;

/**
 * This class represents a character, including metadata.
 *
 * @author <a href="mailto:alexandr.sevrjukov@bsc-ideas.com">Alexandr Sevrjukov</a>
 */
@Data
public class CharacterDensity {
    private final Character character;
    private final int density;
    private final boolean diacritics;
}

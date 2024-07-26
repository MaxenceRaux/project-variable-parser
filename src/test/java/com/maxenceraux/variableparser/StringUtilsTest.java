package com.maxenceraux.variableparser;

import com.maxenceraux.variableparser.model.Variable;
import com.maxenceraux.variableparser.utils.StringUtils;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/*
 * This file is part of the project project-variable-parser.
 * 
 * project-variable-parser is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * project-variable-parser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with project-variable-parser. If not, see <https://www.gnu.org/licenses/>.
 */


class StringUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"${toto}", "${toto:}", "${toto:titi}"})
    @DisplayName("Should return true if expression defines a variable.")
    void shouldReturnTrueIfExpressionDefinesAVariable(String expression) {
        assertTrue(StringUtils.isSpringVariableExpression(expression));
    }

    @ParameterizedTest
    @ValueSource(strings = {"toto", "$toto:}", "${toto:titi", "{toto}"})
    @DisplayName("Should return false if expression does not define a variable.")
    void shouldReturnFalseIfExpressionDoesNotDefineAVariable(String expression) {
        assertFalse(StringUtils.isSpringVariableExpression(expression));
    }

    @ParameterizedTest
    @DisplayName("Should extract variable from expression.")
    @CsvSource({
            "${toto:titi}   ,toto,titi",
            "${toto:}       ,toto,",
            "${toto}        ,toto,",

    })
    void shouldExtractVariableFromExpression(String expression, String expectedName, String expectedValue) {
        AssertionsForClassTypes.assertThat(StringUtils.extractVariableName(expression))
                .contains(new Variable(expectedName, expectedValue));
    }

    @Test
    @DisplayName("Should return empty if no match.")
    void shouldReturnEmptyIfNoMatch() {
        AssertionsForClassTypes.assertThat(StringUtils.extractVariableName("expression")).isEmpty();
    }


}
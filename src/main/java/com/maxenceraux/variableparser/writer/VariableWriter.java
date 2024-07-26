package com.maxenceraux.variableparser.writer;
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


import com.maxenceraux.variableparser.model.ClassVariable;

import java.util.List;

/**
 * Defines an object that can take variables as input and output it in a specific format
 */
public interface VariableWriter {

    /**
     * Output the several variable in parameter
     * @param classVariables Variables to be written
     */
    void write(List<ClassVariable> classVariables);
}

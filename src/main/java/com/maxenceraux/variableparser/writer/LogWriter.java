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
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;

import java.util.List;

/**
 * Output variables as a series of logs
 */
@Slf4j
public class LogWriter implements VariableWriter {


    @Override
    public void write(List<ClassVariable> classVariables) {
        StreamEx.of(classVariables)
                .forEach(classVariable1 -> {
                    log.info(classVariable1.getPackageName() + " : " + classVariable1.getClassName());
                    StreamEx.of(classVariable1.getVariables())
                            .forEach(variable -> {
                                log.info("Variable name : " + variable.getName());
                                if (variable.hasDefaultValue()) {
                                    log.info("Default value : " + variable.getDefaultValue());
                                }
                            });
                });

    }
}

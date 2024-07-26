package com.maxenceraux.variableparser;

import com.maxenceraux.variableparser.model.ClassVariable;
import com.maxenceraux.variableparser.processor.ClassProcessor;
import com.maxenceraux.variableparser.writer.VariableWriter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

@ExtendWith(MockitoExtension.class)
class AnnotationProcessorMojoTest {

    public static final String OUTPUT = "target/test-output";
    private AnnotationProcessorMojo mojo;

    @Mock
    private VariableWriter writer;

    @Mock
    private ClassProcessor classProcessor;

    @BeforeEach
    void setUp() {
        mojo = new AnnotationProcessorMojo(classProcessor, writer);
        mojo.setOutputDirectory(OUTPUT);
    }

    @Test
    @SneakyThrows
    @DisplayName("Should call service and writer.")
    void shouldCallServiceAndWriter() {
        var variables = List.of(new ClassVariable("name", "package", List.of()));

        when(classProcessor.processClasses(OUTPUT)).thenReturn(variables);

        mojo.execute();

        verify(writer).write(variables);
    }

}
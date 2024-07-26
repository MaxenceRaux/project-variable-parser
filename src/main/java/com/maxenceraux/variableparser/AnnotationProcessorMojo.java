package com.maxenceraux.variableparser;

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

import com.maxenceraux.variableparser.processor.ClassProcessor;
import com.maxenceraux.variableparser.writer.LogWriter;
import com.maxenceraux.variableparser.writer.VariableWriter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@NoArgsConstructor
@Mojo(name = "parse", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class AnnotationProcessorMojo extends AbstractMojo {

    private ClassProcessor classProcessor = new ClassProcessor();
    private VariableWriter variableWriter = new LogWriter();

    @Setter(AccessLevel.PACKAGE)
    @Parameter(property = "project.build.outputDirectory", required = true)
    private String outputDirectory;

    AnnotationProcessorMojo(ClassProcessor classProcessor, VariableWriter variableWriter) {
        this.classProcessor = classProcessor;
        this.variableWriter = variableWriter;
    }

    public void execute() throws MojoExecutionException {
        var classVariables = classProcessor.processClasses(outputDirectory);
        variableWriter.write(classVariables);
    }
}

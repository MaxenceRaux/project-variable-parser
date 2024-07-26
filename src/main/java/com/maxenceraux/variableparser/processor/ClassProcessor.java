package com.maxenceraux.variableparser.processor;

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
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import lombok.RequiredArgsConstructor;
import one.util.streamex.StreamEx;
import org.apache.maven.plugin.MojoExecutionException;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class ClassProcessor {

    private final AnnotationProcessor annotationProcessor;

    public ClassProcessor() {
        annotationProcessor = new GenericAnnotationProcessor();
    }


    public List<ClassVariable> processClasses(String outputDirectory) throws MojoExecutionException {
        try (ScanResult scanResult = new ClassGraph()
                .overrideClasspath(outputDirectory)
                .enableAllInfo()
                .scan()) {

            List<ClassInfo> classInfos = scanResult.getAllClasses();
            return StreamEx.of(classInfos)
                    .map(this::processClass)
                    .toList();
        } catch (Exception e) {
            throw new MojoExecutionException("Error parsing annotations", e);
        }
    }

    public ClassVariable processClass(ClassInfo classInfo) {
        if (classInfo.getAnnotations().isEmpty()) {
            return new ClassVariable(classInfo.getName(), classInfo.getPackageName(), List.of());
        }
        var annotations = collectAnnotations(classInfo);
        var variables = annotationProcessor.processAnnotations(annotations);
        return new ClassVariable(classInfo.getName(), classInfo.getPackageName(), variables);
    }

    private List<AnnotationInfo> collectAnnotations(ClassInfo classInfo) {
        var classAnnotations = classInfo.getAnnotationInfo();
        var fieldInfo = classInfo.getDeclaredFieldInfo()
                .stream()
                .flatMap(fieldInfo1 -> fieldInfo1.getAnnotationInfo().stream());
        var parametersInfo = classInfo.getConstructorInfo()
                .stream()
                .flatMap(methodInfo -> Arrays.stream(methodInfo.getParameterInfo()))
                .flatMap(methodParameterInfo -> methodParameterInfo.getAnnotationInfo().stream());
        var methodInfo = classInfo.getConstructorInfo()
                .stream()
                .flatMap(methodInfo1 -> methodInfo1.getAnnotationInfo().stream());
        return StreamEx.of(fieldInfo)
                .append(parametersInfo)
                .append(methodInfo)
                .append(classAnnotations)
                .toList();
    }

}
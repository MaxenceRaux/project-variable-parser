package com.maxenceraux.variableparser.processor;

import com.maxenceraux.variableparser.model.ClassVariable;
import com.maxenceraux.variableparser.model.Variable;
import io.github.classgraph.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

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
class ClassProcessorTest {

    public static final String OUTPUT = "output";
    public static final String CLASS_NAME = "CLASS";
    public static final String PACKAGE_NAME = "PACKAGE";
    private ClassProcessor classProcessor;

    @Mock
    private ClassGraph classGraph;

    @Mock
    private ClassInfo classInfo;

    @Mock
    private ScanResult scanResult;

    @Mock
    private AnnotationProcessor annotationProcessor;

    @BeforeEach
    void setUp() {
        classProcessor = new ClassProcessor(annotationProcessor);
    }

    @Test
    @SneakyThrows
    @DisplayName("Should process every scanned classes.")
    void shouldProcessEveryScannedClasses() {
        try (var mockedConstruction = mockConstruction(ClassGraph.class, withSettings().defaultAnswer(invocationOnMock -> classGraph))) {

            when(classGraph.enableAllInfo()).thenReturn(classGraph);
            when(classGraph.scan()).thenReturn(scanResult);
            when(scanResult.getAllClasses()).thenReturn(new ClassInfoList(List.of(classInfo)));

            when(classInfo.getAnnotations()).thenReturn(ClassInfoList.emptyList());
            when(classInfo.getName()).thenReturn(CLASS_NAME);
            when(classInfo.getPackageName()).thenReturn(PACKAGE_NAME);

            var variables = classProcessor.processClasses(OUTPUT);

            assertThat(variables)
                    .asList()
                    .containsExactly(new ClassVariable(CLASS_NAME, PACKAGE_NAME, List.of()));
        }
    }

    @Test
    @DisplayName("Should send class annotation to processor.")
    void shouldSendClassAnnotationToProcessor() {
        AnnotationInfoList annotationInfos = new AnnotationInfoList();
        annotationInfos.add(mock(AnnotationInfo.class));
        var expectedVariables = List.of(new Variable("name", null));
        var annotations = new ClassInfoList(List.of(mock(ClassInfo.class)));

        when(classInfo.getAnnotations()).thenReturn(annotations);
        when(classInfo.getAnnotationInfo()).thenReturn(annotationInfos);
        when(classInfo.getConstructorInfo()).thenReturn(MethodInfoList.emptyList());
        when(classInfo.getDeclaredFieldInfo()).thenReturn(FieldInfoList.emptyList());
        when(annotationProcessor.processAnnotations(ArgumentMatchers.argThat(annotationInfos1 -> annotationInfos1.containsAll(annotationInfos) && annotationInfos.containsAll(annotationInfos1))))
                .thenReturn(expectedVariables);

        var variables = classProcessor.processClass(classInfo);

        assertThat(variables).isEqualTo(new ClassVariable(null, null, expectedVariables));
    }

}
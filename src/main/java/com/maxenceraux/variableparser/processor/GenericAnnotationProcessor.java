package com.maxenceraux.variableparser.processor;

import com.maxenceraux.variableparser.utils.StringUtils;
import com.maxenceraux.variableparser.model.Variable;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.AnnotationParameterValue;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class GenericAnnotationProcessor implements AnnotationProcessor {

    @Override
    public List<Variable> processAnnotation(AnnotationInfo annotationInfo) {
        return annotationInfo.getParameterValues()
                .stream().map(AnnotationParameterValue::getValue)
                .map(Object::toString)
                .filter(StringUtils::isSpringVariableExpression)
                .map(StringUtils::extractVariableName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
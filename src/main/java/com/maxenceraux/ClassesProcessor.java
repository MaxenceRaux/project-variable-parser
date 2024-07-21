package com.maxenceraux;

import io.github.classgraph.*;
import lombok.AllArgsConstructor;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import java.util.List;

@AllArgsConstructor
public class ClassesProcessor {

    private Log log;

    public void processClasses(String outputDirectory) throws MojoExecutionException {
        try (ScanResult scanResult = new ClassGraph()
                .overrideClasspath(outputDirectory)
                .enableAllInfo()
                .scan()) {

            List<ClassInfo> classInfos = scanResult.getAllClasses();

            for (ClassInfo classInfo : classInfos) {
                processClass(classInfo);
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Error parsing annotations", e);
        }
    }

    private void processClass(ClassInfo classInfo) {
        if (!classInfo.getAnnotations().isEmpty()) {
            log.info("Class: " + classInfo.getName());
            var annotations = classInfo.getAnnotationInfo();
            processAnnotations(classInfo, annotations);
        }
    }

    private void processAnnotations(ClassInfo classInfo, AnnotationInfoList annotations) {
        classInfo.getDeclaredFieldInfo().forEach(fieldInfo -> annotations.addAll(fieldInfo.getAnnotationInfo()));
        annotations.forEach(this::processAnnotation);
    }

    private void processAnnotation(AnnotationInfo annotationInfo) {
        annotationInfo.getParameterValues()
                .stream().map(AnnotationParameterValue::getValue)
                .map(Object::toString)
                .filter(StringUtils::isSpringVariableExpression)
                .forEach(val -> log.info("Parameter value: " + StringUtils.extractVariableName(val)));
    }
}
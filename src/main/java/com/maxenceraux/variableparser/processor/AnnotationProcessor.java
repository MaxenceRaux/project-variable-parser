package com.maxenceraux.variableparser.processor;

import com.maxenceraux.variableparser.model.Variable;
import io.github.classgraph.AnnotationInfo;
import one.util.streamex.StreamEx;

import java.util.List;

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

public interface AnnotationProcessor {

    default List<Variable> processAnnotations(List<AnnotationInfo> annotations) {
        return StreamEx.of(annotations).flatCollection(this::processAnnotation).toList();
    }

    List<Variable> processAnnotation(AnnotationInfo annotationInfo);
}

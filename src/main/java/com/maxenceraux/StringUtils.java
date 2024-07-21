package com.maxenceraux;

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

import com.maxenceraux.model.Variable;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.regex.Pattern;

@UtilityClass
public class StringUtils {

    private final Pattern variablePattern = Pattern.compile("\\$\\{[^}]*}");
    private final Pattern variableNamePattern = Pattern.compile("\\$\\{([^:}]+).*}");
    private final Pattern variableWithDafeaultPattern = Pattern.compile("\\$\\{([^:}]+):([^}]+)}");

    public static boolean isSpringVariableExpression(String expression) {
        return variablePattern.matcher(expression).matches();
    }

    public static Optional<Variable> extractVariableName(String expression) {
        var matcher = variableNamePattern.matcher(expression);
        if (matcher.find()) {
            String name = matcher.group(1);
            matcher = variableWithDafeaultPattern.matcher(expression);
            if (matcher.find()) {
                return Optional.of(new Variable(name, matcher.group(2)));
            }
            return Optional.of(new Variable(name, null));
        }
        return Optional.empty();
    }
}

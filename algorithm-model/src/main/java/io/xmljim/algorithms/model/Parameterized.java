/*
 * Copyright 2021-2022 Jim Earley (xml.jim@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.xmljim.algorithms.model;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A base interface for any entity requiring parameters (e.g., functions, models).
 * Parameters should be accessible by name ({@link Parameter#getName()}), variable ({@link Parameter#getVariable()}) (if present),
 * and parameter type ({@link Parameter#getParameterType()}).
 * <p>
 *     Parameters and parameter values can be access using the {@link #find(Predicate)} method that uses predicates
 *     to locate the requested parameter, or using any of the {@code get*} methods, some of which use the {@code find}
 *     method.
 * </p>
 */
public interface Parameterized extends VariableEntity {

    /**
     * Return an optional (nullable) of a parameter by name. Using
     * an {@link Optional} allows for consumers to implement specific
     * logic in the event that the named parameter is not present.
     * @param name the parameter name
     * @return an optional parameter. Note that if more than one parameter exists
     * for a given name, then the first parameter found is returned
     */
    default Optional<Parameter<?>> getParameter(String name) {
        return find(parameterName(name));
    }

    /**
     * Return an optional parameter by both name and variable. Using
     * an {@link Optional} allows for consumers to implement specific
     * logic in the event that the named parameter is not present.
     * @param name the parameter name
     * @param variable the variable
     * @return an optional parameter that can be accessed through {@link Optional#get()}
     */
    default Optional<Parameter<?>> getParameter(String name, String variable) {
        return find(parameterNameVariable(name, variable));
    }

    /**
     * Return an optional parameter by variable name.
     * @param variable the variable name
     * @return an optional parameter. If more than one parameter with the same
     * variable exists, then the first parameter found is returned
     */
    default Optional<Parameter<?>> getParameterFromVariable(String variable) {
        return find(parameterVariable(variable));
    }

    /**
     * Return a parameter by index position
     * @param index the index
     * @return the parameter at the specified index or null
     */
    Parameter<?> getParameter(int index);

    /**
     * Return the parameter value for a parameter at a given index position
     * @param index the index position
     * @param <T> The assigned parameter value type
     * @return the parameter value
     */
    default <T> T getValue(int index) {
        return (T) getParameter(index).getValue();
    }

    /**
     * Return a parameter value for a given named parameter
     * @param name the parameter name
     * @param <T> the assigned parameter value type
     * @return the parameter value for a given named parameter. If
     * more than one parameter exists with the same name, the first
     * parameter found is returned
     */
    @SuppressWarnings("unchecked")
    default <T> T getValue(String name) {
        Optional<Parameter<?>> parameter = getParameter(name);
        return (T) parameter.map(Parameter::getValue).orElse(null);
    }

    /**
     * Return a parameter value from a given parameter by name and variable
     * @param name the parameter name
     * @param variable the variable
     * @param <T> the assigned value for the parameter value
     * @return the value for the parameter or null if no parameter is found. Note
     * that if there is more than one parameter with the same name and variable,
     * the first parameter found is returned.
     */
    @SuppressWarnings("unchecked")
    default <T> T getValue(String name, String variable) {
        Optional<Parameter<?>> parameter = getParameter(name, variable);
        return (T) parameter.map(Parameter::getValue).orElse(null);
    }

    default Optional<ParameterTypes> getParameterType(String name) {
        Optional<Parameter<?>> parameter = getParameter(name);
        return Optional.ofNullable(parameter.map(Parameter::getParameterType).orElse(null));
    }

    /**
     * Return a parameter value from a given parameter by variable name
     * @param variable the variable
     * @param <T> the assigned value for the parameter value
     * @return the value for the parameter or null if no parameter is found. Note
     * that if there is more than one parameter with the same variable name,
     * the first parameter found is returned.
     */
    @SuppressWarnings("unchecked")
    default <T> T getValueFromVariable(String variable) {
        Optional<Parameter<?>> parameter = getParameterFromVariable(variable);
        return (T) parameter.map(Parameter::getValue).orElse(null);
    }

    /**
     * Returns whether a parameter exists for a given parameter name
     * @param name the parameter name
     * @return {@code true} if the parameter exists; {@code false} otherwise
     */
    default boolean hasParameter(String name) {
        return find(parameterName(name)).isPresent();
    }

    /**
     * Returns whether a parameter exists for a given name and parameter
     * @param name the parameter name
     * @param variable the variable
     * @return {@code true} if the parameter exists; {@code false} otherwise
     */
    default boolean hasParameter(String name, String variable) {
        return find(parameterNameVariable(name, variable)).isPresent();
    }

    /**
     * Returns whether a parameter exists for a given name, variable and parameter type
     * @param name the parameter name
     * @param variable the variable
     * @param type the parameter type
     * @return {@code true} if the parameter exists; {@code false} otherwise
     */
    default boolean hasParameter(String name, String variable, ParameterTypes type) {
        return find(parameterNameVariableType(name, variable, type)).isPresent();
    }

    /**
     * Find a parameter with a predicate
     * @param parameterPredicate a predicate that applies a boolean test against a parameter
     * @return an optional parameter.
     */
    default Optional<Parameter<?>> find(Predicate<Parameter<?>> parameterPredicate) {
        return stream().filter(parameterPredicate).findFirst();
    }

    /**
     * Return a stream of parameters
     * @return the parameter stream
     */
    Stream<Parameter<?>> stream();

    /**
     * Predicate for locating a parameter by name.
     * <p>
     *     Effectively syntactic sugar for applying the lambda: {@code p -> p.getName().equals(name)}
     * </p>
     * @param name the parameter name to apply to the predicate
     * @return a predicate that tests for the presence of a given named parameter
     */
    default Predicate<Parameter<?>> parameterName(String name) {
        return p -> p.getName().equals(name);
    }

    /**
     * Predicate for locating a parameter by variable name
     * <p>
     *     Syntactic sugar for applying the lambda {@code p -> p.getVariable().equals(name)}
     * </p>
     *
     * @param variable the variable name
     * @return a predicate that tests for presence of a parameter by a given variable name
     */
    default Predicate<Parameter<?>> parameterVariable(String variable) {
        return p -> p.getVariable().equals(variable);
    }

    /**
     * Predicate for locating a parameter by name and variable
     * <p>
     *     Syntactic sugar for the applying the lambda
     *     <pre>p -> p.getName().equals(name) &amp;&amp; p.getVariable().equals(variable)}</pre>
     * </p>
     * <p>Internally uses predicate chaining using the {@link Predicate#and(Predicate)} method to
     * combine {@link #parameterName(String)} and {@link #parameterVariable(String)} predicates into
     * a single predicate</p>
     * @param name the parameter name
     * @param variable the parameter variable
     * @return the predicate for locating a parameter by name and variable
     */
    default Predicate<Parameter<?>> parameterNameVariable(String name, String variable) {
        return parameterName(name).and(parameterVariable(variable));
    }

    /**
     * Predicate for locating a parameter by parameter type
     * <p>
     *     Syntactic sugar for the applying the lambda
     *     <pre>p -> p.getName().getParameterType.equals(parameterType)</pre>
     * </p>
     * @param parameterType the parameter name
     * @return the predicate for locating a parameter by name and variable
     */
    default Predicate<Parameter<?>> parameterType(ParameterTypes parameterType) {
        return p -> p.getParameterType().equals(parameterType);
    }

    /**
     * Return a predicate to locate a parameter by name and parameter type
     * <p>
     *     Syntactic sugar for the lambda:
     *     <pre>p -> p.getName().equals(name) &amp;&amp; p.getParameterType().equals(parameterType)</pre>
     * </p>
     * <p>Internally uses predicate chaining using the {@link Predicate#and(Predicate)} method to
     * combine {@link #parameterName(String)} and {@link #parameterType(ParameterTypes)} predicates into
     * a single predicate</p>
     * @param name the parameter name
     * @param parameterType the parameter type
     * @return a predicate to locate a parameter by name and parameter type
     */
    default Predicate<Parameter<?>> parameterNameAndType(String name, ParameterTypes parameterType) {
        return parameterName(name).and(parameterType(parameterType));
    }

    /**
     * Return a predicate to locate a parameter by name and parameter type
     * <p>
     *     Syntactic sugar for the lambda:
     *     <pre>p -> p.getVariable().equals(name) &amp;&amp; p.getParameterType().equals(parameterType)</pre>
     * </p>
     * <p>Internally uses predicate chaining using the {@link Predicate#and(Predicate)} method to
     * combine {@link #parameterName(String)} and {@link #parameterType(ParameterTypes)} predicates into
     * a single predicate</p>
     * @param variable the parameter name
     * @param parameterType the parameter type
     * @return a predicate to locate a parameter by variable and parameter type
     */
    default Predicate<Parameter<?>> parameterVariableAndType(String variable, ParameterTypes parameterType) {
        return parameterVariable(variable).and(parameterType(parameterType));
    }

    /**
     * Return a predicate to locate a parameter by name, variable, and parameter type
     * <p>
     *     Syntactic sugar for the lambda:
     *     <pre>p -> p.getVariable().equals(name) &amp;&amp; p.getVariable.equals(variable) &amp;&amp; p.getParameterType().equals(parameterType)</pre>
     * </p>
     * <p>Internally uses predicate chaining using the {@link Predicate#and(Predicate)} method to
     * combine {@link #parameterName(String)}, {@link #parameterVariable(String)} and {@link #parameterType(ParameterTypes)} predicates into
     * a single predicate</p>
     *
     * @param name the parameter name
     * @param variable the parameter variable
     * @param parameterType the parameter type
     * @return a predicate to locate a parameter by name, variable, and parameter type
     */
    default Predicate<Parameter<?>> parameterNameVariableType(String name, String variable, ParameterTypes parameterType) {
        return parameterName(name).and(parameterVariable(variable)).and(parameterType(parameterType));
    }
}
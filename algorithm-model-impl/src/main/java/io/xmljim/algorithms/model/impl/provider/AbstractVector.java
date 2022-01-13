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

package io.xmljim.algorithms.model.impl.provider;
import io.xmljim.algorithms.model.AbstractVariableEntity;
import io.xmljim.algorithms.model.Vector;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class AbstractVector<T> extends AbstractVariableEntity implements Vector<T> {
    private final List<T> data;

    public AbstractVector(String name, String variable, Stream<T> stream) {
        super(name, variable);
        data = stream.collect(Collectors.toList());
    }

    @Override
    public T first() {
        return data.get(0);
    }

    @Override
    public T last() {
        return data.get(data.size() - 1);
    }

    @Override
    public T get(final long index) {
        return data.get((int)index);
    }

    @Override
    public int length() {
        return data.size();
    }

    public int getElementSize() {
        return data.size();
    }

    @Override
    public Stream<T> stream() {
        return data.stream();
    }

    @Override
    public Iterator<T> iterator() {
        return data.iterator();
    }
}

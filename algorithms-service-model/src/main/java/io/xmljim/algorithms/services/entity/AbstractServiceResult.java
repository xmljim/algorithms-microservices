package io.xmljim.algorithms.services.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.StringJoiner;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractServiceResult<T> implements ServiceResult<T> {
    private String description;
    private T data;
    
    public AbstractServiceResult(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AbstractServiceResult.class.getSimpleName() + "[", "]")
                .add("description='" + description + "'")
                .add("data=" + data)
                .toString();
    }
}

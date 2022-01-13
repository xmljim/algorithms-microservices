package io.xmljim.algorithms.services.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
public abstract class AbstractServiceResponse<T extends ServiceResult<?>> implements ServiceResponse<T> {    
    private List<T> results;
    private int count;
    
    public AbstractServiceResponse(T serviceResult) {
        this.results = serviceResult != null ? List.of(serviceResult) : new ArrayList<>();
    }
    
    public AbstractServiceResponse(List<T> serviceResults) {
        this.results = serviceResults;
    }
    
    public int getCount() {
        return count == 0 ? (this.results == null ? 0 : this.results.size()) : count;
    }
}

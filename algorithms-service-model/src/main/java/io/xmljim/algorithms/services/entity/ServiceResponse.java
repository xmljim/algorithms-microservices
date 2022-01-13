package io.xmljim.algorithms.services.entity;

import java.util.List;

public interface ServiceResponse<T extends ServiceResult<?>> {

    List<T> getResults();

    int getCount();
}

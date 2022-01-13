package io.xmljim.algorithms.service.cpi.repository;

import io.xmljim.algorithms.service.cpi.entity.CPI;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CPIRepository extends CrudRepository<CPI, Long> {

    CPI findByYear(@Param("year") int year);

    @Query("select c from CPI c where c.year between ?1 and ?2")
    List<CPI> findAllBetween(int yearStart, int yearEnd);


}

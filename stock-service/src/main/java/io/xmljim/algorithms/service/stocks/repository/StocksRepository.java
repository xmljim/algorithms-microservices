package io.xmljim.algorithms.service.stocks.repository;


import io.xmljim.algorithms.service.stocks.entity.MarketData;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface StocksRepository extends CrudRepository<MarketData, Long> {

    @Query("select distinct m.market from MarketData m")
    List<String> findDistinctMarkets();

    @Query("select m from MarketData m where m.market = ?1 order by m.year")
    List<MarketData> findByMarket(String market, Sort sort);

    @Query("select m from MarketData m where m.year = ?1 and m.market = ?2")
    Optional<MarketData> findByYearAndMarket(Integer year, String market);

    @Query("select m from MarketData m where m.year = ?1")
    List<MarketData> findMarketsByYear(Integer year);

    @Query("select m from MarketData m where m.market = ?1 and m.year >= ?2")
    List<MarketData> findMarketFromYear(String market, Integer year);

    @Query("select m from MarketData m where m.year >= ?1 order by m.market, m.year")
    List<MarketData> findAllFromYear(Integer year, Sort sort);

    List<MarketData> findByYearBetween(Integer yearStart, Integer yearEnd);

    List<MarketData> findByMarketEqualsAndYearIsBetween(String market, Integer yearStart, Integer yearEnd, Sort sort);

    @Query("select (count(m) > 0) from MarketData m where upper(m.market) = upper(?1) and m.year = ?2")
    boolean marketYearExists(String market, Integer year);





}

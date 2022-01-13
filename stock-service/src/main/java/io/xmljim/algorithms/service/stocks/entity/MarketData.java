package io.xmljim.algorithms.service.stocks.entity;

import io.xmljim.algorithms.services.entity.stocks.MarketEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "MarketData",
        indexes = {
                @Index(name = "idx_markethistory_year", columnList="year"),
                @Index(name = "idx_markethistory_year_market", columnList = "year, market")
        }
)
public class MarketData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "open", nullable = false)
    private double open;

    @Column(name = "close", nullable = false)
    private double close;

    @Column(name = "market", nullable = false)
    private String market;


    public MarketEntity convert() {
        return new MarketEntity(market, year, open, close);
    }
}

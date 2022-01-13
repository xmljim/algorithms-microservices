package io.xmljim.algorithms.service.cpi.entity;

import lombok.*;

import javax.persistence.*;

@Table(
        name="cpi",
        indexes = {
                @Index(name = "idx_cpi_year", columnList = "year")
        }
)
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CPI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year", nullable = false)
    private int year;

    @Column(name = "cpi_rate", nullable = false)
    private double cpiRate;
}

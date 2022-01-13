package io.xmljim.algorithms.services.entity.date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PeriodRange {
    Map<String, Integer> period;

    public static PeriodRange betweenYears(int yearStart, int yearEnd) {
        Map<String, Integer> period = new HashMap<>();
        period.put("yearStart", yearStart);
        period.put("yearEnd", yearEnd);
        return new PeriodRange(period);
    }

    public static PeriodRange forYear(int year) {
        Map<String, Integer> period = new HashMap<>();
        period.put("year", year);
        return new PeriodRange(period);
    }

    public static PeriodRange referenceRange(int forYear, int baseYear) {
        Map<String, Integer> period = new HashMap<>();
        period.put("forYear", forYear);
        period.put("baseYear", baseYear);
        return new PeriodRange(period);
    }

    public static PeriodRange durationYears(int years) {
        Map<String, Integer> period = new HashMap<>();
        period.put("years", years);
        return new PeriodRange(period);
    }

    public static PeriodRange fromYear(int year) {
        Map<String, Integer> period = new HashMap<>();
        period.put("sinceYear", year);
        return new PeriodRange(period);
    }
}

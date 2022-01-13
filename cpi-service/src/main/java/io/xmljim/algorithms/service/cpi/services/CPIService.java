package io.xmljim.algorithms.service.cpi.services;

import io.xmljim.algorithms.service.cpi.entity.CPI;
import io.xmljim.algorithms.service.cpi.repository.CPIRepository;
import io.xmljim.algorithms.services.entity.cpi.CPIComputedResult;
import io.xmljim.algorithms.services.entity.cpi.CPIEntityResult;
import io.xmljim.algorithms.services.entity.cpi.CPIResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class CPIService {

    @Autowired
    private CPIRepository repository;

    public CPIEntityResult getCPIForYear(final int year) {
        log.info("in CPIService::getCPIForYear: {}", year);
        Optional<CPI> optionalData = getCpiDataForYear(year);

        if (optionalData.isPresent()) {
            return new CPIEntityResult("CPI Rate for " + year, optionalData.get().getCpiRate(), year);
        }

        return null;
    }

    private Optional<CPI> getCpiDataForYear(final int year) {
        return Optional.ofNullable(repository.findByYear(year));
    }

    public CPIComputedResult computeInflationMultiplier(final int forYear, final int baseYear) {
        Optional<CPI> yearCPI = getCpiDataForYear(forYear);
        Optional<CPI> baseCPI = getCpiDataForYear(baseYear);

        double multiplier = 0.0;

        if (yearCPI.isPresent() && baseCPI.isPresent()) {
            multiplier = yearCPI.get().getCpiRate() / baseCPI.get().getCpiRate();
        }

        String description = "Inflation multiplier for " + forYear + " from base year " + baseYear;

        return new CPIComputedResult(description, multiplier);
    }

    public List<CPIEntityResult> getAll() {
        List<CPIEntityResult> results = new ArrayList<>();
        repository.findAll().forEach(cpi -> results.add(new CPIEntityResult("CPI Data for year " + cpi.getYear(), cpi.getCpiRate(), cpi.getYear())));

        return results;
    }

    public List<CPIEntityResult> getAllBetween(final int startYear, final int endYear) {
        log.debug("in CPIService::getAllBetween: {} - {}", startYear, endYear);
        List<CPIEntityResult> results = new ArrayList<>();
        repository.findAllBetween(startYear, endYear).forEach(cpi -> results.add(new CPIEntityResult("CPI Data for year " + cpi.getYear(), cpi.getCpiRate(), cpi.getYear())));

        return results;
    }

    public CPIComputedResult getAverageInflation(int previousNumberOfYears) {
        List<CPIEntityResult> cpiEntityResults = getAll();

        List<CPIEntityResult> data = cpiEntityResults.stream().sorted(Comparator.comparing(CPIEntityResult::getYear).reversed()).collect(Collectors.toList());

        double average = IntStream.range(0, previousNumberOfYears)
                .mapToDouble(i -> {
                    double rate = data.get(i).getData();
                    double previousYear = data.get(i + 1).getData();
                    return (rate - previousYear) / previousYear;
                }).average()
                .orElse(0.0);

        return new CPIComputedResult("Average Inflation (last " + previousNumberOfYears + " years)", average);
    }


}

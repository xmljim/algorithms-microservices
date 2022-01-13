package io.xmljim.algorithms.service.cpi.controller;

import io.xmljim.algorithms.service.cpi.services.CPIService;
import io.xmljim.algorithms.services.entity.cpi.CPIComputedResult;
import io.xmljim.algorithms.services.entity.cpi.CPIEntityResult;
import io.xmljim.algorithms.services.entity.cpi.CPIResponse;
import io.xmljim.algorithms.services.entity.date.PeriodRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cpi")
public class CPIController {

    @Autowired
    private CPIService cpiService;

    @GetMapping("/{year}")
    public CPIResponse<CPIEntityResult> getCPIForYear(@PathVariable("year") int year) {
        return new CPIResponse<>(cpiService.getCPIForYear(year), PeriodRange.forYear(year));
    }

    @GetMapping("/")
    public CPIResponse<CPIEntityResult> listCPIHistory() {
        List<CPIEntityResult> cpiList = cpiService.getAll();
        int yearStart = cpiList.isEmpty() ? -1 : cpiList.get(0).getYear();
        int yearEnd = cpiList.isEmpty() ? -1 : cpiList.get(cpiList.size() - 1).getYear();

        return new CPIResponse<>(cpiList, PeriodRange.betweenYears(yearStart, yearEnd));
    }

    @GetMapping("/{startYear}/{endYear}")
    public CPIResponse<CPIEntityResult> getCPIBetween(@PathVariable("startYear") int startYear, @PathVariable("endYear") int endYear) {
        List<CPIEntityResult> cpiList = cpiService.getAllBetween(startYear, endYear);
        int yearStart = cpiList.isEmpty() ? -1 : cpiList.get(0).getYear();
        int yearEnd = cpiList.isEmpty() ? -1 : cpiList.get(cpiList.size() - 1).getYear();

        return new CPIResponse<>(cpiList, PeriodRange.betweenYears(yearStart, yearEnd));
    }

    @GetMapping("/inflation/{year}/{baseYear}")
    public CPIResponse<CPIComputedResult> getInflationMultiplier(@PathVariable("year") int year, @PathVariable("baseYear") int baseYear) {
        return new CPIResponse<>(cpiService.computeInflationMultiplier(year, baseYear), PeriodRange.referenceRange(year, baseYear));
    }

    @GetMapping("/inflation/rate/{lastNumberOfYears}")
    public CPIResponse<CPIComputedResult> getAverageInflationRate(@PathVariable("lastNumberOfYears") int lastNumberOfYears) {
        return new CPIResponse<>(cpiService.getAverageInflation(lastNumberOfYears), PeriodRange.durationYears(lastNumberOfYears));
    }
}

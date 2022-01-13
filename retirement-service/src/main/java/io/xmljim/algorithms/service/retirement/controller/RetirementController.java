package io.xmljim.algorithms.service.retirement.controller;

import io.xmljim.algorithms.service.retirement.entity.RetirementInput;
import io.xmljim.algorithms.service.retirement.service.RetirementService;
import io.xmljim.algorithms.services.entity.DefaultServiceResponse;
import io.xmljim.algorithms.services.entity.ServiceResponse;
import io.xmljim.algorithms.services.entity.statistics.ModelResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/retirement")
@Slf4j
public class RetirementController {

    @Autowired
    RetirementService retirementService;

    @PostMapping("/estimate")
    public ServiceResponse<ModelResult> calculateRetirement(@RequestBody final RetirementInput input) {
        log.info("RetirementInput: {}", input);
        ModelResult result = retirementService.getRetirementModel(input);
        log.info("ModelResult: {}", result);
        return new DefaultServiceResponse<>(result);
    }
}

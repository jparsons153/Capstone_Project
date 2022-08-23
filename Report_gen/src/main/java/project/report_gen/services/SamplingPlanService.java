package project.report_gen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.report_gen.models.SamplingPlan;
import project.report_gen.models.ValidationStrategy;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SamplingPlanService {
    List<SamplingPlan> samplePlanList = new ArrayList<>();

    // Update method to invoke and return repository.findAll
    // create and save some report objects
    public List<SamplingPlan> getAllSamplingPlans() {
        return samplePlanList;
    }

    // Update method to invoke and return repository.save(report)
    public SamplingPlan savePlan(SamplingPlan plan) {
        samplePlanList.add(plan);
        return plan;
    }
}

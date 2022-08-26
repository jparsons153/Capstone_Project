package project.report_gen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.report_gen.models.Product;
import project.report_gen.models.ValidationStrategy;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ValidationService {

    List<ValidationStrategy> valList = new ArrayList<>();

    // Update method to invoke and return repository.findAll
    // create and save some report objects
    public List<ValidationStrategy> getAllVals() {
        return valList;
    }

    // Update method to invoke and return repository.save(report)
    public ValidationStrategy saveVal(ValidationStrategy val) {
        valList.add(val);
        return val;
    }

    public ValidationStrategy getVal(Long id){
        return valList.get(Math.toIntExact(id));
    }
}
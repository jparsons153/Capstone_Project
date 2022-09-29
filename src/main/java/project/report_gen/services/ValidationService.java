package project.report_gen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.report_gen.models.*;

import java.util.*;

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

    public ValidationStrategy getVal(int id){
        return valList.get(id);
    }

    public Boolean deleteAllVals(){return valList.removeAll(valList);}

}
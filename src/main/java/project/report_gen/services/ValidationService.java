package project.report_gen.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.report_gen.exceptions.NoSuchValidationException;
import project.report_gen.models.*;
import project.report_gen.repos.ValidationRepo;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ValidationService {

    @Autowired
    final ValidationRepo validationRepo;

    //List<ValidationStrategy> valList = new ArrayList<>();

    // Update method to invoke and return repository.findAll
    // create and save some report objects
    @Transactional
    public List<ValidationStrategy> getAllVals() {
        ArrayList<ValidationStrategy> valList = new ArrayList<>(validationRepo.findAll());

        return valList;
    }

    // Update method to invoke and return repository.save(report)
    @Transactional
    public ValidationStrategy saveVal(ValidationStrategy val) {
        validationRepo.save(val);
        return val;
    }

    @Transactional
    public ValidationStrategy getVal(Long id) throws NoSuchValidationException {
        Optional<ValidationStrategy> valOptional = validationRepo.findById(id);

        if (valOptional.isEmpty()){
            throw new NoSuchValidationException("No validation with ID " + id + "could be found");
        }

        ValidationStrategy validation = valOptional.get();
        return validation;
    }

    @Transactional
    public void deleteAllVals(){
            validationRepo.deleteAll();
    }
}
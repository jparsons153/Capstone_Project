package project.report_gen.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode

public class TableRow {

    private Long id;
    int sampleSize;

    // map each AQL to AcceptReject
    Map<Double, AcceptReject> acceptRejectHashMap;

    @Override
    public String toString() {
        System.out.println("Sample table for selected sampleSize");
        acceptRejectHashMap.entrySet().forEach(System.out::println);

        return "sampleSize=" + sampleSize;
    }
}
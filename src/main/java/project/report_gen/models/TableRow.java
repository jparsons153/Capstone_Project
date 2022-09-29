package project.report_gen.models;

import lombok.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TableRow {

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
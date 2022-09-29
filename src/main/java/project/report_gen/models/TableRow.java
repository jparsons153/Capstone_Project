package project.report_gen.models;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TableRow {

    int sampleSize;

    // map each AQL to AcceptReject
    Map<Double, AcceptReject> acceptRejectHashMap;
}
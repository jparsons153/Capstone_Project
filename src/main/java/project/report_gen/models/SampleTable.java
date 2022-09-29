package project.report_gen.models;

import lombok.*;

import java.util.ArrayList;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SampleTable {
    String tableName;
    ArrayList<TableRow> tableRows = new ArrayList<>();

    @Override
    public String toString() {
        return "SampleTable" + tableName ;
    }
}

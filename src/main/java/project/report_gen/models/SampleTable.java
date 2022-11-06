package project.report_gen.models;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class SampleTable {

    private Long id;
    private String tableName;
    private List<TableRow> tableRows;

    @Override
    public String toString() {
        return "SampleTable" + tableName ;
    }
}
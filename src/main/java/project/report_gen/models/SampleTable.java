package project.report_gen.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SampleTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String tableName;
    private ArrayList<TableRow> tableRows = new ArrayList<>();

    @Override
    public String toString() {
        return "SampleTable" + tableName ;
    }
}

package project.report_gen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.report_gen.models.AcceptReject;
import project.report_gen.models.SampleTable;
import project.report_gen.models.TableRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SampleTableConfig {

    @Bean
    public SampleTable sampleTable(){
        AcceptReject zeroOne = new AcceptReject(0, 1);
        AcceptReject oneTwo = new AcceptReject(1,2);
        AcceptReject twoThree = new AcceptReject(2,3);
        AcceptReject threeFour = new AcceptReject(3,4);

        Map<Double, AcceptReject> acceptRejectMap_GL2_800pcs_normal = new HashMap<>();
        acceptRejectMap_GL2_800pcs_normal.put(0.015,zeroOne);
        acceptRejectMap_GL2_800pcs_normal.put(0.025,zeroOne);
        acceptRejectMap_GL2_800pcs_normal.put(0.040,zeroOne);
        acceptRejectMap_GL2_800pcs_normal.put(0.065,oneTwo);
        acceptRejectMap_GL2_800pcs_normal.put(0.10,twoThree);
        acceptRejectMap_GL2_800pcs_normal.put(0.15,threeFour);

        TableRow normalGL2_800_row = TableRow.builder()
                .sampleSize(800)
                .acceptRejectHashMap(acceptRejectMap_GL2_800pcs_normal)
                .build();

        ArrayList<TableRow> normalGL2rows = new ArrayList<>();
        normalGL2rows.add(normalGL2_800_row);

        SampleTable normalGeneralLevelIIsampleTable = SampleTable.builder()
                .tableName("GL2")
                .tableRows(normalGL2rows)
                .build();

        return normalGeneralLevelIIsampleTable;
    }

}

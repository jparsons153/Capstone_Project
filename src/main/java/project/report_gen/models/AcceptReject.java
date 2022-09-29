package project.report_gen.models;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class AcceptReject {
    int accept;
    int reject;

    @Override
    public String toString() {
        return "acc" + accept + "/" +
                "rej" + reject;
    }
}

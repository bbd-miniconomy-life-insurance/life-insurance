package bbd.miniconomy.lifeinsurance.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CountDTO {
    private Long transactionCount;
    private Long policyCount;
}
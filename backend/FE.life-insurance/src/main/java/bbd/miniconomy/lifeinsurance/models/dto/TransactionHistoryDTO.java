package bbd.miniconomy.lifeinsurance.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionHistoryDTO {
    private Long id;
    private String reference;
    private Long amount;
    private String date;
}

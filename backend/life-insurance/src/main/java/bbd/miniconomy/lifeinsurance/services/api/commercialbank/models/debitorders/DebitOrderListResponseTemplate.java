package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.debitorders;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DebitOrderListResponseTemplate {
    int pageIndex;
    int itemsPerPage;
    int currentItemCount;
    List<DebitOrderResponse> items;
}
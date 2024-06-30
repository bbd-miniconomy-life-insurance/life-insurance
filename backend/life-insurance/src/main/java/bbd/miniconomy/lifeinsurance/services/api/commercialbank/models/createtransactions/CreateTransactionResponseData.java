package bbd.miniconomy.lifeinsurance.services.api.commercialbank.models.createtransactions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateTransactionResponseData {
    private int pageIndex;
    private int itemsPerPage;
    private int currentItemCount;
    private List<CreateTransactionResponseItem> items;
}

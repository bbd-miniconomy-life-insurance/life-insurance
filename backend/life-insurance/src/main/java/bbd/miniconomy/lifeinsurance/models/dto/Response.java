package bbd.miniconomy.lifeinsurance.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Response {
    private int status;
    private String message;
}
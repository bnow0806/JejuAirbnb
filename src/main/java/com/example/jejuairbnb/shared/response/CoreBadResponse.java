package com.example.jejuairbnb.shared.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO representing the response of creating a material")
public class CoreBadResponse {
    @Schema(description = "titleMessage", example = "titleMessage")
    private String titleMessage;
    @Schema(description = "detailMessage", example = "detailMessage")
    private String detailMessage;
    @Schema(description = "statusCode", example = "400")
    private int statusCode;
    @Schema(description = "errorId", example = "1")
    private int errorId;
}

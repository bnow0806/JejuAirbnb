package com.example.jejuairbnb.shared.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "get temporary response dto")
public class CoreSuccessResponseWithData {
    @Schema(description = "successful boolean", example = "true")
    private boolean ok;

    @Schema(description = "Message related to the operation", example = "get temporaty save successfully")
    private String message;

    @Schema(description = "Status code of the operation", example = "200")
    private int statusCode;

    @Schema(description = "data")
    private Object data;
}

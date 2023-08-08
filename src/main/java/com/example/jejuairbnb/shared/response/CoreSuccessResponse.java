package com.example.jejuairbnb.shared.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@Schema(description = "success response dto")
public class CoreSuccessResponse {
    @Schema(description = "success response boolean", example = "true")
    private boolean ok;

    @Schema(description = "success response message", example = "success response successfully")
    private String message;

    @Schema(description = "success response statuscode", example = "201")
    private int statusCode;
}

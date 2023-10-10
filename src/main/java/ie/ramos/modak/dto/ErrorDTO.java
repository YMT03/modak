package ie.ramos.modak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {
    private String id;
    private String title;
    private String description;
    private int httpStatus;
    private OffsetDateTime date;
}

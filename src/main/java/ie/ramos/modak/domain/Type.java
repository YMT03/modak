package ie.ramos.modak.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Type {
    MARKETING("marketing"),
    NEWS("news"),
    STATUS("status");

    private final String value;
}

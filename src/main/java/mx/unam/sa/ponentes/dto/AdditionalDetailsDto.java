package mx.unam.sa.ponentes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.unam.sa.ponentes.models.Authority;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdditionalDetailsDto {
    private String phoneNumber;
    private String address;
    private Authority authority;
}

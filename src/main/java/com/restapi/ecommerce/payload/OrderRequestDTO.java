package com.restapi.ecommerce.payload;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
	@NotNull
	private List<AddressDTO> addressDTOList;
	@NotNull
	private CartDTO cartDTO;
	private String pgName;
    private String pgPaymentId;
    private String pgStatus;
    private String pgResponseMessage;
}

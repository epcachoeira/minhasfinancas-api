package com.epc.minhasfinancas.api.dto;


public class AtualizaStatusDTO {

	private String status;

	public AtualizaStatusDTO() {
	}

	public AtualizaStatusDTO(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}

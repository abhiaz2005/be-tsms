package com.tsms.service;

import java.util.List;

import com.tsms.dto.FeesDto;
import com.tsms.dto.Response;

public interface FeesService {

	Response<?> getAllFees();
	
	Response<?> getAllFeesV2(Integer year);

	Response<?> getAllFeesByStudent(Long id);

	Response<?> createBulkFee(List<FeesDto> fees);

}

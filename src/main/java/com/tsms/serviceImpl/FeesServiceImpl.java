package com.tsms.serviceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.tsms.dto.FeesDto;
import com.tsms.dto.Response;
import com.tsms.dto.UserDto;
import com.tsms.dto.UserFeeDto;
import com.tsms.entity.Fees;
import com.tsms.entity.User;
import com.tsms.enums.Role;
import com.tsms.repository.UserRepository;
import com.tsms.security.CustomizedUserDetailsService;
import com.tsms.service.FeesService;

import jakarta.transaction.Transactional;

@Service
public class FeesServiceImpl implements FeesService {

	@Autowired
	private FeesRepository feesRepository;

	@Autowired
	private UserRepository userRepository ;
	
	@Autowired
	private CustomizedUserDetailsService customizedUserDetailsService;

	@Override
	public Response<?> getAllFees() {
		try {
			Optional<User> userDetailsOptional = customizedUserDetailsService.getUserDetails();
			if (userDetailsOptional.isPresent() && userDetailsOptional.get().getRole().equals(Role.ADMIN)) {
				List<Fees> feesList = feesRepository.findAll();
				return new Response<>(HttpStatus.OK.value(), feesList.isEmpty() ? "No data present" : "OK",
						!feesList.isEmpty() ? feesList.stream().map(e -> e.convertToDto()).collect(Collectors.toList())
								: Collections.emptyList());

			}
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "You have no permission for the API", null);

		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "something went wrong", null);
		}
	}
	
	@Override
	public Response<?> getAllFeesV2() {
	    try {

	        Optional<User> userDetailsOptional =
	                customizedUserDetailsService.getUserDetails();

	        if (userDetailsOptional.isPresent()
	                && userDetailsOptional.get().getRole().equals(Role.ADMIN)) {

	            List<Fees> feesList = feesRepository.findAll();

	            if (feesList.isEmpty()) {
	                return new Response<>(
	                        HttpStatus.OK.value(),
	                        "No data present",
	                        Collections.emptyList()
	                );
	            }

	            Map<Long, List<Fees>> groupedFees =
	                    feesList.stream()
	                            .collect(Collectors.groupingBy(
	                                    fee -> fee.getStudent().getId()
	                            ));

	            List<UserFeeDto> result = new ArrayList<>();

	            for (Map.Entry<Long, List<Fees>> entry : groupedFees.entrySet()) {

	                List<Fees> studentFees = entry.getValue();

	                UserDto userDto =
	                        studentFees.get(0)
	                                   .getStudent()
	                                   .convertToDto();

	                List<FeesDto> feesDtos =
	                        studentFees.stream()
	                                   .map(Fees::convertToDto)
	                                   .collect(Collectors.toList());

	                result.add(new UserFeeDto(userDto, feesDtos));
	            }

	            return new Response<>(
	                    HttpStatus.OK.value(),
	                    "OK",
	                    result
	            );
	        }

	        return new Response<>(
	                HttpStatus.BAD_REQUEST.value(),
	                "You have no permission for the API",
	                null
	        );

	    } catch (Exception e) {
	        e.printStackTrace();

	        return new Response<>(
	                HttpStatus.BAD_REQUEST.value(),
	                "Something went wrong",
	                null
	        );
	    }
	}

	@Override
	public Response<?> getAllFeesByStudent(Long id) {
		try {
			List<Fees> feesList = feesRepository.findAllByUserId(id);
			return new Response<>(HttpStatus.BAD_REQUEST.value(), feesList.isEmpty() ? "No data present" : "OK",
					!feesList.isEmpty() ? feesList.stream().map(Fees::convertToDto).collect(Collectors.toList())
							: Collections.emptyList());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(HttpStatus.BAD_REQUEST.value(), "something went wrong", null);
		}
	}

	@Override
	@Transactional
	public Response<?> createBulkFee(List<FeesDto> feesDtos) {
	    try {

	        List<Fees> feesToSave = new ArrayList<>();
	        List<String> duplicates = new ArrayList<>();

	        for (FeesDto dto : feesDtos) {

	            boolean exists = feesRepository
	                    .existsByStudentAndMonthAndYear(
	                    		new User( dto.getStudent().getId()),
	                            dto.getMonth(),
	                            dto.getYear()
	                    );

	            if (exists) {
	                duplicates.add(
	                        "StudentId: " + dto.getStudent().getId()
	                                + " Month: " + dto.getMonth()
	                                + " Year: " + dto.getYear()
	                );
	                continue;
	            }

	            Fees fee = new Fees();
	            fee.setStudent(
	                    userRepository.findById(dto.getStudent().getId())
	                            .orElseThrow(() -> new RuntimeException("Student not found"))
	            );
	            fee.setMonth(dto.getMonth());
	            fee.setYear(dto.getYear());
	            fee.setAmount(dto.getAmount());
	            fee.setPaymentDate(dto.getPaymentDate());
	            fee.setMode(dto.getMode());

	            feesToSave.add(fee);
	        }

	        if (!feesToSave.isEmpty()) {
	            feesRepository.saveAll(feesToSave);
	        }

	        if (!duplicates.isEmpty()) {
	            return new Response<>(
	                    HttpStatus.OK.value(),
	                    "Some records skipped (duplicate month/year)",
	                    duplicates
	            );
	        }

	        return new Response<>(
	                HttpStatus.OK.value(),
	                "All fees saved successfully",
	                null
	        );

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new Response<>(
	                HttpStatus.BAD_REQUEST.value(),
	                "Something went wrong",
	                null
	        );
	    }
	}

}

package com.tsms.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.tsms.entity.StudentClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tsms.cache.InMemCache;
import com.tsms.dto.LoginReponse;
import com.tsms.dto.LoginRequest;
import com.tsms.dto.OtpRequest;
import com.tsms.dto.RegisterRequest;
import com.tsms.dto.Response;
import com.tsms.dto.UserDto;
import com.tsms.entity.Address;
import com.tsms.entity.User;
import com.tsms.enums.Role;
import com.tsms.repository.AddressRepository;
import com.tsms.repository.UserRepository;
import com.tsms.security.CustomizedUserDetailsService;
import com.tsms.security.JwtService;
import com.tsms.service.UserService;

import jakarta.validation.Valid;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AddressRepository addressRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomizedUserDetailsService customizedUserDetailsService;

    @Autowired
    private InMemCache inMemCache;

    @Autowired
    private EmailService emailService;

    private boolean isAdmin() {
        Optional<User> user = customizedUserDetailsService.getUserDetails();
        return user.isPresent() && user.get().getRole().equals(Role.ADMIN);
    }

    @Override
    public Response<?> register(RegisterRequest request) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isPresent()) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "User with this email already exists", null);
            }
            Optional<User> userPhoneOptional = userRepository.findByPhoneNo(request.getPhoneNo());
            if (userPhoneOptional.isPresent()) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "User with this phone number already exists",
                        null);
            }

            User user = new User();
            user.setEmail(request.getEmail());
            user.setDob(request.getDob());
            user.setName(request.getName());
            String firstName = request.getName().trim().split("\\s+")[0];
            int year = request.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
            String pass = firstName + "@" + year;
            user.setPassword(passwordEncoder.encode(pass));
            user.setRole(Role.USER);
            user.setIsActive(true);
//			user.setSection(new StudentClass(request.getSectionId()));
            user.setGender(request.getGender());
            user.setPhoneNo(request.getPhoneNo());
            user.setFatherName(request.getFatherName());
            user.setMotherName(request.getMotherName());
            user.setStudiedFrom(request.getStudiedFrom());

            // Address
            Address presentAddress = request.getPresentAddress().convertToEntity();
            Address permanentAddress = request.getPermanentAddress().convertToEntity();
            presentAddress = addressRepository.save(presentAddress);
            permanentAddress = addressRepository.save(permanentAddress);
            logger.info("address saved ");
            user.setPermanentAddress(permanentAddress);
            user.setPresentAddress(presentAddress);
            User savedUser = userRepository.save(user);
            logger.info("User saved");

            new Thread(() -> {
                emailService.sendCredentials(savedUser.getEmail(), savedUser.getName(), savedUser.getEmail(), pass);
            }).start();
            return new Response<>(HttpStatus.OK.value(), "Registration successful", null);

        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    public Response<?> registerAdmin(RegisterRequest request) {
        try {
            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isPresent()) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "User with this email already exists", null);
            }
            Optional<User> userPhoneOptional = userRepository.findByPhoneNo(request.getPhoneNo());
            if (userPhoneOptional.isPresent()) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "User with this phone number already exists",
                        null);
            }

            User user = new User();
            user.setEmail(request.getEmail());
            user.setDob(request.getDob());
            user.setName(request.getName());
            if (request.getName() == null || request.getName().isEmpty()) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "admin-please provide name", null);
            }
            String firstName = request.getName().trim().split("\\s+")[0];
            int year = request.getDob().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
            String pass = firstName + "@" + year;
            user.setPassword(passwordEncoder.encode(pass));
            user.setRole(Role.ADMIN);
            user.setIsActive(true);
            user.setPhoneNo(request.getPhoneNo());
            User savedUser = userRepository.save(user);
            savedUser.setPassword(pass);
            logger.info("User saved");
            // thread used for speed application
            new Thread(() -> {
                emailService.sendAccountConfirmationMail(savedUser);
            }).start();
            return new Response<>(HttpStatus.OK.value(), "Registration successful", null);

        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<Object> login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        try {
            if (userOptional.isPresent() && userOptional.get().getIsActive()) {
                User user = userOptional.get();

                if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

                    String token = jwtService.generateToken(loginRequest.getEmail());
                    LoginReponse response = new LoginReponse();
                    response.setId(user.getId());
                    response.setToken(token);
                    response.setUserName(user.getName());
                    response.setEmail(user.getEmail());
                    response.setRole(user.getRole());

                    return new Response<>(HttpStatus.OK.value(), "Login Success.", response);
                }
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Invalid credentials.", null);
            }
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "invalid credential", null);

        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "something went wrong", null);
        }
    }

    @Override
    public Response<?> getAllStudent() {
        try {
            Optional<User> userDetailsOptional = customizedUserDetailsService.getUserDetails();
            if (userDetailsOptional.isPresent() && userDetailsOptional.get().getRole().equals(Role.ADMIN)) {

                List<User> users = userRepository.findAll();
                List<UserDto> userList = users.stream().filter(e -> e != null)
                        .filter(e -> e.getRole().equals(Role.USER)).map(e -> {
                            UserDto userDto = e.convertToDto();
                            Date dob = e.getDob();
                            if (dob != null) {
                                LocalDate birthDate = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                LocalDate now = LocalDate.now();
                                userDto.setAge(Period.between(birthDate, now).getYears());
                            } else {
                                userDto.setAge(null);
                            }
                            userDto.setPhoneNo(e.getPhoneNo() != null ? e.getPhoneNo() : null);
                            return userDto;
                        }).collect(Collectors.toList());
                return new Response<>(HttpStatus.OK.value(), "success.",
                        userList.isEmpty() ? Collections.emptyList() : userList);
            }
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "You have no permission for the API", null);

        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "something went wrong", null);
        }
    }

    @Override
    public Response<?> getStudentById(Long id) {
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                UserDto userDto = user.convertToDto();
                Date dob = user.getDob();
                if (dob != null) {
                    LocalDate birthDate = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate now = LocalDate.now();
                    userDto.setAge(Period.between(birthDate, now).getYears());
                } else {
                    userDto.setAge(null);
                }
                return new Response<>(HttpStatus.OK.value(), "success.", userDto);
            }
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "No user present", null);

        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "something went wrong", null);
        }
    }

    @Override
    public Response<?> verifyOtp(OtpRequest request) {
        try {
            boolean isVerified = inMemCache.verifyOtp(request.getEmail(), request.getOtp());
            if (isVerified) {
                return new Response<>(HttpStatus.OK.value(), "Email verified", null);
            }
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Invalid OTP", null);

        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "something went wrong", null);
        }
    }

    @Override
    public Response<?> sendOtp(OtpRequest request) {
        try {
            emailService.sendPasswordResetEmail(request);
            inMemCache.storeOtp(request.getEmail());
            return new Response<>(HttpStatus.OK.value(), "email sent", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "something error in sending otp	", null);
        }
    }

    @Override
    public Response<?> updateStudent(UserDto request) {
        try {
            Optional<User> userDetailsOptional = customizedUserDetailsService.getUserDetails();

            if (userDetailsOptional.isEmpty()) {
                return new Response<>(
                        HttpStatus.UNAUTHORIZED.value(),
                        "User not authenticated",
                        null);
            }

            User loggedInUser = userDetailsOptional.get();

            if (!Role.ADMIN.equals(loggedInUser.getRole())) {

                if (!loggedInUser.getId().equals(request.getId())) {
                    return new Response<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "You have no access to update another user",
                            null);
                }
            }
            Optional<User> userOptional = userRepository.findById(request.getId());

            if (userOptional.isEmpty()) {
                return new Response<>(HttpStatus.NOT_FOUND.value(), "User not found", null);
            }

            User user = userOptional.get();

            // Email duplicate check
            if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(user.getEmail())) {

                Optional<User> emailUser = userRepository.findByEmail(request.getEmail());

                if (emailUser.isPresent()) {
                    return new Response<>(HttpStatus.BAD_REQUEST.value(), "User with this email already exists", null);
                }

                user.setEmail(request.getEmail());
            }

            // Basic Details
            if (request.getName() != null) {
                user.setName(request.getName());
            }

            if (request.getDob() != null) {
                user.setDob(request.getDob());
            }

            if (request.getGender() != null) {
                user.setGender(request.getGender());
            }

            if (request.getFatherName() != null) {
                user.setFatherName(request.getFatherName());
            }

            if (request.getMotherName() != null) {
                user.setMotherName(request.getMotherName());
            }

            if (request.getSectionId() != null) {
                user.setSection(new StudentClass(request.getSectionId()));
            }

            if (request.getStudiedFrom() != null) {
                user.setStudiedFrom(request.getStudiedFrom());
            }

            if (request.getPhoneNo() != null) {
                user.setPhoneNo(request.getPhoneNo());
            }

            // Present Address Update
            if (request.getPresentAddress() != null) {

                Address presentAddress = user.getPresentAddress();

                if (presentAddress == null) {
                    presentAddress = new Address();
                }

                copyAddress(request.getPresentAddress(), presentAddress);

                presentAddress = addressRepository.save(presentAddress);

                user.setPresentAddress(presentAddress);
            }

            // Permanent Address Update
            if (request.getPermanentAddress() != null) {

                Address permanentAddress = user.getPermanentAddress();

                if (permanentAddress == null) {
                    permanentAddress = new Address();
                }

                copyAddress(request.getPermanentAddress(), permanentAddress);

                permanentAddress = addressRepository.save(permanentAddress);

                user.setPermanentAddress(permanentAddress);
            }

            userRepository.save(user);

            return new Response<>(HttpStatus.OK.value(), "User updated successfully", null);

        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    @Override
    public Response<?> updateImage(UserDto request) {
        try {

            if (!isAdmin()) {
                return new Response<>(
                        HttpStatus.BAD_REQUEST.value(),
                        "You have no access to update another user",
                        null);
            }

            Optional<User> userOptional = userRepository.findById(request.getId());
            if (userOptional.isEmpty()) {
                return new Response<>(HttpStatus.NOT_FOUND.value(), "User not found", null);
            }

            User user = userOptional.get();

            if (request.getImage() == null || request.getImage().isBlank()) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Image URL is required", null);
            }
            user.setImage(request.getImage());

            userRepository.save(user);
            return new Response<>(HttpStatus.OK.value(), "Image updated successfully", null);

        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(HttpStatus.BAD_REQUEST.value(), "Something went wrong", null);
        }
    }

    private void copyAddress(Address source, Address target) {
        target.setCity(source.getCity());
        target.setState(source.getState());
        target.setStreet(source.getStreet());
        target.setPincode(source.getPincode());

    }

    @Override
    public Response<?> updatePassword(LoginRequest request) {
        try {

            if (request.getEmail() == null || request.getEmail().isBlank()) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(),
                        "Email is required", null);
            }

            if (request.getOtp() == null || request.getOtp().isBlank()) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(),
                        "OTP is required", null);
            }

            if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(),
                        "New Password is required", null);
            }

            Optional<User> userOptional =
                    userRepository.findByEmail(request.getEmail());

            if (userOptional.isEmpty()) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(),
                        "User with this email not exists", null);
            }

            User user = userOptional.get();

            // OTP check
            if(user.getOtp() == null ) {
                return new Response<>(HttpStatus.BAD_REQUEST.value(), "Please generate OTP  ", null);
            }
            if (!user.getOtp().equals(request.getOtp())) {

                return new Response<>(HttpStatus.BAD_REQUEST.value(),
                        "Invalid OTP", null);
            }

            // Expiry check
            if (user.getOtpExpiry() == null ||
                    user.getOtpExpiry().before(new Date())) {

                return new Response<>(HttpStatus.BAD_REQUEST.value(),
                        "OTP expired", null);
            }

            // Update password
            user.setPassword(
                    passwordEncoder.encode(request.getNewPassword()));

            // Clear OTP after successful use
            user.setOtp(null);
            user.setOtpExpiry(null);

            userRepository.save(user);

            return new Response<>(HttpStatus.OK.value(),
                    "Password updated successfully", null);

        } catch (Exception e) {
            e.printStackTrace();

            return new Response<>(HttpStatus.BAD_REQUEST.value(),
                    "Something went wrong", null);
        }
    }

    @Override
    public Response<?> generateOtp(LoginRequest request) {

        try {
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                return new Response<>(400, "Email is required", null);
            }

            Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

            if (userOptional.isEmpty()) {
                return new Response<>(404, "User not found", null);
            }

            User user = userOptional.get();
            // 6 digit OTP
            String otp = String.valueOf(
                    ThreadLocalRandom.current().nextInt(100000, 1000000)
            );

            user.setOtp(otp);

            user.setOtpExpiry(
                    Date.from(
                            LocalDateTime.now()
                                    .plusMinutes(5)
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant()
                    )
            );

            userRepository.save(user);

            //send mail
            new Thread(
                    () -> emailService.sendOtp(user.getEmail(), otp))
                    .start();

            return new Response<>(200, "OTP generated successfully", null);


        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(400, "Something went wrong", null);
        }
    }

}

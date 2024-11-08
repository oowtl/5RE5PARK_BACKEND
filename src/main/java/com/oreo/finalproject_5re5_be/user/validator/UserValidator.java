package com.oreo.finalproject_5re5_be.user.validator;

import com.oreo.finalproject_5re5_be.user.dto.request.UserRegisterRequest;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@Component
public class UserValidator implements Validator {

    // 아이디 유효성 검증: 6~20자, 영문 및 숫자만 허용
    private static final Pattern ID_PATTERN = Pattern.compile("^[a-zA-Z0-9]{6,20}$");

    // 비밀번호 유효성 검증: 8~20자, 특수문자 포함, 동일 문자 4회 이상 연속 불가
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?!.*(.)\\1{3})(?=.*[!@#$%^&*()_+=-])[A-Za-z\\d!@#$%^&*()_+=-]{8,20}$");

    // 이름 유효성 검증: 특수문자, 숫자 불가
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z가-힣]{1,20}$");

    // 생년월일 유효성 검증: 'YYYY-MM-DD' 형식
    private static final Pattern BIRTHDATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    // 이메일 유효성 검증: 유효한 이메일 형식
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$");

    // 휴대전화 번호 유효성 검증: 숫자만 입력, 11자
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{11}$");


    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegisterRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // 타입 변환
        var userRegisterRequest = (UserRegisterRequest) target;

        // 빈 값 체크
        checkBlank(errors);

        // 필드 유효성 검증
        checkValidFields(userRegisterRequest, errors);
    }

    private void checkBlank(Errors errors) {
        // 필수값 : id, email, name, phon, normAddr, birthDate
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "required", "아이디를 입력해주세요.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "required", "이메일을 입력해주세요.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "required", "비밀번호를 입력해주세요.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required", "이름을 입력해주세요.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "normAddr", "required", "주소를 입력해주세요.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birthDate", "required", "생년월일을 입력해주세요.");
    }

    private void checkValidFields(UserRegisterRequest userRegisterRequest, Errors errors) {
        // 아이디 유효성 검증
        if (!isValidId(userRegisterRequest.getId())) {
            errors.rejectValue("id", "invalidId", "아이디는 6~20자의 영문 및 숫자만 허용됩니다.");
        }
        // 비밀번호 유효성 검증
        if (!isValidPassword(userRegisterRequest.getPassword())) {
            errors.rejectValue("password", "invalidPassword", "비밀번호는 8~29자의 특수문자를 포함해야하며, 동일 문자 4회 이상 연속 불가합니다.");
        }

        // 이름 유효성 검증
        if (!isValidName(userRegisterRequest.getName())) {
            errors.rejectValue("name", "invalidName", "이름은 특수문자, 숫자를 포함할 수 없습니다.");
        }

        // 생년월일 유효성 검증
        if (!isValidBirthDate(userRegisterRequest.getBirthDate())) {
            errors.rejectValue("birthDate", "invalidBirthDate", "생년월일은 'YYYY-MM-DD' 형식으로 입력해주세요.");
        }

        // 이메일 유효성 검증
        if (!isValidEmail(userRegisterRequest.getEmail())) {
            errors.rejectValue("email", "invalidEmail", "유효한 이메일 형식이 아닙니다.");
        }
    }

    private boolean isValidId(String userId) {
        return userId != null && ID_PATTERN.matcher(userId).matches();
    }

    private boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    private boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    private boolean isValidBirthDate(String birthDate) {
        return birthDate != null && BIRTHDATE_PATTERN.matcher(birthDate).matches();
    }

    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        return phone != null  && PHONE_PATTERN.matcher(phone).matches();
    }
}





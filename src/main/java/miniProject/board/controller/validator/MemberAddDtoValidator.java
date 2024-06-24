package miniProject.board.controller.validator;

import lombok.extern.slf4j.Slf4j;
import miniProject.board.dto.MemberDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class MemberAddDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return MemberDto.Create.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        MemberDto.Create memberAddDto = (MemberDto.Create) target;

        if (!memberAddDto.getPassword().equals(memberAddDto.getConfirmPassword())) {
            errors.rejectValue("confirmPassword","passwordMismatch",
                    "확인 비밀 번호가 일치하지 않습니다.");
        }
    }
}
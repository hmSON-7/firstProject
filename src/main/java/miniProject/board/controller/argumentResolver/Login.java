package miniProject.board.controller.argumentResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * LoginMemberArgumentResolver에서 사용하는 Annotaion
 * 메서드 파라미터에 해당 Annotation이 붙어 있으면서 클래스 타입이 일치하는 경우
 * LoginMemberArgumentResolver가 실행되어 파라미터에 값을 주입합니다.
 * </pre>
 *
 * @see LoginMemberArgumentResolver
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {
}
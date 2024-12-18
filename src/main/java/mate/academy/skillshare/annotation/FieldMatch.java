package mate.academy.skillshare.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Constraint(validatedBy = {FieldMatch.Validator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FieldMatch {
    String message() default "Fields do not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String firstField();

    String secondField();

    class Validator implements ConstraintValidator<FieldMatch, Object> {
        private String firstField;
        private String secondField;
        private String message;

        @Override
        public void initialize(FieldMatch constraintAnnotation) {
            this.firstField = constraintAnnotation.firstField();
            this.secondField = constraintAnnotation.secondField();
            this.message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            try {
                final Object firstFieldValue = getProperty(value, firstField);
                final Object secondFieldValue = getProperty(value, secondField);

                boolean valid = firstFieldValue != null && firstFieldValue.equals(secondFieldValue);
                if (!valid) {
                    context.buildConstraintViolationWithTemplate(message)
                            .addPropertyNode(secondField)
                            .addConstraintViolation()
                            .disableDefaultConstraintViolation();
                }
                return valid;
            } catch (Exception e) {
                throw new RuntimeException("Can't compare passwords", e);
            }
        }

        private Object getProperty(Object object, String fieldName) throws Exception {
            Method method = object.getClass().getMethod(fieldName);
            return method.invoke(object);
        }
    }
}

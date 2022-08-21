package validation;

import dto.NewUserDTO;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidationService {

    public <T> String validateAndGetErrorMessage(T object) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(object);

        for (ConstraintViolation<T> violation : violations) {
            return violation.getMessage();
        }
        return null;
    }
}

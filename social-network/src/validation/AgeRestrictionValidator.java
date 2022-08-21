package validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class AgeRestrictionValidator implements ConstraintValidator<AgeRestriction, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date == null)
            return false;
        Period period = Period.between(date, LocalDate.now());
        int ages = period.getYears();
        return ages >= 15;
    }
}

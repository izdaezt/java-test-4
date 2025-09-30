package com.namnguyen.javatest4.web.rest.vm;

import com.namnguyen.javatest4.service.dto.AdminUserDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * View Model extending the AdminUserDTO, which is meant to be used in the user management UI.
 */
public class GuessVM {

    public static final int NUMBER_MIN = 1;

    public static final int NUMBER_MAX = 5;

    @Min(NUMBER_MIN)
    @Max(NUMBER_MAX)
    private Integer guessNumber;

    public GuessVM() {
        // Empty constructor needed for Jackson.
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }

    public Integer getGuessNumber() {
        return guessNumber;
    }

    public void setGuessNumber(Integer guessNumber) {
        this.guessNumber = guessNumber;
    }
}

package com.namnguyen.javatest4.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO representing a user, with only the public attributes.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GuessDTO implements Serializable {

    private Boolean isCorrect;

    private Integer guessNumber;

    private Integer serverNumber;

    private String errorMessage;

    private Integer currentTurn;

    private Integer currentScore;

    public GuessDTO() {
        // Empty constructor needed for Jackson.
    }

    public GuessDTO(String error, Integer score, Integer turn) {
        this.errorMessage = error;
        this.currentTurn = turn;
        this.currentScore = score;
        // Empty constructor needed for Jackson.
    }

    public GuessDTO(Integer guestNumber, Integer serverNumber, Integer score, Integer turn) {
        this.guessNumber = guestNumber;
        this.serverNumber = serverNumber;
        this.isCorrect = guestNumber.equals(serverNumber);
        this.currentTurn = turn;
        this.currentScore = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GuessDTO userDTO = (GuessDTO) o;
        if (userDTO.getGuessNumber() == null || getGuessNumber() == null) {
            return false;
        }

        return Objects.equals(getGuessNumber(), userDTO.getGuessNumber()) && Objects.equals(getServerNumber(), userDTO.getServerNumber());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameDTO{" +
            "guessNumber='" + guessNumber + '\'' +
            ", serverNumber='" + serverNumber + '\'' +
            ", isCorrect='" + isCorrect + '\'' +
            "}";
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public Integer getGuessNumber() {
        return guessNumber;
    }

    public void setGuessNumber(Integer guessNumber) {
        this.guessNumber = guessNumber;
    }

    public Integer getServerNumber() {
        return serverNumber;
    }

    public void setServerNumber(Integer serverNumber) {
        this.serverNumber = serverNumber;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(Integer currentTurn) {
        this.currentTurn = currentTurn;
    }

    public Integer getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(Integer currentScore) {
        this.currentScore = currentScore;
    }
}

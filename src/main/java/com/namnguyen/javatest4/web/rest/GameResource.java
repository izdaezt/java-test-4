package com.namnguyen.javatest4.web.rest;

import com.namnguyen.javatest4.service.GameService;
import com.namnguyen.javatest4.service.dto.GuessDTO;
import com.namnguyen.javatest4.web.rest.vm.GuessVM;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class GameResource {

    private static class GameResourceException extends RuntimeException {

        private GameResourceException(String message) {
            super(message);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(GameResource.class);

    private final GameService gameService;

    public GameResource(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/guess")
    public GuessDTO registerAccount(@Valid @RequestBody GuessVM guessVM) {
        return gameService.GuessTheNumber(guessVM.getGuessNumber());
    }
}

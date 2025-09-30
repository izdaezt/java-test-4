package com.namnguyen.javatest4.service;

import com.namnguyen.javatest4.domain.AppConfig;
import com.namnguyen.javatest4.domain.User;
import com.namnguyen.javatest4.repository.AppConfigRepository;
import com.namnguyen.javatest4.repository.UserRepository;
import com.namnguyen.javatest4.security.SecurityUtils;
import com.namnguyen.javatest4.service.dto.GuessDTO;
import org.glassfish.jaxb.runtime.v2.runtime.reflect.opt.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class GameService {

    private static final Logger LOG = LoggerFactory.getLogger(GameService.class);

    private final UserRepository userRepository;
    private final AppConfigRepository appConfigRepository;

    public GameService(UserRepository userRepository, AppConfigRepository appConfigRepository) {
        this.userRepository = userRepository;
        this.appConfigRepository = appConfigRepository;
    }

    public GuessDTO GuessTheNumber(Integer number) {
        // get login user
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalStateException("No current user found"));

        User user = userRepository.findOneByLogin(login).orElseThrow(() -> new IllegalStateException("User not found"));
        var currentScore = user.getScore();
        var currentTurns = user.getTurns();
        if (currentTurns <= 0) {
            return new GuessDTO("Your turn is over", currentScore, currentTurns);
        }

        String winningPercentageKey = "winning_percentage";
        String winningPercentageValue = "5"; // value default

        AppConfig appConfig = appConfigRepository.getAppConfigByKey(winningPercentageKey);
        if (appConfig != null) {
            winningPercentageValue = appConfig.getValue();
        }
        int winningPercentage = Integer.parseInt(winningPercentageValue);
        boolean isLuckyPercent = isLuckyPercent(winningPercentage);
        GuessDTO dto = null;
        currentTurns--;
        user.setTurns(currentTurns);
        if (isLuckyPercent) {
            currentScore++;
            user.setScore(currentScore);
            dto = new GuessDTO(number, number, currentScore, currentTurns);
        } else {
            dto = new GuessDTO(number, getRandomInt(5), currentScore, currentTurns);
        }
        userRepository.save(user);
        return dto;
    }

    public boolean isLuckyPercent(double percent) {
        if (percent <= 0) return false;
        if (percent >= 100) return true;
        return Math.random() * 100 < percent;
    }

    public int getRandomInt(int max) {
        if (max <= 0) {
            throw new IllegalArgumentException("max must be greater than 0");
        }
        return (int) (Math.random() * max) + 1;
    }
}

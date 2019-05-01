package br.inpe.forum.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.inpe.forum.api.service.GamificationService;
import br.inpe.forum.gamification.GamificationSetup;
import net.sf.esfinge.gamification.achievement.Achievement;
import net.sf.esfinge.gamification.user.UserStorage;

@RestController
@RequestMapping(path = "gamification", produces = "application/json")
public class GamificationController {

	@Autowired
	private GamificationService gamificationService;

	@GetMapping(path = "{username}")
	public ResponseEntity<?> profileInfo(@PathVariable String username) {
		UserStorage.setUserID(username);
		GamificationService service = GamificationSetup.configureGamification(gamificationService);
		Map<String, Achievement> achievementInfo = service.achievementInfo(username);
		return new ResponseEntity<Map<String, Achievement>>(achievementInfo, HttpStatus.OK);
	}
}

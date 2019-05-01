package br.inpe.forum.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import br.inpe.forum.api.service.GamificationService;
import net.sf.esfinge.gamification.achievement.Achievement;
import net.sf.esfinge.gamification.achievement.Point;
import net.sf.esfinge.gamification.mechanics.Game;
import net.sf.esfinge.gamification.proxy.GameInvoker;

@Service
public class GamificationServiceImpl implements GamificationService {

	@Override
	public Map<String, Achievement> achievementInfo(String userId) {

		Game game = GameInvoker.getInstance().getGame();
		return game.getAchievements(userId);
	}

	@Override
	public Map<String, Achievement> rankingInfo() {
		
		Map<String, Achievement> ranking = GameInvoker.getInstance().getGame().getAllAchievements(Point.class);
		List<Map.Entry<String, Achievement>> entries = new ArrayList<Map.Entry<String, Achievement>>(
				ranking.entrySet());
		
		Collections.sort(entries, new Comparator<Map.Entry<String, Achievement>>() {
			public int compare(Map.Entry<String, Achievement> a, Map.Entry<String, Achievement> b) {
				return ((Point) a).getQuantity().compareTo(((Point) b).getQuantity());
			}
		});
		
		Map<String, Achievement> sortedMap = new LinkedHashMap<String, Achievement>();
		for (Map.Entry<String, Achievement> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		
		return sortedMap;
	}

}

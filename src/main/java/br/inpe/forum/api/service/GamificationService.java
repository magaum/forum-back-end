package br.inpe.forum.api.service;

import java.util.Map;

import net.sf.esfinge.gamification.achievement.Achievement;

public interface GamificationService {

	Map<String, Achievement> achievementInfo(String userId);
	
	Map<String, Achievement> rankingInfo();
	
}

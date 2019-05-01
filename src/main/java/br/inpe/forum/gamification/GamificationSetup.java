package br.inpe.forum.gamification;

import java.util.Objects;

import org.esfinge.guardian.context.AuthorizationContext;

import br.inpe.forum.model.MongoFactory;
import net.sf.esfinge.gamification.mechanics.Game;
import net.sf.esfinge.gamification.mechanics.GameMongoStorage;
import net.sf.esfinge.gamification.proxy.GameInvoker;
import net.sf.esfinge.gamification.proxy.GameProxy;

/**
 * Gamification settings
 * 
 * @author weslei
 *
 */
public class GamificationSetup {

	private static GamificationSetup gameSetup;

	private GamificationSetup() {
		Game game = new GameMongoStorage(MongoFactory.createMongoDatabase());
		GameInvoker.getInstance().setGame(game);
	}

	private static GamificationSetup configure() {
		if (Objects.isNull(gameSetup)) {
			gameSetup = new GamificationSetup();
		}
		return gameSetup;
	}
	
	public static <T> T configureGamification(T configurationObject) {
		configure();
		return GameProxy.createProxy(configurationObject);
	}

}

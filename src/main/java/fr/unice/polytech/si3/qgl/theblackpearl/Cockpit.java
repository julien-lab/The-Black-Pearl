package fr.unice.polytech.si3.qgl.theblackpearl;

import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.regatta.cockpit.ICockpit;
import fr.unice.polytech.si3.qgl.theblackpearl.actions.*;
import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Calculator;
import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Captain;
import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Sailor;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.InitGame;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.NextRound;
import fr.unice.polytech.si3.qgl.theblackpearl.goal.RegattaGoal;
import fr.unice.polytech.si3.qgl.theblackpearl.referee.Referee;

public class Cockpit implements ICockpit {
	private InitGame parsedInitGame;
	private NextRound parsedNextRound;
	private ObjectMapper objectMapper;
	private List<Action> actionsNextRound;
	private List<String> logs;
	private Referee ref;


	public Cockpit(){
		objectMapper = new ObjectMapper();
		logs = new ArrayList<>();
	}

	public void initGame(String game) {
		try {
			parsedInitGame = objectMapper.readValue(game, InitGame.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public String nextRound(String round) { //reinitialiser toutes les actions à faire des marins
		try {
			parsedNextRound = objectMapper.readValue(round, NextRound.class);
			parsedInitGame.setShip(parsedNextRound.getShip());
			logs.add(parsedInitGame.getShip().getPosition().toString());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		InitGame roundBeginInitGame = parsedInitGame.clone();

		resetSailorsNewRound();
		createNewRoundLog();

		Captain captain = new Captain(parsedInitGame, parsedNextRound.getWind());

		try {
			actionsNextRound = captain.captainOrder();
		} catch (Exception e) {
			e.printStackTrace();
		}
		sailorsTasks(Objects.requireNonNull(actionsNextRound));
		StringBuilder roundJSON=creationJson(Objects.requireNonNull(actionsNextRound));
		StringBuilder saveRoundJSON = roundJSON;
		roundJSON = shipConfigurationCorrection(roundJSON,saveRoundJSON,roundBeginInitGame);

		return roundJSON.toString();
	}

	private StringBuilder shipConfigurationCorrection(StringBuilder roundJSON, StringBuilder saveRoundJSON, InitGame roundBeginInitGame){
		while(true){
			InitGame initGameClone = roundBeginInitGame.clone();
			ref = new Referee(initGameClone, parsedNextRound.clone());
			Calculator c = new Calculator();
			try {
				if (!ref.startRound(roundJSON.toString())){
					RegattaGoal regatta = (RegattaGoal) parsedInitGame.getGoal();
					if(ref.getGoThroughCheckpoint() && !c.shipIsInsideCheckpoint(initGameClone.getShip(),regatta.getCheckpoints().get(0))){
						roundJSON = slowJSonModification();
					}else{
						break;
					}
				}
			} catch (Exception e) {
				roundJSON = saveRoundJSON;
				break;
			}
		}
		return roundJSON;
	}

	public StringBuilder slowJSonModification() {
		StringBuilder newRoundJson;
		Action actionOARLeft =null;
		Action actionOARRight = null;

		for(Action a : actionsNextRound){
			if(a instanceof OAR){
				Sailor sailorOAR = parsedInitGame.getSailorById(a.getSailorId());
				if(actionOARLeft==null && sailorOAR.getY() == parsedInitGame.getShip().getDeck().getWidth()-1){
					actionOARLeft = a;
				}
				if(actionOARRight==null && sailorOAR.getY() == 0){
					actionOARRight = a;
				}
			}
		}
		if(actionOARRight!=null && actionOARLeft!=null){
			actionsNextRound.remove(actionOARRight);
			actionsNextRound.remove(actionOARLeft);
			newRoundJson = creationJson(actionsNextRound);
		}else{
			newRoundJson = null;
		}
		return newRoundJson;
	}


	public void resetSailorsNewRound(){
		for (Sailor sailor : parsedInitGame.getSailors()) {
			sailor.newRoundSailorReset();
		}
	}

	public void createNewRoundLog(){
		StringBuilder log = new StringBuilder();
		for (Sailor sailor : parsedInitGame.getSailors()) {
			log.append(sailor.toString());
		}
		logs.add(log.toString());
	}

	public void sailorsTasks(List<Action> actionsNextRound){
		for(Sailor m : parsedInitGame.getSailors()){
			if (!m.isAvailable()) {
				switch (m.getActionToDo()) {
					case "Ramer":
						actionsNextRound.add(new OAR(m.getId()));
						break;
					case "tournerGouvernail":
						actionsNextRound.add(new TURN(m.getId(), parsedInitGame.getShip().getGouvernail().getAngleAchieved()));
						break;
					case "HisserVoile":
						actionsNextRound.add(new LIFT_SAIL(m.getId()));
						break;
					case "BaisserLaVoile":
						actionsNextRound.add(new LOWER_SAIL(m.getId()));
						break;
				}
			}
		}
	}

	public StringBuilder creationJson(List<Action> actionsNextRound){
		StringBuilder roundJSON= new StringBuilder("[");
		try {
			for(int i=0; i<actionsNextRound.size(); i++){
				if (actionsNextRound.get(i) instanceof TURN) roundJSON.append(actionsNextRound.get(i).toString());
				else roundJSON.append(objectMapper.writeValueAsString(actionsNextRound.get(i)));
				if(i!=actionsNextRound.size()-1){
					roundJSON.append(",");
				}
			}
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		roundJSON.append("]");

		return roundJSON;
	}

	@Override
	public List<String> getLogs() {
		return logs;
	}

	public InitGame getParsedInitGame() {
		return parsedInitGame;
	}

	public void setActionsNextRound(List<Action> actionsNextRound) {
		this.actionsNextRound = actionsNextRound;
	}
}

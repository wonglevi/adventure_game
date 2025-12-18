package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class HelloController {

    @FXML private GridPane grid;

    @FXML private Label playerHpLabel;
    @FXML private Label playerStrengthLabel;
    @FXML private Label playerDexterityLabel;
    @FXML private Label playerIntelligenceLabel;
    @FXML private Label goldLabel;

    @FXML private Label roomLabel;

    @FXML private Label npcHpLabel;
    @FXML private Label npcStrengthLabel;
    @FXML private Label npcDexterityLabel;
    @FXML private Label npcIntelligenceLabel;

    @FXML private Label gameCommentsLabel;

    @FXML private Button upButton;
    @FXML private Button downButton;
    @FXML private Button leftButton;
    @FXML private Button rightButton;

    @FXML private Button fightButton;
    @FXML private Button runButton;
    @FXML private Button searchButton;
    @FXML private Button sleepButton;

    private Game game;

    @FXML
    public void initialize() {
        game = new Game(10);
        updateAll();
        setComment("Game started.");
    }

    @FXML
    private void upButtonClick(ActionEvent e) {
        handleMove(game.moveUp());
    }

    @FXML
    private void downButtonClick(ActionEvent e) {
        handleMove(game.moveDown());
    }

    @FXML
    private void leftButtonClick(ActionEvent e) {
        handleMove(game.moveLeft());
    }

    @FXML
    private void rightButtonClick(ActionEvent e) {
        handleMove(game.moveRight());
    }

    @FXML
    private void fightButtonClick(ActionEvent e) {
        Game.FightResult result = game.fight();
        setComment(makeFightMessage(result));
        updateAll();
    }

    @FXML
    private void runButtonClick(ActionEvent e) {
        Game.RunResult result = game.runAway();
        setComment(makeRunMessage(result));
        updateAll();
    }

    @FXML
    private void searchButtonClick(ActionEvent e) {
        Game.SearchResult result = game.search();
        setComment(makeSearchMessage(result));
        updateAll();
    }

    @FXML
    private void sleepButtonClick(ActionEvent e) {
        Game.SleepResult result = game.sleep();
        setComment(makeSleepMessage(result));
        updateAll();
    }

    private void handleMove(Game.MoveResult moveResult) {
        if (game.getCurrentRoom().getNpc() != null) {
            setComment("A monster blocked you!");
            updateAll();
            return;
        }

        String message;
        if (moveResult == Game.MoveResult.SUCCESS) {
            message = "You moved.";
        } else if (moveResult == Game.MoveResult.GAME_OVER) {
            message = "Game is over.";
        } else if (moveResult == Game.MoveResult.OUT_OF_BOUNDS) {
            message = "Can't go there (edge).";
        } else if (moveResult == Game.MoveResult.BLOCKED) {
            message = "That room is blocked.";
        } else {
            message = "Move failed.";
        }

        setComment(message);
        updateAll();
    }

    private String makeFightMessage(Game.FightResult r) {
        if (r == Game.FightResult.GAME_OVER) return "Game over.";
        if (r == Game.FightResult.NO_MONSTER) return "No monster here.";

        boolean playerHit  = game.wasLastPlayerHit();
        boolean monsterHit = game.wasLastMonsterHit();
        int playerDmg      = game.getLastPlayerDamage();
        int monsterDmg     = game.getLastMonsterDamage();

        if (r == Game.FightResult.MONSTER_DEAD) {
            if (playerHit) {
                return "You hit for " + playerDmg + " and killed the monster!";
            } else {
                return "Monster collapsed.";
            }
        }

        if (r == Game.FightResult.PLAYER_DEAD) {
            if (monsterHit) {
                return "You were struck for " + monsterDmg + " and died. Game Over.";
            } else {
                return "You died.";
            }
        }

        StringBuilder sb = new StringBuilder();
        if (playerHit) {
            sb.append("You hit (").append(playerDmg).append("). ");
        } else {
            sb.append("You miss. ");
        }
        if (monsterHit) {
            sb.append("Monster hits you (").append(monsterDmg).append(").");
        } else {
            sb.append("Monster misses.");
        }
        return sb.toString();
    }

    private String makeSearchMessage(Game.SearchResult r) {
        if (r == Game.SearchResult.GAME_OVER) return "Game over.";
        if (r == Game.SearchResult.MONSTER_PRESENT) return "Can't search now.";
        if (r == Game.SearchResult.NO_GOLD) return "No gold here.";
        if (r == Game.SearchResult.FOUND_GOLD) {
            return "You found gold. Total: " + game.getPlayer().getGold();
        }
        if (r == Game.SearchResult.NO_GOLD_FOUND) return "You didn't find anything.";
        return "";
    }

    private String makeRunMessage(Game.RunResult r) {
        if (r == Game.RunResult.GAME_OVER) return "Game over.";
        if (r == Game.RunResult.NO_MONSTER) return "No monster.";
        if (r == Game.RunResult.ESCAPED) return "You escaped.";
        if (r == Game.RunResult.PLAYER_DEAD) return "You died fleeing. Game Over.";
        if (r == Game.RunResult.MONSTER_HIT) return "Monster hit you while you ran!";
        return "";
    }

    private String makeSleepMessage(Game.SleepResult r) {
        if (r == Game.SleepResult.GAME_OVER) return "Game over.";
        if (r == Game.SleepResult.MONSTER_PRESENT) return "Monster here! Can't sleep.";
        if (r == Game.SleepResult.SAFE) return "ZZZZ....You slept and healed.";
        if (r == Game.SleepResult.FOUND_MONSTER) return "Monster ambushed you in sleep!";
        if (r == Game.SleepResult.PLAYER_DEAD) return "Killed in your sleep. Game Over.";
        return "";
    }

    private void updateAll() {
        updatePlayerStats();
        updateNpcStats();
        updateRoomInfo();
        drawMap();
        updateButtons();
        if (game.isGameOver()) {
            setComment("Game Over");
            disableAll();
        }
    }

    private void updatePlayerStats() {
        Player p = game.getPlayer();
        playerHpLabel.setText("" + p.getHp());
        playerStrengthLabel.setText("" + p.getStrength());
        playerDexterityLabel.setText("" + p.getDexterity());
        playerIntelligenceLabel.setText("" + p.getIntelligence());
        goldLabel.setText("" + p.getGold());
    }

    private void updateNpcStats() {
        Room r = game.getCurrentRoom();
        NPC n = r.getNpc();
        if (n == null) {
            npcHpLabel.setText("-");
            npcStrengthLabel.setText("-");
            npcDexterityLabel.setText("-");
            npcIntelligenceLabel.setText("-");
        } else {
            npcHpLabel.setText("" + n.getHp());
            npcStrengthLabel.setText("" + n.getStrength());
            npcDexterityLabel.setText("" + n.getDexterity());
            npcIntelligenceLabel.setText("" + n.getIntelligence());
        }
    }

    private void updateRoomInfo() {
        roomLabel.setText("(" + game.getPlayerRow() + "," + game.getPlayerCol() + ")");
    }

    private void updateButtons() {
        boolean over = game.isGameOver();
        boolean monster = game.getCurrentRoom().getNpc() != null;

        upButton.setDisable(monster || over);
        downButton.setDisable(monster || over);
        leftButton.setDisable(monster || over);
        rightButton.setDisable(monster || over);

        fightButton.setDisable(!monster || over);
        runButton.setDisable(!monster || over);

        searchButton.setDisable(monster || over);
        sleepButton.setDisable(monster || over);
    }

    private void disableAll() {
        upButton.setDisable(true);
        downButton.setDisable(true);
        leftButton.setDisable(true);
        rightButton.setDisable(true);
        fightButton.setDisable(true);
        runButton.setDisable(true);
        searchButton.setDisable(true);
        sleepButton.setDisable(true);
    }

    private void setComment(String s) {
        gameCommentsLabel.setText(s);
    }

    private void drawMap() {
        grid.getChildren().clear();
        int size = game.getMapSize();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                Room room = game.getRoom(r, c);
                String text = "-";
                if (room != null) {
                    boolean isPlayer = (r == game.getPlayerRow() && c == game.getPlayerCol());
                    boolean hasMonster = room.getNpc() != null;
                    if (room.isBlocked()) {
                        text = "X";
                    } else if (isPlayer && hasMonster) {
                        text = "PM";
                    } else if (isPlayer) {
                        text = "P";
                    } else if (hasMonster) {
                        text = "M";
                    } else {
                        text = "-";
                    }
                }
                Label cell = new Label(text);
                cell.setMinSize(30, 20);
                cell.setPrefSize(30, 20);
                cell.setStyle("-fx-alignment: center; -fx-font-size: 11;");
                grid.add(cell, c, r);
            }
        }
    }
}
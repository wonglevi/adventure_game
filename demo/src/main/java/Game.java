package com.example.demo;

public class Game {

    private Map map;
    private Player player;
    private int playerRow, playerCol;
    private int previousRow, previousCol;
    private boolean gameOver;

    public enum MoveResult { SUCCESS, GAME_OVER, OUT_OF_BOUNDS, BLOCKED }
    public enum SearchResult { GAME_OVER, MONSTER_PRESENT, NO_GOLD, FOUND_GOLD, NO_GOLD_FOUND }
    public enum FightResult { GAME_OVER, NO_MONSTER, PLAYER_HIT, MONSTER_HIT, BOTH_HIT, BOTH_MISS, MONSTER_DEAD, PLAYER_DEAD }
    public enum RunResult { GAME_OVER, NO_MONSTER, MONSTER_HIT, ESCAPED, PLAYER_DEAD }
    public enum SleepResult { GAME_OVER, MONSTER_PRESENT, SAFE, FOUND_MONSTER, PLAYER_DEAD }

    private boolean lastPlayerHit, lastMonsterHit;
    private int lastPlayerDamage, lastMonsterDamage;

    public Game(int size) {
        map = new Map(size);
        player = new Player();
        playerRow = 0;
        playerCol = 0;
        previousRow = 0;
        previousCol = 0;
        gameOver = false;
        lastPlayerHit = false;
        lastMonsterHit = false;
        lastPlayerDamage = 0;
        lastMonsterDamage = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public Room getCurrentRoom() {
        return map.getRoom(playerRow, playerCol);
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Room getRoom(int r, int c) {
        return map.getRoom(r, c);
    }

    public int getMapSize() {
        return map.getSize();
    }

    public boolean wasLastPlayerHit() {
        return lastPlayerHit;
    }

    public boolean wasLastMonsterHit() {
        return lastMonsterHit;
    }

    public int getLastPlayerDamage() {
        return lastPlayerDamage;
    }

    public int getLastMonsterDamage() {
        return lastMonsterDamage;
    }

    public MoveResult moveUp() {
        return moveTo(playerRow - 1, playerCol);
    }

    public MoveResult moveDown() {
        return moveTo(playerRow + 1, playerCol);
    }

    public MoveResult moveLeft() {
        return moveTo(playerRow, playerCol - 1);
    }

    public MoveResult moveRight() {
        return moveTo(playerRow, playerCol + 1);
    }

    public SearchResult search() {
        if (gameOver) {
            return SearchResult.GAME_OVER;
        }
        Room current = getCurrentRoom();
        if (current.getNpc() != null) {
            return SearchResult.MONSTER_PRESENT;
        }
        if (current.getGold() <= 0) {
            return SearchResult.NO_GOLD;
        }
        int roll = Dice.roll(20);
        if (roll < player.getIntelligence()) {
            int gold = current.getGold();
            player.setGold(player.getGold() + gold);
            current.setGold(0);
            return SearchResult.FOUND_GOLD;
        }
        return SearchResult.NO_GOLD_FOUND;
    }

    public FightResult fight() {
        if (gameOver) {
            return FightResult.GAME_OVER;
        }
        Room current = getCurrentRoom();
        NPC npc = current.getNpc();
        if (npc == null) {
            return FightResult.NO_MONSTER;
        }

        lastPlayerHit = false;
        lastMonsterHit = false;
        lastPlayerDamage = 0;
        lastMonsterDamage = 0;

        int rollPlayer = Dice.roll(20);
        if (rollPlayer >= npc.getDexterity()) {
            lastPlayerHit = true;
            lastPlayerDamage = Math.max(1, player.getStrength() / 3);
            npc.setHp(npc.getHp() - lastPlayerDamage);
        }

        if (npc.getHp() <= 0) {
            current.setNpc(null);
            return FightResult.MONSTER_DEAD;
        }

        int rollMonster = Dice.roll(20);
        if (rollMonster >= player.getDexterity()) {
            lastMonsterHit = true;
            lastMonsterDamage = Math.max(1, npc.getStrength() / 3);
            player.setHp(player.getHp() - lastMonsterDamage);
            if (player.getHp() <= 0) {
                gameOver = true;
                return FightResult.PLAYER_DEAD;
            }
        }

        if (lastPlayerHit && lastMonsterHit) {
            return FightResult.BOTH_HIT;
        }
        if (lastPlayerHit) {
            return FightResult.PLAYER_HIT;
        }
        if (lastMonsterHit) {
            return FightResult.MONSTER_HIT;
        }
        return FightResult.BOTH_MISS;
    }


    public RunResult runAway() {
        if (gameOver) {
            return RunResult.GAME_OVER;
        }
        Room current = getCurrentRoom();
        NPC npc = current.getNpc();
        if (npc == null) {
            return RunResult.NO_MONSTER;
        }
        int roll = Dice.roll(20);
        boolean monsterHits = roll < npc.getIntelligence();
        if (monsterHits) {
            int dmg = Math.max(1, npc.getStrength() / 3);
            player.setHp(player.getHp() - dmg);
            playerRow = previousRow;
            playerCol = previousCol;
            if (player.getHp() <= 0) {
                gameOver = true;
                return RunResult.PLAYER_DEAD;
            }
            return RunResult.MONSTER_HIT;
        }
        playerRow = previousRow;
        playerCol = previousCol;
        return RunResult.ESCAPED;
    }

    public SleepResult sleep() {
        if (gameOver) {
            return SleepResult.GAME_OVER;
        }
        Room current = getCurrentRoom();
        if (current.getNpc() != null) {
            return SleepResult.MONSTER_PRESENT;
        }
        player.heal();
        int roll = Dice.roll(6);
        if (roll == 1) {
            NPC newNpc = new NPC();
            current.setNpc(newNpc);
            int dmg = Math.max(1, newNpc.getStrength() / 3);
            player.setHp(player.getHp() - dmg);
            if (player.getHp() <= 0) {
                gameOver = true;
                return SleepResult.PLAYER_DEAD;
            }
            return SleepResult.FOUND_MONSTER;
        }
        return SleepResult.SAFE;
    }

    private MoveResult moveTo(int newRow, int newCol) {
        if (gameOver) {
            return MoveResult.GAME_OVER;
        }
        if (newRow < 0 || newRow >= map.getSize() || newCol < 0 || newCol >= map.getSize()) {
            return MoveResult.OUT_OF_BOUNDS;
        }
        Room target = map.getRoom(newRow, newCol);
        if (target == null || target.isBlocked()) {
            return MoveResult.BLOCKED;
        }
        previousRow = playerRow;
        previousCol = playerCol;
        playerRow = newRow;
        playerCol = newCol;

        Room current = getCurrentRoom();
        if (current.getNpc() == null && Dice.roll(2) == 1) {
            current.setNpc(new NPC());
        }
        return MoveResult.SUCCESS;
    }
}
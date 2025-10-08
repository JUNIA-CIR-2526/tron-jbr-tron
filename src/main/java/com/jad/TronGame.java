package com.jad;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class TronGame {
    private final int width;
    private final int height;
    private final char[][] grid;
    private final Set<Point> walls;

    private LightCycle player1;
    private LightCycle player2;
    private GameState state;
    private String winner;

    private static final char WALL = '#';
    private static final char EMPTY = ' ';
    private static final char PLAYER1_CHAR = '1';
    private static final char PLAYER2_CHAR = '2';

    public TronGame(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new char[height][width];
        this.walls = new HashSet<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = EMPTY;
            }
        }

        player1 = new LightCycle(width / 4, height / 2, Direction.RIGHT, PLAYER1_CHAR);
        player2 = new LightCycle(3 * width / 4, height / 2, Direction.LEFT, PLAYER2_CHAR);

        state = GameState.PLAYING;
    }

    public void start() {
        state = GameState.PLAYING;
    }

    public void update() {
        if (state != GameState.PLAYING) return;

        player1.move();
        player2.move();

        boolean player1Crashed = checkCollision(player1);
        boolean player2Crashed = checkCollision(player2);

        if (player1Crashed && player2Crashed) {
            state = GameState.GAME_OVER;
            winner = "DRAW!";
        } else if (player1Crashed) {
            state = GameState.GAME_OVER;
            winner = "PLAYER 2 WINS!";
        } else if (player2Crashed) {
            state = GameState.GAME_OVER;
            winner = "PLAYER 1 WINS!";
        } else {
            Point p1Pos = new Point(player1.getX(), player1.getY());
            Point p2Pos = new Point(player2.getX(), player2.getY());
            walls.add(p1Pos);
            walls.add(p2Pos);
            grid[p1Pos.y][p1Pos.x] = WALL;
            grid[p2Pos.y][p2Pos.x] = WALL;
        }
    }

    private boolean checkCollision(LightCycle cycle) {
        Point pos = new Point(cycle.getX(), cycle.getY());

        if (cycle.getX() < 0 || cycle.getX() >= width || cycle.getY() < 0 || cycle.getY() >= height) {
            return true;
        }

        return walls.contains(pos);
    }

    public void turnPlayer1Left() {
        player1.turnLeft();
    }

    public void turnPlayer1Right() {
        player1.turnRight();
    }

    public void turnPlayer2Left() {
        player2.turnLeft();
    }

    public void turnPlayer2Right() {
        player2.turnRight();
    }

    public String render() {
        StringBuilder sb = new StringBuilder();

        if (state == GameState.PLAYING) {
            renderGame(sb);
        } else if (state == GameState.GAME_OVER) {
            renderGameOver(sb);
        }

        return sb.toString();
    }

    private void renderGame(StringBuilder sb) {
        char[][] display = new char[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                display[y][x] = grid[y][x];
            }
        }

        display[player1.getY()][player1.getX()] = player1.getSymbol();
        display[player2.getY()][player2.getX()] = player2.getSymbol();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                sb.append(display[y][x]);
            }
            if (y < height - 1) sb.append("\n");
        }
    }

    private void renderGameOver(StringBuilder sb) {
        for (int y = 0; y < height; y++) {
            if (y == height / 2 - 1) {
                sb.append("=== GAME OVER ===");
            } else if (y == height / 2 + 1) {
                sb.append(winner);
            } else if (y == height / 2 + 3) {
                sb.append("Press ESC to exit");
            } else {
                sb.append("\n");
            }
        }
    }

    public GameState getState() {
        return state;
    }
}
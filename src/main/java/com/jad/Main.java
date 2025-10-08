package com.jad;

import com.jad.textwindow.TextWindow;
import com.jad.textwindow.TextWindowSettings;

import java.awt.*;
import java.awt.event.KeyEvent;

public enum Main {
    ;

    public static void main(String[] args) {
        int height = 20;
        int width = 2 * height;

        TextWindowSettings settings = new TextWindowSettings();
        settings.addKeyboardListener(KeyEvent.VK_ESCAPE, "exit");
        settings.addKeyboardListener(KeyEvent.VK_Q, "player1_left");
        settings.addKeyboardListener(KeyEvent.VK_D, "player1_right");
        settings.addKeyboardListener(KeyEvent.VK_LEFT, "player2_left");
        settings.addKeyboardListener(KeyEvent.VK_RIGHT, "player2_right");
        settings.setBackgroundColor(Color.BLACK);
        settings.setForegroundColor(Color.GREEN);
        settings.setScreenHeight(height);
        settings.setScreenWidth(width);
        settings.setFontSize(20);

        TextWindow textWindow = new TextWindow(settings);
        textWindow.setVisible(true);

        TronGame game = new TronGame(width, height);
        game.start();

        long lastUpdate = System.currentTimeMillis();
        int gameSpeed = 200;

        while (textWindow.isOff("exit")) {
            long currentTime = System.currentTimeMillis();

            if (game.getState() == GameState.PLAYING) {
                if (textWindow.isOn("player1_left")) {
                    game.turnPlayer1Left();
                }
                if (textWindow.isOn("player1_right")) {
                    game.turnPlayer1Right();
                }
                if (textWindow.isOn("player2_left")) {
                    game.turnPlayer2Left();
                }
                if (textWindow.isOn("player2_right")) {
                    game.turnPlayer2Right();
                }

                if (currentTime - lastUpdate >= gameSpeed) {
                    game.update();
                    lastUpdate = currentTime;
                }
            }
            textWindow.display(game.render());
        }

        textWindow.close();
    }
}
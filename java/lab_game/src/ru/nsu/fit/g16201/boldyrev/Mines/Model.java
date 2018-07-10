package ru.nsu.fit.g16201.boldyrev.Mines;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Model {
    final String TITLE_OF_PROGRAM = "Mines";
    final String SIGN_OF_FLAG = "f";
    final int BLOCK_SIZE = 30; //in pixels
    final int FIELD_SIZE = 9; //in blocks
    final int FIELD_DX = 0;
    final int FIELD_DY = 80;
    final int START_LOCATION = 250;
    final int MOUSE_BUTTON_LEFT = 1;
    final int MOUSE_BUTTON_RIGHT = 3;
    final int NUMBER_OF_MINES = 10;
    final int[] COLOR_OF_NUMBERS = {0x0000FF, 0x008000, 0xFF0000, 0x800000, 0x0};
    Cell[][] field = new Cell[FIELD_SIZE][FIELD_SIZE];
    Random random = new Random();
    int countOpenedCells;
    boolean youWon, bangMine;
    int bangX, bangY;
    int minesLeft = NUMBER_OF_MINES;

    class Cell {
        private boolean isOpen, isMine, isFlag;
        private int countBombNear = 0;

        void open(Model model) {
            isOpen = true;
            bangMine = isMine;
            if (!isMine) {
                model.incCountOpenedCells();
            }
        }

        boolean isNotOpen() {
            return !isOpen;
        }

        void inverseFlag() {
            if (!isFlag) {
                minesLeft--;
            }
            else {
                minesLeft++;
            }
            isFlag = !isFlag;
        }

        boolean isMined() {
            return isMine;
        }

        void mine() {
            isMine = true;
        }

        void setCountBomb(int count) {
            countBombNear = count;
        }

        int getCountBomb() {
            return countBombNear;
        }

        boolean getIsOpen() { return isOpen; }

        boolean getIsMine() { return isMine; }

        boolean getIsFlag() { return isFlag; }

        void resetCell() {
            isOpen = false;
            isMine = false;
            isOpen = false;
        }
    }

    void openCells(int x, int y, Model model) {
        if (x < 0 || x > FIELD_SIZE - 1 || y < 0 || y > FIELD_SIZE - 1) {
            return;
        }
        if (!field[y][x].isNotOpen()) {
            return;
        }
        if (field[y][x].isFlag) {
            return;
        }
        field[y][x].open(model);
        if (field[y][x].getCountBomb() > 0 || bangMine) { //условие bangMine - иначе некоторые клетки с бомбами будут открываться как обычные
            return;
        }
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = 0; dy < 2; dy++) {
                openCells(x + dx, y + dy, model);
            }
        }
    }

    void initField() { //инициализация игрового поля
        int x = 0;
        int y = 0;
        int countMines = 0;

        //создаём ячейки поля
        for (x = 0; x < FIELD_SIZE; x++) {
            for (y = 0; y < FIELD_SIZE; y++) {
                field[y][x] = new Cell();
            }
        }

        //рандомно расставляем мины
        while(countMines < NUMBER_OF_MINES) {
            do {
                x = random.nextInt(FIELD_SIZE);
                y = random.nextInt(FIELD_SIZE);
            } while (field[y][x].isMined());
            field[y][x].mine();
            countMines++;
        }

        //подсчёт заминированных вокруг ячейки соседей
        for (x = 0; x < FIELD_SIZE; x++) {
            for (y = 0; y < FIELD_SIZE; y++) {
                if (!field[y][x].isMined()) {
                    int count = 0;
                    for (int dx = -1; dx < 2; dx++) {
                        for (int dy = -1; dy < 2; dy++) {
                            int nX = x + dx;
                            int nY = y + dy;
                            if (nX < 0 || nY < 0 || nX > FIELD_SIZE - 1 || nY > FIELD_SIZE - 1) {
                                nX = x;
                                nY = y;
                            }
                            count += (field[nY][nX].isMined()) ? 1 : 0;
                        }
                    }
                    field[y][x].setCountBomb(count);
                }
            }
        }
    }


    public int getBLOCK_SIZE() {
        return BLOCK_SIZE;
    }

    public int getFIELD_SIZE() {
        return FIELD_SIZE;
    }

    public int getFIELD_DX() {  return FIELD_DX; }

    public int getFIELD_DY() {
        return FIELD_DY;
    }

    public int getSTART_LOCATION() {
        return START_LOCATION;
    }

    public int getMOUSE_BUTTON_LEFT() { return MOUSE_BUTTON_LEFT; }

    public int getMOUSE_BUTTON_RIGHT() {
        return MOUSE_BUTTON_RIGHT;
    }

    public int getNUMBER_OF_MINES() {
        return NUMBER_OF_MINES;
    }

    public int getCountOpenedCells() {
        return countOpenedCells;
    }

    public int getBangX() {
        return bangX;
    }

    public int getBangY() {
        return bangY;
    }

    boolean getYouWon() { return youWon; }

    boolean getBangMine() { return bangMine; }

    Cell[][] getField() { return field;}

    public void incCountOpenedCells() { countOpenedCells++; }

    public void setBangX(int i) {
        bangX = i;
    }

    public void setBangY(int i) {
        bangY = i;
    }

    public void setYouWon(boolean i) { youWon = i; }

    public void setBangMine(boolean i) { bangMine = i; }

    public int getMinesLeft() { return minesLeft; }

    public void setMinesLeft() { minesLeft = NUMBER_OF_MINES; }
}

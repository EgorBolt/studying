package ru.nsu.fit.g16201.boldyrev.Mines;

import jdk.nashorn.internal.scripts.JD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class View extends JFrame {
    Model model = new Model();
    final int BLOCK_SIZE = model.getBLOCK_SIZE(); //in pixels
    final int FIELD_SIZE = model.getFIELD_SIZE(); //in blocksreturn
    final int FIELD_DX = model.getFIELD_DX();
    final int FIELD_DY = model.getFIELD_DY();
    final int START_LOCATION = model.getSTART_LOCATION();
    final int MOUSE_BUTTON_LEFT = model.getMOUSE_BUTTON_LEFT();
    final int MOUSE_BUTTON_RIGHT = model.getMOUSE_BUTTON_RIGHT();
    final int NUMBER_OF_MINES = model.getNUMBER_OF_MINES();
    final int[] COLOR_OF_NUMBERS = {0x0000FF, 0x008000, 0xFF0000, 0x800000, 0x0};
    Model.Cell[][] field = model.getField();
    Random random = new Random();
    int countOpenedCells = model.getCountOpenedCells();
    boolean youWon = model.getYouWon();
    boolean bangMine = model.getBangMine();
    int bangX = model.getBangX();
    int bangY = model.getBangY();
    int minesLeft = model.getMinesLeft();
    JLabel winText = new JLabel();
    JLabel bombsleftText = new JLabel("Bombs left: " + model.getMinesLeft());


    View() {
        JPanel panel = new JPanel();
        this.add(panel);
        JMenuBar menu = new JMenuBar();
        JMenu jmGame = new JMenu("Game");
        JMenuItem jmiNewGame = new JMenuItem("New Game");
        //JMenuItem jmiSettings = new JMenuItem("Settings");
        JMenuItem jmiExit = new JMenuItem("Exit");
        menu.add(jmGame);
        jmGame.add(jmiNewGame);
        //jmGame.add(jmiSettings);
        jmGame.addSeparator();
        jmGame.add(jmiExit);
        jmiExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JFrame exitFrame = new JFrame();
                JPanel exitPanel = new JPanel();
                exitFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                exitFrame.setBounds(600, 300, 300, 100);
                exitFrame.setTitle("Exit");
                exitFrame.setVisible(true);
                exitFrame.add(exitPanel);
                JLabel text = new JLabel("Do you really sure you want to exit?", SwingConstants.CENTER);
                JButton yesButton = new JButton("Yes");
                JButton noButton = new JButton("No");
                exitPanel.setLayout(new FlowLayout());
                exitPanel.add(text);
                exitPanel.add(yesButton);
                exitPanel.add(noButton);
                noButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        exitFrame.dispose();
                    }
                });
                yesButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        System.exit(0);
                    }
                });
            }
        });

        setJMenuBar(menu);
        setTitle("Mines");
        setDefaultCloseOperation(EXIT_ON_CLOSE); //стандартное действие при закрытии приложения через крестик - оно закрывается
        setBounds(START_LOCATION, START_LOCATION, FIELD_SIZE * BLOCK_SIZE + FIELD_DX, FIELD_SIZE * BLOCK_SIZE + FIELD_DY); //отрисовка границ
        setResizable(false);
        TimerLabel timeLabel = new TimerLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        Canvas canvas = new Canvas();
        canvas.setBackground(Color.white);
        canvas.addMouseListener(new MouseAdapter() { //В этом теле - описание экземпляра класса MouseAdapter
            @Override
            public void mouseReleased(MouseEvent e) { //mouseReleased - клавиша мыши уже отжата
                int x = e.getX() / BLOCK_SIZE; //в какую ячейку мы попали
                int y = e.getY() / BLOCK_SIZE;
                if (e.getButton() == MOUSE_BUTTON_LEFT && !model.getBangMine() && !model.getYouWon()) {
                    if (field[y][x].isNotOpen()) {
                        model.openCells(x, y, model);
                        model.setYouWon(countOpenedCells == FIELD_SIZE * FIELD_SIZE - NUMBER_OF_MINES); //определяем, победили ли мы уже
                        if (model.getBangMine()) {
                            model.setBangX(x);
                            model.setBangY(y);
                        }
                    }
                }
                jmiNewGame.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        for (int x = 0; x < FIELD_SIZE; x++) {
                            for (int y = 0; y < FIELD_SIZE; y++) {
                                field[y][x].resetCell();
                            }
                        }
                        model.setYouWon(false);
                        model.setBangMine(false);
                        model.setMinesLeft();
                        refreshField(model);
                        canvas.remove(winText);
                        bombsleftText.setText("Bombs left: " + model.getMinesLeft());
                        timeLabel.refreshTimer();
                        model.initField();
                        canvas.repaint();
                    }
                });
                if (e.getButton() == MOUSE_BUTTON_RIGHT) {
                    if (model.getMinesLeft() > 0) {
                        field[y][x].inverseFlag();
                        bombsleftText.setText("Bombs left: " + model.getMinesLeft());
                        canvas.repaint();
                    }
                    else if (field[y][x].isMined()) {
                        field[y][x].inverseFlag();
                    }
                }
                if (model.getBangMine()) {
                    timeLabel.stopTimer();
                    winText = new JLabel("You have lost!");
                    winText.setForeground(Color.red);
                    winText.setSize(100, 300); //?!?!?
                    winText.setLocation(160, 130);
                    canvas.add(winText);

                }
                else if (model.getYouWon()) {
                    timeLabel.stopTimer();
                    winText = new JLabel("You have won!");
                    winText.setForeground(Color.green);
                    winText.setSize(100, 300); //?!?!?
                    winText.setLocation(160, 130);
                    canvas.add(winText);
                }
                refreshField(model);
                canvas.repaint();
            }
        });
        add(BorderLayout.CENTER, canvas);
        add(BorderLayout.SOUTH, timeLabel);
        setVisible(true);
        bombsleftText.setForeground(Color.MAGENTA);
        canvas.setLayout(null);
        bombsleftText.setSize(100, 300);
        bombsleftText.setLocation(5, 130);
        canvas.add(bombsleftText);
        canvas.repaint();
        model.initField();
    }

    void refreshField(Model model) {
        Model.Cell[][] field = model.getField();
        countOpenedCells = model.getCountOpenedCells();
        youWon = model.getYouWon();
        bangMine = model.getBangMine();
        bangX = model.getBangX();
        bangY = model.getBangY();
        minesLeft = model.getMinesLeft();
    }

    void paintField(Graphics g, int x, int y, Model.Cell cell) {
            g.setColor(Color.lightGray);
            g.drawRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            if (!cell.getIsOpen()) {
                if ((bangMine || youWon) && cell.getIsMine()) {
                    paintBomb(g, x, y, Color.black);
                } else {
                    g.setColor(Color.lightGray);
                    g.fill3DRect(x * BLOCK_SIZE, y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, true);
                    if (cell.getIsFlag()) {
                        paintString(g, "f", x, y, Color.red);
                    }
                }
            } else {
                if (cell.getIsMine()) {
                    paintBomb(g,  x, y, bangMine ? Color.red : Color.black);
                }
                else {
                    if (cell.getCountBomb() > 0) {
                        paintString(g, Integer.toString(cell.getCountBomb()), x, y, new Color(COLOR_OF_NUMBERS[cell.getCountBomb() - 1]));
                    }
                }
            }
        }

    void paintBomb(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillRect(x * BLOCK_SIZE + 7, y * BLOCK_SIZE + 10, 18, 10);
        g.fillRect(x * BLOCK_SIZE + 11, y * BLOCK_SIZE + 6, 10, 18);
        g.fillRect(x * BLOCK_SIZE + 9, y * BLOCK_SIZE + 8, 14, 14);
        g.setColor(Color.white);
        g.fillRect(x * BLOCK_SIZE + 11, y * BLOCK_SIZE + 10, 4, 4);
    }

    void paintString(Graphics g, String str, int x, int y, Color color) {
        g.setColor(color);
        g.setFont(new Font("", Font.BOLD, BLOCK_SIZE));
        g.drawString(str, x * BLOCK_SIZE + 8, y * BLOCK_SIZE + 26);
    }

    class Canvas extends JPanel {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            for (int x = 0; x < FIELD_SIZE; x++) {
                for (int y = 0; y < FIELD_SIZE; y++) {
                    paintField(g, x, y, field[y][x]);
                }
            }
        }
    }

    class TimerLabel extends JLabel {
        java.util.Timer timer = new Timer();
        volatile int time;

        TimerLabel() {
            timer.scheduleAtFixedRate(timerTask, 0, 1000);
        }

        TimerTask timerTask = new TimerTask() {
            //volatile int time;
            Runnable refresher = new Runnable() {
                public void run() {
                    TimerLabel.this.setText(String.format("%02d:%02d", time / 60, time % 60));
                }
            };
            public void run() {
                time++;
                SwingUtilities.invokeLater(refresher);
            }
        };

        void stopTimer() {
            timer.cancel();
        }

        void refreshTimer() {
            timer.cancel();
            TimerLabel.this.setText(String.format("%02d:%02d", 0, 0));
            timer = new Timer();
            time = -1;
            TimerTask timerTask = new TimerTask() {
                Runnable refresher = new Runnable() {
                    public void run() {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                setText(String.format("%02d:%02d", time / 60, time % 60));

                            }
                        });
                    }
                };
                public void run() {
                    time++;
                    SwingUtilities.invokeLater(refresher);
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 1000);
        }
    }

}

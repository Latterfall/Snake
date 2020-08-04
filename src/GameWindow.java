import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EventObject;
import java.util.List;

public class GameWindow extends JFrame {
    private GamePanel gamePanel;

    public GameWindow() {
        Font gameFont = new Font("Courier New", Font.PLAIN, 15);
        UIManager.put("Menu.font", gameFont);
        UIManager.put("MenuItem.font", gameFont);
        UIManager.put("OptionPane.messageFont", gameFont);

        setJMenuBar(createMenuBar());
        gamePanel = new GamePanel();
        setContentPane(gamePanel);
        pack();

        setTitle("Snake the game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenu propertiesMenu = new JMenu("Properties");
        JMenu multiplayerMenu = new JMenu("Multiplayer");
        JMenu aboutMenu = new JMenu("About");

        jMenuBar.add(gameMenu);
        jMenuBar.add(propertiesMenu);
        jMenuBar.add(multiplayerMenu);
        jMenuBar.add(aboutMenu);

        // Game

        // Game -> New game

        JMenuItem newGameMenuItem = new JMenuItem("New game");
        newGameMenuItem.addActionListener(actionEvent -> gamePanel.startGame());
        gameMenu.add(newGameMenuItem);

        // Game -> Pause game

        JMenuItem pauseGameMenuItem = new JMenuItem("Pause game");
        pauseGameMenuItem.addActionListener(actionEvent -> gamePanel.pauseGame());
        gameMenu.add(pauseGameMenuItem);

        // Game -> View top scores

        JMenuItem viewTopScoresMenuItem = new JMenuItem("View top scores");
        viewTopScoresMenuItem.addActionListener(actionEvent -> {
            JDialog scoreDialog = new JDialog();

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Player");
            tableModel.addColumn("Score");

            String workingDirectory = System.getProperty("user.dir");
            File topScoreFile = new File(workingDirectory + "/top_score.dat");
            if (topScoreFile.exists()) {
                try {
                    List<String> topScoreFileData = Files.readAllLines(topScoreFile.toPath());
                    List<UserScore> userScores = new ArrayList<>();
                    topScoreFileData.forEach(line -> {
                        String[] playerNameAndScore = line.split(":");
                        userScores.add(new UserScore(Integer.parseInt(playerNameAndScore[1]), playerNameAndScore[0]));
                    });
                    Comparator scoreComparator = (o, t1) -> {
                        UserScore score1 = (UserScore) o;
                        UserScore score2 = (UserScore) t1;
                        return Integer.compare(score2.getScore(), score1.getScore());
                    };
                    userScores.sort(scoreComparator);
                    userScores.forEach(userScore -> tableModel.addRow(new Object[] {userScore.getUserName(), userScore.getScore()}));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            JTable scoreTable = new JTable(tableModel) {
                @Override
                public boolean editCellAt(int row, int column, EventObject e) {
                    return false;
                }
            };
            scoreTable.getTableHeader().setResizingAllowed(false);
            scoreTable.getTableHeader().setReorderingAllowed(false);

            JScrollPane scorePanel = new JScrollPane();
            scorePanel.setViewportView(scoreTable);

            scoreDialog.setContentPane(scorePanel);
            scoreDialog.pack();
            scoreDialog.setBounds(new Rectangle(400, 400));
            scoreDialog.setLocationRelativeTo(null);
            scoreDialog.setResizable(false);
            scoreDialog.setVisible(true);
        });
        gameMenu.add(viewTopScoresMenuItem);

        // Game -> Exit

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        gameMenu.addSeparator();
        gameMenu.add(exitMenuItem);

        exitMenuItem.addActionListener(actionEvent -> dispose());

        // Properties

        // Properties -> Field size

        JMenu setGameFieldSizeMenu = new JMenu("Field size");
        propertiesMenu.add(setGameFieldSizeMenu);

        JMenuItem fieldSize20x20 = new JMenuItem("40x40");
        fieldSize20x20.addActionListener(actionEvent -> changeGameFieldSize(40));

        JMenuItem fieldSize30x30 = new JMenuItem("60x60");
        fieldSize30x30.addActionListener(actionEvent -> changeGameFieldSize(60));

        JMenuItem fieldSize40x40 = new JMenuItem("80x80");
        fieldSize40x40.addActionListener(actionEvent -> changeGameFieldSize(80));

        setGameFieldSizeMenu.add(fieldSize20x20);
        setGameFieldSizeMenu.add(fieldSize30x30);
        setGameFieldSizeMenu.add(fieldSize40x40);

        // Properties -> Set game speed

        JMenu setGameSpeedMenu = new JMenu("Speed");
        propertiesMenu.add(setGameSpeedMenu);

        JMenuItem slowGameSpeed = new JMenuItem("Slow");
        slowGameSpeed.addActionListener(actionEvent -> setGameSpeed(200));

        JMenuItem normalGameSpeed = new JMenuItem("Normal");
        normalGameSpeed.addActionListener(actionEvent -> setGameSpeed(100));

        JMenuItem fastGameSpeed = new JMenuItem("Fast");
        fastGameSpeed.addActionListener(actionEvent -> setGameSpeed(50));

        JMenuItem veryFastGameSpeed = new JMenuItem("Very fast");
        veryFastGameSpeed.addActionListener(actionEvent -> setGameSpeed(10));

        setGameSpeedMenu.add(slowGameSpeed);
        setGameSpeedMenu.add(normalGameSpeed);
        setGameSpeedMenu.add(fastGameSpeed);
        setGameSpeedMenu.add(veryFastGameSpeed);

        // Properties -> Theme

        JMenu setGameFieldTheme = new JMenu("Theme");
        propertiesMenu.add(setGameFieldTheme);

        JMenuItem darkTheme = new JMenuItem("Dark");
        darkTheme.addActionListener(actionEvent -> gamePanel.setGameFieldTheme(GamePanel.Theme.DARK));

        JMenuItem lightTheme = new JMenuItem("Light");
        lightTheme.addActionListener(actionEvent -> gamePanel.setGameFieldTheme(GamePanel.Theme.LIGHT));

        setGameFieldTheme.add(darkTheme);
        setGameFieldTheme.add(lightTheme);

        // Multiplayer

        // Multiplayer -> Host game

        JMenuItem hostGameMenuItem = new JMenuItem("Host game");

        JMenuItem connectToTheGameMenuItem = new JMenuItem("Connect to the game");

        multiplayerMenu.add(hostGameMenuItem);
        multiplayerMenu.add(connectToTheGameMenuItem);

        // About

        // About -> Game

        JMenuItem aboutGameMenuItem = new JMenuItem("Game");
        String gameControls =
        "<html>" +
                "Controls:" +
                "<ul>" +
                    "<li>Use arrows to control snake movement directions</li>" +
                    "<li>Press P to pause the game</li>" +
                    "<li>Press N to start new game</li>" +
                "</ul>" +
        "</html>";
        aboutGameMenuItem.addActionListener(actionEvent -> JOptionPane.showMessageDialog(this, gameControls,"About game", JOptionPane.INFORMATION_MESSAGE));

        // About -> Creators

        JMenuItem aboutCreatorsMenuItem = new JMenuItem("Creators");
        String gameCreators = "Created by Latterfall, 2020.";
        aboutCreatorsMenuItem.addActionListener(actionEvent -> JOptionPane.showMessageDialog(this, gameCreators, "About creators", JOptionPane.INFORMATION_MESSAGE));

        aboutMenu.add(aboutGameMenuItem);
        aboutMenu.add(aboutCreatorsMenuItem);

        return jMenuBar;
    }

    private void changeGameFieldSize(int size) {
        gamePanel.setNumberOfCells(size);
        pack();
        revalidate();
        setLocationRelativeTo(null);
    }

    private void setGameSpeed(int speed) {
        gamePanel.setGameSpeed(speed);
    }
}

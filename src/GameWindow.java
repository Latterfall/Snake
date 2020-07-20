import javax.swing.*;

public class GameWindow extends JFrame {
    private GamePanel gamePanel;

    public GameWindow() {
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
        // JDialog + JScrollPane + JTable?
        //viewTopScoresMenuItem.addActionListener(actionEvent -> );
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
                "Controls\n" +
                "Use arrows to control snake movement directions\n" +
                "Press P to pause the game\n" +
                "Press N to start new game";
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

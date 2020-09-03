package game.ui.dialog;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.net.InetSocketAddress;

public class MultiplayerHostDialogWindow extends JDialog {
    public MultiplayerHostDialogWindow() {
        JPanel contentPane = (JPanel) getContentPane();
        BoxLayout boxLayout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
        contentPane.setLayout(boxLayout);
        contentPane.add(setInfoPanel());
        contentPane.add(setPlayerPanel());
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        pack();
        setLocationRelativeTo(null);
        setTitle("Host multiplayer game");
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private JPanel setInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(getTitledBorder("Info"));
        infoPanel.add(new JLabel("Your IP is " + new InetSocketAddress(8546).getAddress()));

        return infoPanel;
    }

    private JPanel setPlayerPanel() {
        GridBagLayout playersLayout = new GridBagLayout();
        JPanel playersPanel = new JPanel();
        playersPanel.setLayout(playersLayout);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);

        Dimension cellSize;
        int cellHeight = 20;
        for (int i = 0; i < 5; i++) {
            gridBagConstraints.gridy = i;

            Label playerLabel = new Label("Player " + i + ":");
            cellSize = new Dimension(60, cellHeight);
            playerLabel.setPreferredSize(cellSize);

            JTextArea playerNameTextArea = new JTextArea();
            cellSize = new Dimension(90, cellHeight);
            playerNameTextArea.setPreferredSize(cellSize);
            playerNameTextArea.setEditable(false);

            JCheckBox isPlayerReadyCheckBox = new JCheckBox();
            cellSize = new Dimension(30, cellHeight);
            isPlayerReadyCheckBox.setPreferredSize(cellSize);
            isPlayerReadyCheckBox.addActionListener(actionEvent -> {
                if (isPlayerReadyCheckBox.isSelected()) {
                    playerNameTextArea.setBackground(Color.GREEN);
                } else {
                    playerNameTextArea.setBackground(Color.WHITE);
                }
            });

            playersPanel.add(playerLabel, gridBagConstraints);
            playersPanel.add(playerNameTextArea, gridBagConstraints);
            playersPanel.add(isPlayerReadyCheckBox, gridBagConstraints);
        }

        JButton startGameButton = new JButton("Start");
        cellSize = new Dimension(90, cellHeight);
        startGameButton.setPreferredSize(cellSize);
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        playersPanel.add(startGameButton, gridBagConstraints);
        playersPanel.setBorder(getTitledBorder("Players"));

        return playersPanel;
    }

    private TitledBorder getTitledBorder(String title) {
        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                border,
                title,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                this.getFont(),
                Color.BLACK);
        return titledBorder;
    }
}

package game.windows.dialog;

import javax.swing.*;
import java.awt.*;

public class MultiplayerDialogWindow extends JDialog {
    public static void main(String[] args) {
        new MultiplayerDialogWindow();
    }

    public MultiplayerDialogWindow() {
        Box verticalBox = Box.createVerticalBox();
        for (int i = 0; i < 5; i++) {
            Box horizontalBox = Box.createHorizontalBox();
            horizontalBox.add(new Label("Player " + i + ":"));
            horizontalBox.add(new JTextArea());
            horizontalBox.add(new JCheckBox());
            verticalBox.add(horizontalBox);
        }
        verticalBox.add(new JButton("Start game"));
        setContentPane(verticalBox);
        setBounds(new Rectangle(400, 400));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}

package game.windows.dialog;

import game.model.UserScore;
import game.utils.ScoreFileManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.EventObject;
import java.util.List;

public class TopScoresDialogWindow extends JDialog {
    public TopScoresDialogWindow() {
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Player");
        tableModel.addColumn("Score");

        ScoreFileManager scoreFileManager = ScoreFileManager.getInstance();
        List<UserScore> allScores = scoreFileManager.getAllScores();
        allScores.forEach(userScore -> tableModel.addRow(new Object[]{userScore.getUserName(), userScore.getScore()}));

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

        setContentPane(scorePanel);
        pack();
        setBounds(new Rectangle(400, 400));
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}

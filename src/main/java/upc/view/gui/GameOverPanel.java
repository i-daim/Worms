package upc.view.gui;

import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {

    public GameOverPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(20, 20, 30));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        add(centerPanel, BorderLayout.CENTER);

    }


}

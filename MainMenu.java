import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    private int selectedLevel = 1; // выбранный уровень по умолчанию

    public MainMenu() {
        setTitle("Tower Battle");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Панель с градиентным фоном
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 20, 40),
                        0, getHeight(), new Color(50, 30, 70));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);

        // Заголовок
        JLabel title = new JLabel(" TOWER BATTLE ");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(255, 215, 0));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);

        // Подзаголовок
        JLabel subtitle = new JLabel("Защити свою башню!");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 1;
        panel.add(subtitle, gbc);

        // Отступ
        gbc.gridy = 2;
        panel.add(Box.createVerticalStrut(20), gbc);

        // Кнопка СТАРТ
        JButton startBtn = createStyledButton(" СТАРТ ИГРЫ", new Color(46, 204, 113));
        startBtn.addActionListener(e -> {
            dispose();
            Main.startGame();
        });
        gbc.gridy = 3;
        panel.add(startBtn, gbc);

        // Кнопка ВЫБОР УРОВНЯ (с простым выбором)
        JButton levelBtn = createStyledButton("ВЫБОР УРОВНЯ", new Color(52, 152, 219));
        levelBtn.addActionListener(e -> {
            String[] levels = {" Лёгкий", " Средний", " Сложный"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Выбери сложность:",
                    "Уровни",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    levels,
                    levels[selectedLevel - 1]
            );
            if (choice >= 0) {
                selectedLevel = choice + 1;
                String levelName = choice == 0 ? "Лёгкий" : (choice == 1 ? "Средний" : "Сложный");
                JOptionPane.showMessageDialog(this, " Выбран уровень: " + levelName + "\nСложность будет применена в игре!");
            }
        });
        gbc.gridy = 4;
        panel.add(levelBtn, gbc);

        // Кнопка НАСТРОЙКИ
        JButton settingsBtn = createStyledButton("НАСТРОЙКИ", new Color(155, 89, 182));
        settingsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, " Настройки будут в следующей версии!");
        });
        gbc.gridy = 5;
        panel.add(settingsBtn, gbc);

        add(panel);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(220, 50));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        // Эффект наведения
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }
}
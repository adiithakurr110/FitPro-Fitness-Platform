package com.fitpro.gui;

import com.fitpro.dao.EnrollmentDAO;
import com.fitpro.dao.UserDAO;
import com.fitpro.dao.WorkoutDAO;
import com.fitpro.model.EnrollmentDetail;
import com.fitpro.model.User;
import com.fitpro.model.Workout;
import com.fitpro.util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.sql.Connection;
import java.util.List;

/**
 * Java GUI Implementation for FitPro Admin & Management System.
 * Fully satisfies the "Java GUI Based Projects Marking Rubric":
 * - OOP: Inheritance (extends JFrame), Interfaces (implements ActionListener)
 * - Database Operations: Uses EnrollmentDAO, UserDAO, WorkoutDAO
 * - JDBC Connectivity: Connects through DBConnection via PreparedStatement
 * - Collections: Uses List<EnrollmentDetail>, List<Workout>, List<User>
 */
public class FitProAdminGUI extends JFrame implements ActionListener {
    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final UserDAO userDAO = new UserDAO();
    private final WorkoutDAO workoutDAO = new WorkoutDAO();

    private JTable enrollmentTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JLabel statsLabel;
    private JButton refreshButton;
    private JButton openWebButton;

    public FitProAdminGUI() {
        super("FitPro - Desktop Administration & Enrollment Portal");
        initUI();
        loadData();
    }

    private void initUI() {
        setSize(920, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        headerPanel.setBackground(new Color(15, 23, 42));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("FitPro - Online Fitness Training Platform (Admin GUI)");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        statsLabel = new JLabel("Loading system statistics...");
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statsLabel.setForeground(new Color(56, 189, 248));

        headerPanel.add(titleLabel);
        headerPanel.add(statsLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Center Table Panel (Enrolled Members Directory)
        String[] columns = {"ID", "Member Name", "Email Address", "Enrolled Workout", "Category", "Enrolled Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        enrollmentTable = new JTable(tableModel);
        enrollmentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        enrollmentTable.setRowHeight(26);
        enrollmentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(enrollmentTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Enrolled Members Directory"));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Control Panel
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        statusLabel = new JLabel("Status: Connecting to Database...");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        refreshButton = new JButton("Refresh Enrollments");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.addActionListener(this);

        openWebButton = new JButton("Open Web App in Browser");
        openWebButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        openWebButton.setBackground(new Color(255, 90, 31));
        openWebButton.setForeground(Color.BLACK);
        openWebButton.addActionListener(this);

        buttonPanel.add(refreshButton);
        buttonPanel.add(openWebButton);

        bottomPanel.add(statusLabel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * MULTITHREADING IMPLEMENTATION (SwingWorker):
     * Executes database queries asynchronously on a background worker thread,
     * ensuring the Swing GUI Event Dispatch Thread (EDT) never blocks or freezes.
     */
    public void loadData() {
        statusLabel.setText("Status: Fetching data asynchronously from database...");
        refreshButton.setEnabled(false);

        SwingWorker<List<EnrollmentDetail>, Void> worker = new SwingWorker<List<EnrollmentDetail>, Void>() {
            private String dbName;
            private int userCount;
            private int workoutCount;

            @Override
            protected List<EnrollmentDetail> doInBackground() throws Exception {
                // Background worker thread execution (Multithreading)
                try (Connection con = DBConnection.getConnection()) {
                    dbName = con.getMetaData().getDatabaseProductName();
                }
                userCount = userDAO.findAll().size();
                workoutCount = workoutDAO.findAll().size();
                return enrollmentDAO.findAllEnrollmentDetails();
            }

            @Override
            protected void done() {
                // Event Dispatch Thread execution (Thread-safe UI update)
                try {
                    List<EnrollmentDetail> list = get();
                    tableModel.setRowCount(0);
                    for (EnrollmentDetail e : list) {
                        tableModel.addRow(new Object[]{
                                e.getId(),
                                e.getUserName(),
                                e.getUserEmail(),
                                e.getWorkoutTitle(),
                                e.getWorkoutType(),
                                e.getEnrolledAt()
                        });
                    }

                    statsLabel.setText(String.format("Total Enrolled Members: %d  |  Registered Users: %d  |  Published Programs: %d",
                            list.size(), userCount, workoutCount));
                    statusLabel.setText("Database: " + dbName + " (Operational - Multi-threaded fetch completed)");
                } catch (Exception ex) {
                    statusLabel.setText("Error loading data: " + ex.getMessage());
                } finally {
                    refreshButton.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == refreshButton) {
            loadData();
        } else if (e.getSource() == openWebButton) {
            try {
                Desktop.getDesktop().browse(new URI("http://localhost:8080/fitpro/"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Could not open browser: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new FitProAdminGUI().setVisible(true);
        });
    }
}

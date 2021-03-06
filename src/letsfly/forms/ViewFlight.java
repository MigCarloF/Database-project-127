package letsfly.forms;

import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import letsfly.forms.admin.Admin;

import java.awt.*;
import java.awt.event.*;


public class ViewFlight extends JFrame {
    private JScrollPane jScrollPane1;
    private JTable jTable1;
    private JButton searchButton;
    private JTextField searchField;
    private JComboBox<String> searchType;
    private String parent;

    public ViewFlight(String parent) {
        initComponents();
        String sql = "Select * From flights";
        showTable(sql);
        this.parent = parent;
    }
    private void showTable(String sql){
        
        try (Connection con = connect();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            java.util.List<String> l = new ArrayList<>();
            while (rs.next()) {
                l.add(String.valueOf(rs.getInt("flightNum")));
                l.add(rs.getString("airlineID"));
                l.add(rs.getString("destinationFrom"));
                l.add(rs.getString("destinationTo"));
                l.add(rs.getString("date"));
                l.add(rs.getString("departureTime"));
            }
            if (l.isEmpty()){
                JOptionPane.showMessageDialog(this, "No results Found");
            } else {
                int row = l.size() / 6;

                String[][] data = new String[row][6];
                for (int i = 0; i < row; i++){

                    data[i][0] = l.get(6 * i);
                    data[i][1] = l.get(6 * i + 1);
                    data[i][2] = l.get(6 * i + 2);
                    data[i][3] = l.get(6 * i + 3);
                    data[i][4] = l.get(6 * i + 4);
                    data[i][5] = l.get(6 * i + 5);
                }
                
                String column[] = {"FlightNum", "Airline ID", "DestinationFrom", "DestinationTo",  "Date", "Departure Time"};

                jTable1.setModel(new DefaultTableModel(data, column));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private Connection connect(){
        String url = "jdbc:sqlite:lib/data.db";
        Connection con = null;
        try{
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(url); 
        } catch (ClassNotFoundException | SQLException e){
            System.out.println(e.getMessage());
        }
        return con;
    }
    

    
    private void initComponents() {

        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        searchField = new JTextField();
        searchType = new JComboBox<>();
        searchButton = new JButton();

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    new Admin().setVisible(true);
            }
        });

        jTable1.setFont(new Font("Times New Roman", 0, 16)); // NOI18N
        jTable1.setModel(new DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        
        searchType.setModel(new DefaultComboBoxModel<>(new String[] { "All", "AirlineID", "DestinationFrom", "DestinationTo", "Date", "DepartureTime"}));
        
        searchButton.setText("Search");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
       
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(searchField, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(searchType, GroupLayout.PREFERRED_SIZE, 259, GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(searchButton, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(searchField, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchType, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        setResizable(false);
        pack();
    }                        

    private void searchButtonActionPerformed(ActionEvent evt) {                                             
        String search = searchField.getText();
        String type = searchType.getSelectedItem().toString();
        if (type.equals("All")){
            showTable("Select * From flights");
        } else {
            String sql = "Select * From flights Where " + type + " == \"" + search + "\"";
            showTable(sql);
        }
        searchField.setText("");
        
    }                                            

}
   

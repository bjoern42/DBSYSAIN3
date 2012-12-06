import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.LinkedList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class FillDatabase extends JFrame implements ActionListener{
private static final long serialVersionUID = 1L;
private JTextField tfTable = new JTextField();
private JTextArea taOutput = new JTextArea();
private JButton btInsert = new JButton("Insert");
private JFileChooser chooser = new JFileChooser();
private Connection conn;
private Statement stmt;
private ResultSet rs;

	public static void main(String[] args) {
		new FillDatabase();
	}
	
	public FillDatabase(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(BorderLayout.NORTH,tfTable);
		add(BorderLayout.CENTER,new JScrollPane(taOutput));
		add(BorderLayout.SOUTH,btInsert);
		btInsert.addActionListener(this);
		setSize(500,500);
		setVisible(true);
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url= "jdbc:oracle:thin:@oracle11g.in.htwg-konstanz.de:1521:ora11g";
			String loginData[] = JOptionPaneTwoInput.showInputDialog(this, "Bitte Login Daten einegeben",JOptionPane.OK_OPTION);
			conn= DriverManager.getConnection(url,loginData[0],loginData[1]); 
			stmt = conn.createStatement(); 
		} catch (ClassNotFoundException e) {
			taOutput.append(e.toString()+"\n");
		} catch (SQLException e) {
			taOutput.append(e.toString()+"\n");
		}
		taOutput.append("Erfolgreich angemeldet!\n");
	}
	
	private void insert(String pTableName, File pValues) throws IOException, SQLException{
		String buffer, columnName;
		LinkedList<String> values = new LinkedList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pValues)));
		while((buffer = reader.readLine()) != null){
			values.add(buffer);
		}
		reader.close();
		columnName = values.getFirst().replace(";", ",");
		for(int i=1;i<values.size();i++){
			StringBuilder builder = new StringBuilder();
			if(values.get(i).contains("random")){
				String tmp[] = values.get(i).split(";");
				for(int j=0;j<tmp.length;j++){
					if(tmp[j].startsWith("random")){
						if(tmp[j].contains("min:") && tmp[j].contains("max:")){
							int min,max;
							min = Integer.parseInt(tmp[j].substring(tmp[j].indexOf("min:")+4, tmp[j].indexOf("max:")));
							max = Integer.parseInt(tmp[j].substring(tmp[j].indexOf("max:")+4));
							builder.append(String.valueOf((int)(min+Math.random()*max)));
						}else{
							try{
								builder.append(String.valueOf((int)(1+Math.random()*getMaxID(tmp[j].substring(6)))));
							}catch(Exception e){
								taOutput.append(e.toString()+"\n");
							}
						}
					}else{
						builder.append(tmp[j]);
					}
					if(j<tmp.length-1){
						builder.append(",");
					}
				}
			}else{
				builder.append(values.get(i).replace(";", ","));
			}
			String sql = "INSERT INTO "+pTableName+" ("+columnName+") VALUES ("+builder.toString()+")";
			System.out.println(sql);
			taOutput.append(sql+"\n");
			repaint();
			try{
				stmt.executeUpdate(sql); 
			}catch(SQLException e){
				taOutput.append(e.toString()+"\n");
			}
		}
	}
	
	private int getMaxID(String pTableName) throws SQLException{
		String sql = "SELECT count(*) FROM "+pTableName;
		rs = stmt.executeQuery(sql); 
		rs.next();
		return rs.getInt("count(*)");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		new Thread(){
			public void run(){
				taOutput.setText("");
				chooser.showOpenDialog(null);
				try {
					insert(tfTable.getText(),chooser.getSelectedFile());
				} catch (SQLException e) {
					taOutput.append(e.toString()+"\n");
				} catch (IOException e) {
					taOutput.append(e.toString()+"\n");
				}
			}
		}.start();	
	}
	
	private static class JOptionPaneTwoInput extends JOptionPane{
		private static final long serialVersionUID = 1L;
		
		public static String[] showInputDialog(Component c, String title,int type){
			JTextField tf1 = new JTextField(5);
			JTextField tf2 = new JTextField(5);
			JPanel myPanel = new JPanel();
			myPanel.add(new JLabel("Username:"));
		    myPanel.add(tf1);
		    myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		    myPanel.add(new JLabel("Password:"));
		    myPanel.add(tf2);
			JOptionPane.showConfirmDialog(c, myPanel,title, JOptionPane.OK_CANCEL_OPTION);
			String retVal[] = new String[2];
			retVal[0] = tf1.getText();
			retVal[1] = tf2.getText();
			return retVal;
		}
	}
}

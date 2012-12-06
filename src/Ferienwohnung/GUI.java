package Ferienwohnung;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI extends JFrame implements ActionListener{
private static final long serialVersionUID = 1L;
private JTextField tfLand = new JTextField(), tfZimmer = new JTextField(),tfDatumAn = new JTextField(),tfDatumAb = new JTextField(),tfAusstattung = new JTextField();
private JLabel lbLand = new JLabel("Land:"), lbZimmer = new JLabel("Anzahl Min. Zimmer:"),lbDatumAn = new JLabel("Datum Ankunft:"),lbDatumAb = new JLabel("Datum Abfahrt:"),lbAusstattung = new JLabel("Ausstattung(optional):");
private JButton btSuchen = new JButton("Suchen"),btBuchen = new JButton("Buchen");
private JPanel pNorth = new JPanel(),pSouth = new JPanel();;
private Database database;

	public static void main(String[] args) {
		new GUI();
	}
	
	public GUI(){
		try {
			database = new Database("dbsys55", "dbsys55");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		setSize(500,500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pNorth.setLayout(new GridLayout(5,2));
		pNorth.add(lbLand);
		pNorth.add(tfLand);
		pNorth.add(lbZimmer);
		pNorth.add(tfZimmer);
		pNorth.add(lbDatumAn);
		pNorth.add(tfDatumAn);
		pNorth.add(lbDatumAb);
		pNorth.add(tfDatumAb);
		pNorth.add(lbAusstattung);
		pNorth.add(tfAusstattung);
		pSouth.add(btSuchen);
		pSouth.add(btBuchen);
		btSuchen.addActionListener(this);
		btBuchen.addActionListener(this);
		add(BorderLayout.NORTH,pNorth);
		add(BorderLayout.SOUTH,pSouth);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == btSuchen){
			
		}else{
			
		}
	}
}

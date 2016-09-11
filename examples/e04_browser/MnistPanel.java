package e04_browser;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JTextField;

import github.yeori.mnist.db.MnistDb;
import github.yeori.mnist.db.MnistLoop;
import github.yeori.mnist.db.Mnistlet;

import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class MnistPanel extends JPanel {
	private MnistDb db;
	private JPanel numPanel;

	private int curPage = 0;
	private JLabel lblIndex;
	private JComboBox<String> numBox;
	/**
	 * Create the panel.
	 */
	public MnistPanel() {
		this( null );
	}

	public MnistPanel(MnistDb db) {
		this.db = db;
		setLayout(new BorderLayout(0, 0));
		
		numPanel = new JPanel();
		add(numPanel, BorderLayout.CENTER);
		numPanel.setLayout(new GridLayout(10, 10, 0, 0));
		
		JPanel controllPanel = new JPanel();
		add(controllPanel, BorderLayout.SOUTH);
		GridBagLayout gbl_controllPanel = new GridBagLayout();
		gbl_controllPanel.columnWidths = new int[]{0, 0, 0};
		gbl_controllPanel.columnWeights = new double[]{1.0, 0.0, 0.0};
		gbl_controllPanel.rowHeights = new int[] {0};
		gbl_controllPanel.rowWeights = new double[]{0.0};
		controllPanel.setLayout(gbl_controllPanel);
		
		JButton btnPrev = new JButton("<");
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prevImages();
			}
		});
		
		numBox = new JComboBox<>();
		numBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				byNum( (String) numBox.getSelectedItem() );
			}
		});
		numBox.setModel(new DefaultComboBoxModel<String>(new String[] {"ALL", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"}));
		GridBagConstraints gbc_numBox = new GridBagConstraints();
		gbc_numBox.insets = new Insets(0, 0, 0, 5);
		gbc_numBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_numBox.gridx = 0;
		gbc_numBox.gridy = 0;
		controllPanel.add(numBox, gbc_numBox);
		GridBagConstraints gbc_btnPrev = new GridBagConstraints();
		gbc_btnPrev.insets = new Insets(0, 0, 0, 5);
		gbc_btnPrev.gridx = 1;
		gbc_btnPrev.gridy = 0;
		controllPanel.add(btnPrev, gbc_btnPrev);
		
		JButton btnNewButton = new JButton(">");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextImages();
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 0;
		controllPanel.add(btnNewButton, gbc_btnNewButton);
		
		lblIndex = new JLabel("  ");
		lblIndex.setFont(new Font("Verdana", Font.BOLD, 18));
		add(lblIndex, BorderLayout.NORTH);
		
		drawNum(0, 100);
	}

	protected void byNum(String n) {
		curPage = 0 ;
		drawNum(0, 100);
	}

	protected void nextImages() {
		int imgs_per_page= 100;
		int totalPage = db.size() / imgs_per_page + (db.size() % imgs_per_page > 0 ? 1 : 0);
		int nextPage = ( curPage + 1 ) % totalPage;
		
		int s = nextPage * imgs_per_page ;
		int e = s + imgs_per_page;
		
		drawNum(s, e);
		curPage = nextPage;
	}

	protected void prevImages() {
		int imgs_per_page= 100;
		int totalPage = db.size() / imgs_per_page + (db.size() % imgs_per_page > 0 ? 1 : 0);
		int nextPage = ( totalPage + curPage - 1 ) % totalPage;
		
		int s = nextPage * imgs_per_page ;
		int e = s + imgs_per_page;
		drawNum(s, e);
		curPage = nextPage;
	}

	private void drawNum(int start, int end) {
		int curIdx = numBox.getSelectedIndex();
		MnistLoop loop = null;
		
		if ( curIdx == 0 ) {
			loop = db.queryByRange(start, end);
		} else {
			char num = numBox.getItemAt( numBox.getSelectedIndex()).charAt(0);
			loop = db.query(num, start, end);
		}
		numPanel.removeAll();
		
		for ( int i = 0 ; i < loop.size(); i++ ) {
			Mnistlet m = loop.get(i);
			numPanel.add(new NumberPanel( m ) );
		}
		numPanel.revalidate();
		lblIndex.setText(String.format("[%05d - %05d]", start, end-1));
		
	}

}

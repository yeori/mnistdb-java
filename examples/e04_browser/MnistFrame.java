package e04_browser;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import github.yeori.mnist.MnistConfig;
import github.yeori.mnist.MnistUtil;
import github.yeori.mnist.db.MnistDb;

import javax.swing.JSplitPane;

public class MnistFrame extends JFrame {

	public static MnistDb db;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		InputStream in = MnistFrame.class.getResourceAsStream("mnist.config");
		
		MnistConfig config = new MnistConfig(in);
		
		db = MnistUtil.loadDb(
				config.getTrainingLabel().getAbsolutePath(),
				config.getTrainingImage().getAbsolutePath());
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MnistFrame frame = new MnistFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MnistFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		MnistPanel mnistPanel = new MnistPanel(db);
		contentPane.add(mnistPanel, BorderLayout.CENTER);
	}

}

package guiCore;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class IdleWarningDialog extends Dialog {
	
	private Label msg;
	private Button continueBtn;
	
	public IdleWarningDialog(AirTrafficSimulator owner) {
		super(owner, "Idle Warning");
		
		setLocation(700, 300);
        setResizable(false);
		setLayout(new BorderLayout());
		
		msg = new Label("");
		add(msg, BorderLayout.CENTER);
		
		Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER));
		continueBtn = new Button("Continue");
		buttons.add(continueBtn);
		add(buttons, BorderLayout.SOUTH);
		
		continueBtn.addActionListener((ae) -> {
            owner.timer.reset();
            owner.setMaskKey(true);
            dispose();
        });
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				owner.setMaskKey(true);
			}
		});
		
		pack();
	}
	
	
	public void secondsLeft(int s) {
		msg.setText("App will close in " + s + "s. Press Continue to continue.");
		pack();
		repaint();
	}
	
	public void prompt(AirTrafficSimulator owner) {
        IdleWarningDialog dialog = new IdleWarningDialog(owner);
        dialog.setVisible(true);
    }
	
}

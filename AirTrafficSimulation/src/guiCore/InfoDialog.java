package guiCore;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class InfoDialog {
	
	private InfoDialog() {}
	
	public static void showInfoDialog(Window owner, String title, String message) {
		Dialog dialog = new Dialog(owner, title, Dialog.ModalityType.APPLICATION_MODAL);
	    dialog.setLayout(new BorderLayout());

	    TextArea ta = new TextArea(message, 10, 60, TextArea.SCROLLBARS_BOTH);
	    ta.setEditable(false);
	    ta.setFocusable(false);
	    ta.setBackground(dialog.getBackground());
	    dialog.add(ta, BorderLayout.CENTER);

	    Panel buttons = new Panel(new FlowLayout(FlowLayout.RIGHT));
	    Button ok = new Button("Ok");
	    ok.addActionListener(ev -> dialog.dispose());
	    buttons.add(ok);
	    dialog.add(buttons, BorderLayout.SOUTH);

	    dialog.pack();
	    dialog.setResizable(true);
	    dialog.setLocationRelativeTo(owner);
	    dialog.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) { dialog.dispose(); }
	    });
	    dialog.setVisible(true);
	}
}

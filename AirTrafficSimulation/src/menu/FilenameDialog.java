package menu;

import java.awt.*;
import java.awt.event.*;

import guiCore.AirTrafficSimulator;

public class FilenameDialog extends Dialog {
	
	private TextField textField = new TextField(24);
    private String result = null;

    public FilenameDialog(AirTrafficSimulator owner, String title) {
        super(owner, title, true);
        setLocation(700, 300);
        setResizable(false);
        setLayout(new BorderLayout());

        add(new Label("File name:"), BorderLayout.NORTH);
        add(textField, BorderLayout.CENTER);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        Button confirm = new Button("Confirm");
        Button cancel = new Button("Cancel");
        buttons.add(confirm);
        buttons.add(cancel);
        add(buttons, BorderLayout.SOUTH);

        confirm.addActionListener((ae) -> { 
        	result = textField.getText().trim();
        	dispose(); 
        });
        textField.addActionListener((ae) -> { 
        	result = textField.getText().trim(); 
        	dispose(); 
        });
        cancel.addActionListener((ae) -> { 
        	result = null; 
        	dispose(); 
        });
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { 
            	result = null; 
            	dispose(); 
            }
        });
        
        pack();
    }

    public String prompt(AirTrafficSimulator owner, String title) {
        FilenameDialog dialog = new FilenameDialog(owner, title);
        dialog.setVisible(true);
        String filename = dialog.result;
        return filename;
    }
}

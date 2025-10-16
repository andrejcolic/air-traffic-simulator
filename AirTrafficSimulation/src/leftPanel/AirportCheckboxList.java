package leftPanel;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import guiCore.AirTrafficSimulator;
import models.Airport;

public class AirportCheckboxList extends Panel {
	private final ScrollPane scroll;
    private final Panel content;
    private final List<Checkbox> checkBoxes = new ArrayList<>();
    private final Map<Checkbox, String> codeByCb = new LinkedHashMap<>();
    AirTrafficSimulator owner;

	
	private final ItemListener stateListener = (ItemEvent e) -> {
        Checkbox src = (Checkbox) e.getSource();
        String code = codeByCb.get(src);
        if (code == null) return;

        boolean checked = (e.getStateChange() == ItemEvent.SELECTED);
        if (checked)
        	owner.visibleAirports.add(code);
        else
        	owner.visibleAirports.remove(code);


        owner.scene.repaint();
    };
    
    public AirportCheckboxList(AirTrafficSimulator owner) {
    	
        super(new BorderLayout());
        this.owner = owner;
        content = new Panel(new GridLayout(0, 1));
        scroll = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        scroll.add(content);
        add(scroll, BorderLayout.CENTER);

        refreshList();
        
    }

    public void refreshList() {
        content.removeAll();
        codeByCb.clear();
        checkBoxes.clear();

        if (owner.airportBase != null) {
            for (Airport a : owner.airportBase.getAll()) {
                String code = a.getCode();
                boolean selected = owner.visibleAirports.isEmpty() || owner.visibleAirports.contains(code);

                Checkbox cb = new Checkbox(a.toString(), selected);
                codeByCb.put(cb, code);
                checkBoxes.add(cb);
                content.add(cb);

                for (ItemListener old : cb.getItemListeners()) cb.removeItemListener(old);
                cb.addItemListener(stateListener);

                if (selected) owner.visibleAirports.add(code);
                else          owner.visibleAirports.remove(code);
            }
        }

        content.validate();
        revalidate();
        repaint();
        if(owner.scene != null)
        	owner.scene.repaint();
    }

    public java.util.List<String> getSelectedLabels() {
        java.util.List<String> out = new ArrayList<>();
        for (Checkbox cb : checkBoxes) if (cb.getState()) out.add(cb.getLabel());
        return out;
    }

    public void addItemListener(ItemListener l) {
        for (Checkbox cb : checkBoxes) cb.addItemListener(l);
    }

}
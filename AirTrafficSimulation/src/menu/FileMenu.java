package menu;

import java.awt.Color;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import exceptions.FileErrorException;
import exceptions.FilenameEmptyException;
import exceptions.InvalidExtensionException;
import exceptions.InvalidFilenameCharactersException;
import exceptions.InvalidInputException;
import guiCore.AirTrafficSimulator;
import guiCore.InfoDialog;
import models.Airport;
import models.Flight;

public class FileMenu extends Menu {
	
	private AirTrafficSimulator owner;
	public Menu load;
	public MenuItem ldAirports;
	public MenuItem ldFlights;
    public Menu save;
    public MenuItem svAirports;
    public MenuItem svFlights;
    public MenuItem exit;
	
	public FileMenu(AirTrafficSimulator owner) {
		super("File");
		this.owner = owner;
		
		load = new Menu("Load");
		ldAirports = new MenuItem("Load Airports");
		ldFlights = new MenuItem("Load Flights");		
		save = new Menu("Save");
		svAirports = new MenuItem("Save Airports");
		svFlights = new MenuItem("Save Flights");	
		exit = new MenuItem("Exit");
		
		load.add(ldAirports);
		load.add(ldFlights);
		save.add(svAirports);
		save.add(svFlights);
		this.add(load);
		this.add(save);
		this.addSeparator();
		this.add(exit);
		
		exit.setShortcut(new MenuShortcut(KeyEvent.VK_Q));
		
		exit.addActionListener(ae -> {
		    owner.dispatchEvent(new WindowEvent(owner, WindowEvent.WINDOW_CLOSING));
		});
		
		ldAirports.addActionListener(ae -> {
			String filename;
			filename = promptFile("Load Airports", "load");
			if(filename != null) {
		    	try {
                    List<List<String>> rows = readCsv(filename);
                    boolean err = false;
                    int errRows = 0;
                    int rowN = 0;
                    StringBuilder errMsg = new StringBuilder();
                    for(List<String> row : rows) {
                    	rowN++;
                    	if(row.size() != 4) {
                    		err = true;
                    		errRows++;
                    		errMsg.append("Line " + rowN + ": Invalid number of arguments.\n");
                    		continue;
                    	}
                    	try {
                    		owner.airportTablePanel.addAirport(row.get(0), row.get(1), row.get(2), row.get(3));
                    		
                    	}
                    	catch(InvalidInputException e) {
                    		err = true;
                    		errRows++;
                    		errMsg.append("Line " + rowN + ": " + e.getMessage() + "\n");
                    	}
                    }
                    owner.airportTablePanel.setStatus("Added " + String.valueOf(rows.size() - errRows) + " new airports.", Color.GREEN);
                    if(err) {
                    	throw new FileErrorException(errMsg.toString());
                    }
         
                } 
		    	catch (FileErrorException e) {
                    InfoDialog.showInfoDialog(owner, "Error", e.getMessage());
                }
			}
		    
		});

		ldFlights.addActionListener(ae -> {
			String filename;
		    filename = promptFile("Load Flights", "load");
		    if(filename != null) {
		    	try {
                    List<List<String>> rows = readCsv(filename);
                    boolean err = false;
                    int errRows = 0;
                    int rowN = 0;
                    StringBuilder errMsg = new StringBuilder();
                    for(List<String> row : rows) {
                    	rowN++;
                    	if(row.size() != 4) {
                    		err = true;
                    		errRows++;
                    		errMsg.append("Line " + rowN + ": Invalid number of arguments.\n");
                    		continue;
                    	}
                    	try {
                    		owner.flightTablePanel.addFlight(row.get(0), row.get(1), row.get(2), row.get(3));
                    		
                    	}
                    	catch(InvalidInputException e) {
                    		err = true;
                    		errRows++;
                    		errMsg.append("Line " + rowN + ": " + e.getMessage() + "\n");
                    	}
                    }
                    owner.flightTablePanel.setStatus("Added " + String.valueOf(rows.size() - errRows) + " new flights.", Color.GREEN);
                    if(err) {
                    	throw new FileErrorException(errMsg.toString());
                    }
         
                } 
		    	catch (FileErrorException e) {
                    InfoDialog.showInfoDialog(owner, "Error", e.getMessage());
                }
			}
		    
		});

		svAirports.addActionListener(ae -> {
			String filename;
		    filename = promptFile("Save Airports", "save");
		    if(filename != null) {
		    	try {
		            saveAirportsCsv(filename);
		            owner.airportTablePanel.setStatus("Airports saved to: " + filename, Color.GREEN);
		        } 
		    	catch (FileErrorException e) {
		            InfoDialog.showInfoDialog(owner, "Error", e.getMessage());
		        }
			}
		});

		svFlights.addActionListener(ae -> {
			String filename;
		    filename = promptFile("Save Flights", "save");
		    if(filename != null) {
		    	try {
		            saveFlightsCsv(filename);
		            owner.flightTablePanel.setStatus("Flights saved to: " + filename, Color.GREEN);
		        } 
		    	catch (FileErrorException e) {
		            InfoDialog.showInfoDialog(owner, "Error", e.getMessage());
		        }
			}
		});
	}
	
	private String promptFile(String title, String mode) {
		FilenameDialog dialog = new FilenameDialog(owner, title);
		String filename = "";
		try {
			filename = dialog.prompt(owner, title);
			if(filename == null) {
				return null;
			}
			validateFilename(filename, mode);
		}
		catch (Exception e) {
			InfoDialog.showInfoDialog(owner, "Error", e.getMessage());
			return null;
		}
		return filename;
	}
	
	void validateFilename(String filename, String mode) throws Exception {
		if(filename == "") {
			throw new FilenameEmptyException();
		}
		if (filename.matches(".*[*?\"<>|].*")) {
			throw new InvalidFilenameCharactersException();
		}
		if (!filename.endsWith(".csv")) {
			throw new InvalidExtensionException();
		}
		
		File f = new File(filename);
		
		if(mode == "load") {
			if (!f.exists()) { 
				throw new FileErrorException("File doesn't exist."); 
			}
	        if (!f.isFile()) { 
	        	throw new FileErrorException("File failed to open.");
	        }
	        if (!f.canRead()) {
	        	throw new FileErrorException("Can not read from file.");
	        }
		}
		else {
			File parent = f.getAbsoluteFile().getParentFile();
			if(parent != null && !parent.exists()) {
				throw new FileErrorException("Parent folder doesn't exist."); 
			}
		}
	}
	
	private List<List<String>> readCsv(String filename) throws FileErrorException {
		try {
	        List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
	        List<List<String>> out = new ArrayList<>();
	
	        for (String line : lines) {
	            if (line == null) continue;
	            line = line.trim();
	            if (line.isEmpty()) continue;
	
	            List<String> cols = splitCsvLine(line);
	            for (int i = 0; i < cols.size(); i++) cols.set(i, cols.get(i).trim());
	
	            out.add(cols);
	        }
	        return out;
		}
		catch (IOException e) {
			throw new FileErrorException("Can't read from file: " + e.getMessage());
		}
    }

	
    private List<String> splitCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder curr = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                inQuotes = !inQuotes;
            } 
            else if (c == ',' && !inQuotes) {
                fields.add(curr.toString());
                curr.setLength(0);
            } 
            else {
                curr.append(c);
            }
        }
        fields.add(curr.toString());
        return fields;
    }
    
    private void saveAirportsCsv(String filename) throws FileErrorException {
        StringBuilder sb = new StringBuilder();
        for (Airport a : owner.airportBase.getAll()) {
            sb.append(csv(a.getCode())).append(',')
              .append(csv(a.getName())).append(',')
              .append(csv(String.valueOf(a.getX()))).append(',')
              .append(csv(String.valueOf(a.getY()))).append('\n');
        }
        writeAll(filename, sb.toString());
    }
    
    private void saveFlightsCsv(String filename) throws FileErrorException {
        StringBuilder sb = new StringBuilder();
        for (Flight f : owner.flightBase.getAll()) {
            String a1 = f.getDepartureAirport().getCode();
            String a2   = f.getDestinationAirport().getCode();
            String time = String.valueOf(f.getDepartureTime());
            String dur  = String.valueOf(f.getDuration());

            sb.append(csv(a1)).append(',')
              .append(csv(a2)).append(',')
              .append(csv(time)).append(',')
              .append(csv(dur)).append('\n');
        }
        writeAll(filename, sb.toString());
    }
    
    private String csv(String s) {
        if (s == null) return "";
        boolean needsQuote = s.indexOf(',') >= 0 || s.indexOf('"') >= 0 || s.indexOf('\n') >= 0 || s.indexOf('\r') >= 0;
        if (!needsQuote) return s;
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }

    private void writeAll(String filename, String content) throws FileErrorException {
        try {
            Files.write(
                Paths.get(filename),
                content.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
            );
        } catch (IOException io) {
            throw new FileErrorException("Can't write to file: " + io.getMessage());
        }
    }
}

package Command;
import java.awt.event.ActionEvent;
import java.util.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

public class CommandPanel extends JPanel  {

    private static final long serialVersionUID = 1L;
    private static final int FONT_SIZE = 20;

    private final JTextField commandField = new JTextField();
    private final LinkedList<String> commandBuffer = new LinkedList<>();
    private boolean active = false;

    public CommandPanel() {
        class AddActionListener implements ActionListener {
            public void actionPerformed(ActionEvent event)	{ //makes sure action has been performed
            	if(active) {
            		synchronized (commandBuffer) { //synchronised defines code where multiple threads can access a variable in the same way
            			commandBuffer.add(commandField.getText());
            			commandBuffer.notify();
            		}
                }
            	commandField.setText("");
            }
        }
        ActionListener listener = new AddActionListener();
        commandField.addActionListener(listener);
        commandField.setFont(new Font("Verdana", Font.PLAIN, FONT_SIZE));
        Border border = BorderFactory.createMatteBorder(5, 5, 5, 5, Color.white); //border co-ordinates and colour
        commandField.setBorder(border); //creates a boreder
        commandField.setBackground(Color.black); //sets background to desired colour
        commandField.setForeground(Color.white); //sets foreground to diesred colout
        setLayout(new BorderLayout());
        add(commandField, BorderLayout.CENTER);
    }

    public String getCommand() {
    	active = true;
        String command;
        synchronized(commandBuffer) { //synchronised defines code where multiple threads can access a variable in the same way
            while (commandBuffer.isEmpty()) {
                try {
                    commandBuffer.wait();
                    //exception handling
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            command = commandBuffer.pop();
	    command = command.trim();

            //if were told to quit then exit program
            if(command.equalsIgnoreCase("quit"))
            	System.exit(0);
        }
        //when you exit return false to show command is no longer taking place
        active = false; 
        return command;
    }

}

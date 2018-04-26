package Command;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.*;

import Board.Board;

import java.awt.*;

public class InfoPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int FONT_SIZE = 15;

    private final JTextArea textArea  = new JTextArea();

    //constructor which sets the board and other relevant things(colour,size etc)
    public InfoPanel() {
        JScrollPane scrollPane = new JScrollPane(textArea);
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        textArea.setEditable(false);
        textArea.setFont(new Font("Verdana", Font.PLAIN, FONT_SIZE));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.black);
        textArea.setForeground(Color.white);
        Border border = BorderFactory.createMatteBorder(0, 5, 0, 5, Color.white);
        textArea.setBorder(border);
        setPreferredSize(new Dimension(Board.BOARD_WIDTH/2, Board.BOARD_HEIGHT));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); //allows you to scroll down 
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); //Indicates that the caret position is to be always updated accordingly to the document changes
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addText(String text) {
        textArea.append(":>  "+text+"\n\n");
    }
    
    public void clearTextArea() {
    	textArea.setText(null);
    }

}

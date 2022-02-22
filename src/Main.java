import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * @author : Christian Berniga
 * @class : 4 D
 * @created : 12/02/2022, sabato
 **/

class Button extends JButton{
    public int x;
    public int y;
    public int nearBombs;
    public int flowers;
    public boolean bomb;
    public Button(int x,int y){
        super();
        this.x=x;
        this.y=y;
    }
}

public class Main {
    static final int NUM_BOMS=10;
    static final int SIZE=9;
    static final int NUM_FLOWERS=3;
    static JFrame frame = new JFrame("MINESWEEPER");
    static JLabel l=new JLabel("POINTS  ");
    static JPanel panel = new JPanel();
    static JPanel panel1 = new JPanel();
    static JTextField score = new JTextField(5);
    static Button[][] buttons = new Button[9][9];
    static int points=0;
    static int turns=0;

    static void setGameWindow(){
        panel.removeAll();
        points=0;
        turns=0;
        FlowLayout Flow=new FlowLayout();
        GridLayout grid = new GridLayout(0,9);
        score.setEnabled(false);
        panel1.add(l);
        panel1.add(score);
        panel1.setLayout(Flow);
        panel.setLayout(grid);
        for(int x=0; x<SIZE;x++) {
            for(int y=0;y<SIZE;y++){
                buttons[x][y]=new Button(x,y);
                buttons[x][y].setBackground(Color.BLACK);
                buttons[x][y].setText(" ");
                buttons[x][y].addActionListener( new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(e.getSource() instanceof Button){
                            if(++turns==1)  setBombs();
                            Button b=(Button)e.getSource();
                            b.setBackground(Color.WHITE);
                            b.setEnabled(false);
                            if(b.bomb)
                                gameOver();
                            else{
                                if(b.nearBombs!=0&&!b.bomb)b.setText(Integer.toString(b.nearBombs));
                                check(b.x,b.y);
                                setPoints(b);
                            }
                            score.setText(Integer.toString(points));
                        }
                    }
                });
                panel.add(buttons[x][y]);
            }
        }
        setFlowers();
    }

    static void setBombs(){
        Button b;
        int r,c;
        for(int i=0;i<NUM_BOMS;i++){
            do{                                                     //elimina la possibilità di avere piu bombe nella stessa casella
                r=new Random().nextInt(SIZE);
                c=new Random().nextInt(SIZE);
            }while(buttons[r][c].bomb||nearflowers(r,c));           //elimino la possibilità di mettere bombe che nel 3x3 hanno fiori
            (b=buttons[r][c]).bomb=true;
            b.setIcon(new ImageIcon("icons\\BOMB.png"));
            for(int x=r-1;x<=(r+1);x++)
                for (int y=c-1;y<=(c + 1);y++) {
                    try {
                        buttons[x][y].nearBombs++;
                    } catch (ArrayIndexOutOfBoundsException e) {}   //xche potrebbe uscire dai limiti della matrice
                    b.nearBombs=0;
                    b.setText(" ");
                }
        }
    }

    static void setFlowers(){
        Button b;
        for(int i=0;i<NUM_FLOWERS;i++) {
            (b = buttons[new Random().nextInt(9)][new Random().nextInt(9)]).flowers++;
            b.setIcon(new ImageIcon("E:\\SCUOLA\\Informatica\\WorkJ\\PratoFioritoGUI\\icons\\flower.png"));
            //b.setBackground(Color.GREEN);
        }
    }

    static void setPoints(Button clicked){
        if(clicked.flowers>0)   points+=(10*clicked.flowers);
        for(int i=0;i<SIZE;i++)
            for(int j=0;j<SIZE;j++)
                if(buttons[i][j].getBackground()==Color.WHITE)  points++;
    }

    static void check(int x,int y){
        buttons[x][y].setEnabled(false);                    //tolgo la possibilità di ricliccare su buttoni scoperti (xche seno potrei schiacciare su un fiore x esempio)
        for(int i=x-1;i<=(x+1);i++)
            for(int j=y-1;j<=(y+1);j++)
                if(i>=0&&i<9&&j>=0&&j<9) {
                    //if((i!=x-1&&j!=x-1)||(i!=x-1&&j!=y+1)||(i!=x-1&&j!=y-1)||(i!=x+1&&j!=y-1)||(i!=x+1&&j!=y+1)){
                    if((i-x==1||i-x==-1)^(j-y==1||j-y==-1)){
                        /**XOR: se sono entrambe verificate o non verificate non entra, seno si
                         * (se nn sono verificate entrambe vuol dire che sono nella stessa cella su cui è invocato il metodo )
                         * (se sono entrambe verificate vuol dire che sono negli angoli del 3x3 attorno alla cella su cui è invocato il metodo)
                         * **/
                        Button b=buttons[i][j];
                        if (b.getBackground()!=Color.WHITE && !b.bomb){
                            if(b.flowers!=0)
                                b.setBackground(Color.WHITE);
                            else     b.setBackground(Color.WHITE);
                            if (buttons[i][j].nearBombs != 0)
                                buttons[i][j].setText(Integer.toString(buttons[i][j].nearBombs));
                            else
                                check(i,j);
                        }
                    }
                }
    }

    static boolean nearflowers(int x,int y){
        for(int i=x-1;i<=(x+1);i++)
            for(int j=y-1;j<=(y+1);j++)
                try{
                    if(buttons[i][j].flowers!=0)    return true;
                }catch(ArrayIndexOutOfBoundsException e){}

        return false;
    }

    static void gameOver(){
        for(int x=0; x<9;x++)
            for(int y=0;y<9;y++){
                buttons[x][y].setBackground(Color.BLACK);
                buttons[x][y].setText(" ");
            }
        JOptionPane.showMessageDialog(null, "GAME OVER!!\nyou hit a bomb");
        setGameWindow();
    }

    public static void main(String args[])
    {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 900);
        setGameWindow();
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.getContentPane().add(BorderLayout.NORTH, panel1);
        frame.setVisible(true);
    }
}

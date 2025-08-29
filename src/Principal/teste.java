package Principal;
import javax.swing.*;
import java.awt.*;


public class teste {
        public static void main(String[] args) {
            GuiFoda guigui = new GuiFoda();
            JFrame frame = new JFrame("Minha Aberração");
            frame.setContentPane(guigui.Pegar_Painel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack(); // ajusta o tamanho
            frame.setVisible(true);
        }
    }
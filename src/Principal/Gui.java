package Principal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;


public class Gui {
    public static void Colocar_item(DefaultTableModel model){
        List<String> temp = Conexão.Ler();
        int id,idade;
        String nome;
        for (int i = 0; i < temp.size();i++){
            System.out.println("lindo: " + temp.get(i));
            System.out.println("UI, DIVIDI " + Arrays.toString(temp.get(i).split(";")));
            String[] temp2 = temp.get(i).split(";");
            System.out.println("lindinho "+ temp2);
            model.addRow(new Object[]{temp2[0],temp2[1],temp2[2]});
        }

    }
    public static void Criar_Gui(){
        StringBuilder Operacao = new StringBuilder();
        JFrame frame = new JFrame("Simulador de Biblioteca");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(850,450));
        frame.setSize(350,550);
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Nome");
        model.addColumn("Idade");

        JTable table = new JTable(model);
        Colocar_item(model);

        frame.add(new JScrollPane(table));
        frame.setVisible(true);

    }

    public static void main(String[] args) {

        Criar_Gui();
    }
}
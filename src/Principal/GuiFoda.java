package Principal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

public class GuiFoda {
    private JPanel panel1;
    private JButton inserirDadosButton;
    private JButton resetarTabelaButton;
    private JButton apagarTudoButton;
    private JButton ativarFiltroButton;
    private JButton editarItemButton;
    private JButton çButton;
    private JButton apagarItemButton;
    private JButton nnSeiButton;
    private JComboBox FiltroCB;
    private JComboBox TabelaCB;
    private JTable table1;
    private JTextField NomeValor;
    private JTextField IdadeValor;
    private JTextField EmailValor;
    private JTextField AlturaValor;
    private JTextField FiltroValor;
    private JLabel NomeDaTabela;
    private JLabel Entrada1;
    private JLabel Entrada2;
    private JLabel Entrada3;
    private JLabel Entrada4;
    final static String[] Colunas = {"Id","Nome","Idade","Email","Altura"};
    public static DefaultTableModel Colocar_item( List<String> temp){

        DefaultTableModel model = new DefaultTableModel();
        for (int i=0; i<Colunas.length;i++){
            model.addColumn(Colunas[i]);
        }

        model.setRowCount(0);
        for (int i = 0; i < temp.size();i++){
            String Dividido = Arrays.toString(temp.get(i).split(";"));
            System.out.println("UI, DIVIDI " + Dividido);
            String[] temp2 = temp.get(i).split(";");
            model.addRow(new Object[]{temp2[0],temp2[1],temp2[2],temp2[3],temp2[4].replace(',','.')});
            model.fireTableDataChanged();
        }
        return model;
    }
    public static void Apagar_item(JTable model){
        int temp = model.getSelectedRow();
        int temp2 = model.convertRowIndexToModel(temp);
        String Id = model.getModel().getValueAt(temp2,0).toString();
        Conexão.Apagar(Id);
        Colocar_item(Conexão.Ler());

    }

    public JPanel Pegar_Painel(){
        Conexão.criar_tabela();
        final int[] LinhaSelecionada = new int[1];
        final int[] IdSelecionado = new int[1];
        inserirDadosButton.addActionListener(e ->{
            try{
                String NomeEscolhido = NomeValor.getText();
                int IdadeEscolhida = Integer.parseInt(IdadeValor.getText());
                String EmailEscolhido = EmailValor.getText();
                double AlturaEscolhida = Double.parseDouble(AlturaValor.getText());
                System.out.println("QUE LINDOOOOOO");

                System.out.println("Nome: " + NomeValor.getText());
                System.out.println("Idade: " + IdadeValor.getText());
                NomeValor.setText("");
                IdadeValor.setText("");
                EmailValor.setText("");
                AlturaValor.setText("");
                Conexão.Colocar(NomeEscolhido,IdadeEscolhida,EmailEscolhido,AlturaEscolhida);
                table1.setModel(Colocar_item(Conexão.Ler()));
            } catch(Exception f){
                System.out.println("Erro: "+ f.getMessage());
                JOptionPane.showMessageDialog(
                        null,
                        "Idade ou Nome Invalidos",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
                NomeValor.setText("");
                IdadeValor.setText("");
            }

        });

        apagarItemButton.addActionListener(e -> {
            try {
                Apagar_item(table1);
                NomeValor.setText("");
                IdadeValor.setText("");
                EmailValor.setText("");
                AlturaValor.setText("");
                table1.setModel(Colocar_item(Conexão.Ler()));
            } catch(Exception f){
                System.out.println("Erro: "+ f.getMessage());
                JOptionPane.showMessageDialog(
                        null,
                        "Você não tem um item Escolhido",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        editarItemButton.addActionListener(e -> {
            try {
                System.out.println("Lindo " + IdSelecionado[0] );
                if (IdSelecionado[0] != 0){
                    Conexão.Editar_item(IdSelecionado[0], NomeValor.getText(), IdadeValor.getText(),EmailValor.getText(),AlturaValor.getText());
                    table1.setModel(Colocar_item(Conexão.Ler()));
                } else{
                    JOptionPane.showMessageDialog(
                            null,
                            "Você não escolheu um item para apagar",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Algum valor coloca é invalido, verifique os valores",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );

            }
            Colocar_item(Conexão.Ler());
        });

        apagarTudoButton.addActionListener(e -> {
            System.out.println("Apaguei um itemzinho");
            Conexão.Apagar_tudo();
            Colocar_item(Conexão.Ler());
            table1.setModel(Colocar_item(Conexão.Ler()));
        });

        resetarTabelaButton.addActionListener( e -> {
            Colocar_item(Conexão.Ler());
            table1.setModel(Colocar_item(Conexão.Ler()));
        });

        ativarFiltroButton.addActionListener(e -> {

            String Filtro = FiltroCB.getSelectedItem().toString().toLowerCase();
            String ValorDoFiltro = FiltroValor.getText();
            Colocar_item(Conexão.Filtrar_item(Filtro,ValorDoFiltro));
            table1.setModel(Colocar_item(Conexão.Filtrar_item(Filtro,ValorDoFiltro)));
        });
        TabelaCB.addActionListener(e -> {
            NomeDaTabela.setText(TabelaCB.getSelectedItem().toString());
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LinhaSelecionada[0] = table1.getSelectedRow();
                IdSelecionado[0] = Integer.parseInt(table1.getValueAt(LinhaSelecionada[0],0).toString());

                NomeValor.setText(table1.getValueAt(LinhaSelecionada[0],1).toString());
                IdadeValor.setText(table1.getValueAt(LinhaSelecionada[0],2).toString());
                EmailValor.setText(table1.getValueAt(LinhaSelecionada[0],3).toString());
                AlturaValor.setText(table1.getValueAt(LinhaSelecionada[0],4).toString());
            }
        });
        table1.setModel(Colocar_item(Conexão.Ler()));
        return panel1;
    }
}

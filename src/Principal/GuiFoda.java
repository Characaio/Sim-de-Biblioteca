package Principal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
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
    private JPanel painelEntrada;
    private JComboBox EntradaCB;
    static String[] Colunas;

    /**
     * Cria um modelo de tabela novo com os valores novos
     * @param temp Valor lido da tabela
     * @return modelo para ser colocado no JTable
     */
    public static DefaultTableModel Colocar_item( List<String> temp){
        //Cria os dados que vai em uma tabela em um modelo e retorna para ser colocado no tabela
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        for (String coluna : Colunas) {
            model.addColumn(coluna);
        }
        model.setRowCount(0);
        for (String Valor : temp) {
            String Dividido = Arrays.toString(Valor.split(";"));
            System.out.println("UI, DIVIDI " + Dividido);
            String[] temp2 = Valor.split(";");
            model.addRow(new Object[]{temp2[0], temp2[1], temp2[2], temp2[3], temp2[4]});
            model.fireTableDataChanged();
        }
        return model;
    }

    /**
     * Pega o item selecionado na tabela e Apaga ele
     * @param model Tabela principal da aplicação
     */
    public static void Apagar_item(JTable model){
        //Pega o Id selecionado pelo usuario e deleta
        int temp = model.getSelectedRow();
        int temp2 = model.convertRowIndexToModel(temp);
        String Id = model.getModel().getValueAt(temp2,0).toString();
        Conexão.Apagar(Id);
        Colocar_item(Conexão.Ler());
    }

    /**
     * Coloca novos valores na combobox de input, usuarios tem uma escolha separada de Livros e Autores
     * @return Modelo atualizado da combobox
     */
    public static DefaultComboBoxModel<String> Novos_Valores_Na_ComboBox(){
        //Verifica qual é o valor da tabela atual e coloca valores novos na combo box de input baseado nisso
        //Livros e autores usam a mesma tabela
        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
        String[] Categoria = {"Aventura","Terror","Ação","Drogas","Suspense","Romance"};
        String[] ValoresUsario = {"Horrivel","Ruim","Desconhecida","Boa","Otima"};
        if (Conexão.TabelaAtual.equals("usuarios")){
            for (String Valor: ValoresUsario){
                modelo.addElement(Valor);
            }
        } else{
            for (String Valor: Categoria){
                modelo.addElement(Valor);
            }
        }
        return modelo;
    }

    /**
     * Coloca os valores atualizados nos componentes que precisam ser atualizados conforme a mudança de tabela, como ComboBoxes, filtros, entradas, etc
     */
    public void Arrumar_valores(){
        //Atualiza todos valores que dependem de outros, como os nome das tabelas, valores do filtro, resultados,Dados na tabela, etc
        Conexão.Atualizar_dados();
        Colunas = Arrays.copyOf(Conexão.Valores,Conexão.Valores.length);
        String NomeDaTabelaFormatada = Conexão.TabelaAtual.replaceFirst(Character.toString(Conexão.TabelaAtual.charAt(0)),Character.toString(Conexão.TabelaAtual.charAt(0)).toUpperCase());
        NomeDaTabela.setText(NomeDaTabelaFormatada);
        System.out.println("Valores: "+Conexão.Valores);
        Entrada1.setText(Conexão.Valores[0]);
        Entrada2.setText(Conexão.Valores[1]);
        Entrada3.setText(Conexão.Valores[2]);
        Entrada4.setText(Conexão.Valores[3]);
        EntradaCB.setModel(Novos_Valores_Na_ComboBox());
        TabelaCB.setSelectedItem(NomeDaTabelaFormatada);
        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
        for (String Valor: Conexão.Valores){
            modelo.addElement(Valor);
        }
        FiltroCB.setModel(modelo);
        table1.setModel(Colocar_item(Conexão.Ler()));
    }

    /**
     * Pega os itens da GuiFoda.form e monta a GUI com os componentes e os eventos ligados a eles
     * @return retorna o .form com sua funcionalidade
     */
    public JPanel Pegar_Painel(){
        Conexão.criar_tabela();
        painelEntrada.setSize(new Dimension(224,125));
        painelEntrada.setPreferredSize(new Dimension(224,125));

        Arrumar_valores();
        for (String coluna : Colunas) {
            System.out.println("Coluna linda " + coluna);
        }

        final int[] LinhaSelecionada = new int[1];
        final int[] IdSelecionado = new int[1];
        inserirDadosButton.addActionListener(e ->{
            try{
                String Valor1 = NomeValor.getText();
                String Valor2= IdadeValor.getText();
                String Valor3= EmailValor.getText();
                String Valor4 = EntradaCB.getSelectedItem().toString();
                System.out.println("QUE LINDOOOOOO");
                System.out.printf("%s:%s",Conexão.Valores[0],Valor1);
                System.out.printf("%s:%s",Conexão.Valores[1],Valor2);
                System.out.printf("%s:%s",Conexão.Valores[2],Valor3);
                System.out.printf("%s:%s",Conexão.Valores[3],Valor4);
                NomeValor.setText("");
                IdadeValor.setText("");
                EmailValor.setText("");
                Conexão.Colocar(Valor1,Valor2,Valor3,Valor4);
                table1.setModel(Colocar_item(Conexão.Ler()));
            } catch(Exception f){
                System.out.println("Erro: "+ f.getMessage());
                JOptionPane.showMessageDialog(
                        null,
                        "Coisas estão invalidas, verifica tudo mlk",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        apagarItemButton.addActionListener(e -> {
            try {
                Apagar_item(table1);
                NomeValor.setText("");
                IdadeValor.setText("");
                EmailValor.setText("");
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
                    Conexão.Editar_item(IdSelecionado[0], NomeValor.getText(), IdadeValor.getText(),EmailValor.getText(),EntradaCB.getSelectedItem().toString());
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
            String TabelaAntiga = Conexão.TabelaAtual;
            for (String Tabela: new String[]{"usuarios","livros","autores"}){
                Conexão.TabelaAtual = Tabela;
                Conexão.Apagar_tudo();
            }
            Conexão.TabelaAtual = TabelaAntiga;

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
            //Colocar_item(Conexão.Filtrar_item(Filtro,ValorDoFiltro));
            table1.setModel(Colocar_item(Conexão.Filtrar_item(Filtro,ValorDoFiltro)));
        });

        TabelaCB.addActionListener(e -> {
            NomeDaTabela.setText(TabelaCB.getSelectedItem().toString());
            Conexão.TabelaAtual = TabelaCB.getSelectedItem().toString().toLowerCase();
            Conexão.Atualizar_dados();
            System.out.println("Valores: "+Arrays.toString(Conexão.Valores));
            Colunas = Arrays.copyOf(Conexão.Valores,Conexão.Valores.length);
            System.out.println("Conexão atual: "+Conexão.TabelaAtual);

            Arrumar_valores();

            for (String coluna : Colunas) {
                System.out.println("Coluna linda " + coluna);
            }
            //System.out.println(painelEntrada.getSize());
        });

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LinhaSelecionada[0] = table1.getSelectedRow();
                IdSelecionado[0] = Integer.parseInt(table1.getValueAt(LinhaSelecionada[0],0).toString());
                NomeValor.setText(table1.getValueAt(LinhaSelecionada[0],1).toString());
                IdadeValor.setText(table1.getValueAt(LinhaSelecionada[0],2).toString());
                EmailValor.setText(table1.getValueAt(LinhaSelecionada[0],3).toString());
                //EntradaCB.setSelectedItem(table1.getValueAt(LinhaSelecionada[0],4).toString());
                EntradaCB.setSelectedItem(table1.getValueAt(LinhaSelecionada[0],4));
            }
        });
        table1.setModel(Colocar_item(Conexão.Ler()));
        return panel1;
    }
}
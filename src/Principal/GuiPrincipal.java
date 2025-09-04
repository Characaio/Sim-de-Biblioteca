package Principal;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.Key;

public class GuiPrincipal {
    static JFrame frame = new JFrame("Minha Aberração");
    static GuiFoda guigui = new GuiFoda();

    static final boolean[] FocoParaTeclas = {false};

    /**
     * Cria as keybinds que serão usadas no sistema
     */
    public static void Criar_Keybinds(){
        JComponent TelaDaGui = (JComponent) frame.getContentPane();
        //Cria as Keybinds
        TelaDaGui.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"),"f1pressed");
        TelaDaGui.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("1"),"1pressed");
        TelaDaGui.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("2"),"2pressed");

        //Criar as Funcionalides das Keybinds
        TelaDaGui.getActionMap().put("f1pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (FocoParaTeclas[0]) {
                    JOptionPane.showMessageDialog(
                            null,
                            "oiii" +
                                    "i",
                            "sla oq colocar aqui mai fds",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });
        TelaDaGui.getActionMap().put("1pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (FocoParaTeclas[0]) {
                    Conexão.TabelaAtual = "usuarios";
                    guigui.Arrumar_valores();

                }
            }
        });
        TelaDaGui.getActionMap().put("2pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e){
                if (FocoParaTeclas[0]) {
                    Conexão.TabelaAtual = "livros";
                    guigui.Arrumar_valores();
                }
            }
        });
        TelaDaGui.getActionMap().put("3pressed", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e){
                if (FocoParaTeclas[0]) {
                    Conexão.TabelaAtual = "autores";
                    guigui.Arrumar_valores();
                }
            }
        });
        TelaDaGui.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("3"),"3pressed");
    }
    public static void main(String[] args) {
        frame.setContentPane(guigui.Pegar_Painel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        //Coloca o foco no frame inves de uma area de texto
        frame.setVisible(true);
        SwingUtilities.invokeLater(() ->{
            frame.getContentPane().requestFocusInWindow();
        });

        /*
        Isso faz com que a aplicação descubra onde o foco esta, se o foco eestiver um uma instacia de um TextField ou uma TextArea
        O foco fica falso se o foco estiver em uma entrada de texto
        Se o foco estiver fora, as keybinds podem ser usadas
         */
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e ->{
            if (e.getID() == KeyEvent.KEY_PRESSED){
                Component comp = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

                if (!(comp instanceof JTextField) && !(comp instanceof JTextArea)){
                    FocoParaTeclas[0] = true;
                } else{
                    FocoParaTeclas[0] = false;
                }
            }
            return false;
        });
        Criar_Keybinds();

    }
}
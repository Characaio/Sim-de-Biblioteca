package Principal;

import java.sql.*;
import java.util.*;
import java.util.HashMap;
public class Conexão {
    static String url = "jdbc:sqlite:sample.db";
    static boolean executarDiretamente = false;
    static String[] ValoresAtuais = new String[5];
    static HashMap<String,String[]> map = new HashMap<>();


    static String[] Valores = new String[5];
    static String[] resultado = new String[5];
    static String[] TiposDosValores = new String[5];
    static String[] ValoresLivros = {"nome","autor","quantidade","categoria"};//String,String,int,String
    static String[] ValoresAutores = {"nome","idade","cpf","categoria"};//String,int,String,String
    static String[] ValoresUsuarios = {"nome","idade","email","reputação"};//String,int,String,Stringv
    static String TabelaAtual = "usuarios";

    /**
     * Função geral que coloca valores no PreparedStatement, cada valor é colocado baseado na tabela atual,
     * @param pstmt Comando SQL que precisa receber 4 valores
     * @param Valor1 Autoexplicativo
     * @param Valor2 Autoexplicativo
     * @param Valor3 Autoexplicativo
     * @param Valor4 Autoexplicativo
     */
    public static void Colocar_Valores(PreparedStatement pstmt, String Valor1, String Valor2, String Valor3, String Valor4){
        try {
            if (TabelaAtual.equals("usuarios")) {
                pstmt.setString(1, Valor1);
                pstmt.setInt(2, Integer.parseInt(Valor2));
                pstmt.setString(3, Valor3);
                pstmt.setString(4, Valor4);
            }
            if (TabelaAtual.equals("autores")) {
                pstmt.setString(1, Valor1);
                pstmt.setInt(2, Integer.parseInt(Valor2));
                pstmt.setString(3, Valor3);
                pstmt.setString(4, Valor4);
            }
            if (TabelaAtual.equals("livros")) {
                pstmt.setString(1, Valor1);
                pstmt.setString(2, Valor2);
                pstmt.setInt(3, Integer.parseInt(Valor3));
                pstmt.setString(4, Valor4);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Função geral que pega os valores do ResultSet, os parametros precisam ser setados antes de usar essa função, os valores são colocados no array de resultados
     * @param rs ResultSet para extrair os valores
     */
    public static void Pegar_Valores(ResultSet rs ){
        try {
            if (TabelaAtual.equals("usuarios")) {
                resultado[0] = rs.getString(Valores[0]);
                resultado[1] = String.valueOf(rs.getInt(Valores[1]));
                resultado[2] = rs.getString(Valores[2]);
                resultado[3] = rs.getString(Valores[3]);
            }

            if (TabelaAtual.equals("autores")) {
                resultado[0] = rs.getString(Valores[0]);
                resultado[1] = String.valueOf(rs.getInt(Valores[1]));
                resultado[2] = rs.getString(Valores[2]);
                resultado[3] = rs.getString(Valores[3]);
            }

            if (TabelaAtual.equals("livros")) {
                resultado[0] = rs.getString(Valores[0]);
                resultado[1] = rs.getString(Valores[1]);
                resultado[2] = String.valueOf(rs.getInt(Valores[2]));
                resultado[3] = rs.getString(Valores[3]);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Essa Função cuida da inserção de dados na tabela
     * @param Valor1 Autoexplicativo
     * @param Valor2 Autoexplicativo
     * @param Valor3 Autoexplicativo
     * @param Valor4 Autoexplicativo
     */
    public static void Colocar(String Valor1, String Valor2, String Valor3, String Valor4){
        //Tabela Livros

        String ComandoSQL = String.format("INSERT INTO %s (%s,%s,%s,%s) VALUES (?,?,?,?)",TabelaAtual,
                ValoresAtuais[0],ValoresAtuais[1],ValoresAtuais[2],ValoresAtuais[3]);
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(ComandoSQL)) {
            Colocar_Valores(pstmt,Valor1,Valor2,Valor3,Valor4);
            pstmt.execute();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Le todos os dados de um unico usuario baseado em seu Id
     * @param Id Id Do usuario a ser lido
     */
    public static void Ler_usuario_especifico(int Id){
        String ComandoSQL = String.format("SELECT * FROM %s WHERE id = ?",TabelaAtual);
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(ComandoSQL);
        ){

            pstmt.setInt(1,Id);
            ResultSet rs = pstmt.executeQuery();
            Pegar_Valores(rs);
            System.out.printf("Id: %d|%s: %s|%s: %s|%s: %s|%s: %s| \n",Id,Valores[0],resultado[0],Valores[1],resultado[1],Valores[2],resultado[2],Valores[3],resultado[3]);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Le todos os valores da tabela atual, eles são colocados em uma lista e separados pelo caractere ';', a ordem dos item segue a ordem das tabelas e Valores
     * @return Todos os itens da tabela atual
     */
    public static List<String> Ler(){
        List<String> lista = new ArrayList<>();
        String ComandoSQL = String.format("SELECT * FROM %s",TabelaAtual);
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(ComandoSQL)){
            while (rs.next()){

                int id = rs.getInt("id");
                Pegar_Valores(rs);
                lista.add(String.format("%d;%s;%s;%s;%s",id,resultado[0],resultado[1],resultado[2],resultado[3]));
                System.out.printf("Id: %d|%s: %s|%s: %s|%s: %s|%s: %s| \n",id,Valores[0],resultado[0],Valores[1],resultado[1],Valores[2],resultado[2],Valores[3],resultado[3]);

            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return lista;
    }

    /**
     * Edita um item com base em seu Id, Valores são passados para serem colocados, caso seja vazio, o valor que sera usado no final é o valor ja existente na tabela.
     * Valores são passados como String, mas eles são transformado em seus Valores corretos
     * @param Id Id do item
     * @param Valor1 Autoexplicativo
     * @param Valor2 Autoexplicativo
     * @param Valor3 Autoexplicativo
     * @param Valor4 Autoexplicativo
     */
    public static void Editar_item(int Id,String Valor1, String Valor2,String Valor3, String Valor4){
        String[] Valores = map.get(TabelaAtual);
        String ComandoSQL1 = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ? WHERE id = ?",TabelaAtual,Valores[0],Valores[1],Valores[2],Valores[3]);
        String ComandoSQL2 = String.format("SELECT * FROM %s WHERE id = ?",TabelaAtual);
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(ComandoSQL1);
             PreparedStatement pstmt2 = conn.prepareStatement(ComandoSQL2)){
            pstmt2.setInt(1,Id);
            ResultSet rs = pstmt2.executeQuery();

            Pegar_Valores(rs);

            String NovoNome = resultado[0];
            String NovaIdade = resultado[1];
            String NovoEmail = resultado[2];
            String NovaAltura = resultado[3];

            if (!Valor1.isEmpty()) {
                NovoNome = Valor1;
            }
            if (!Valor2.isEmpty()) {
                NovaIdade = Valor2;
            }
            if (!Valor3.isEmpty()){
                NovoEmail = Valor3;
            }
            if (!Valor4.isEmpty()){
                NovaAltura = Valor4;
            }
            //String[] Valores = map.get(TabelaAtual);
            System.out.printf("%s:%s\n%s:%s\n%s:%s\n%s:%s \n",Valores[0],NovoNome,Valores[1],NovaIdade,Valores[2],NovoEmail,Valores[3],NovaAltura);

            Colocar_Valores(pstmt,NovoNome,NovaIdade,NovoEmail,NovaAltura);
            pstmt.setInt(5,Id);
            pstmt.execute();

            Ler();

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Apaga apenas um item slecionado pelo Id
     * @param Id Variavel usada para deletar um item da tabela
     */
    public static void Apagar(String Id){
        String ComandoSQL = String.format("DELETE FROM %s WHERE id = ?",TabelaAtual);
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(ComandoSQL)
        ){
            int IdInt = Integer.parseInt(Id);
            pstmt.setInt(1,IdInt);
            pstmt.executeUpdate();

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Essa função cuida da filtragem de valores na lista
     * @param Filtro Valor da coluna escolhida para filtrar
     * @param EscolhaDoFiltro Valor para ser filtrada
     * @return Retorna os itens do banco de dados que encaixa no filtro
     */
    public static List<String> Filtrar_item(String Filtro, String EscolhaDoFiltro){

        List<String> lista = new ArrayList<>();

        if (!Arrays.asList(ValoresAtuais).contains(Filtro)){
            throw new IllegalArgumentException("Filtro escolhido invalido");
        }
        String ComandoSQL = String.format("SELECT * FROM %s WHERE %s = ?",TabelaAtual,Filtro);
        try( Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(ComandoSQL)){
            System.out.println("Filto/EscolhaDoFiltro: "+ Filtro + "/" + EscolhaDoFiltro);
            if (Arrays.asList(new String[]{"quantidade_no_estoque","idade","cpf","idade"}).contains(Filtro)){
                int resultado = Integer.parseInt(EscolhaDoFiltro);
                pstmt.setInt(1,resultado);
            } else{
                pstmt.setString(1,EscolhaDoFiltro);
            }

            //static String[] ValoresLivros = {"nome","autor","categoria","quantidade_no_estoque"};//String,String,String,int
            //static String[] ValoresAutores = {"nome","idade","cpf","categoria"};//String,int,int,String
            //static String[] ValoresUsuarios = {"nome","idade","email","reputação"};//String,int,String,Stringv

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Pegar_Valores(rs);
                lista.add(String.format("%d;%s;%s;%s;%s", rs.getInt("id"),resultado[0],resultado[1],resultado[2],resultado[3]));
                System.out.printf("%d|%s|%s|%s|%s", rs.getInt("id"),resultado[0],resultado[1],resultado[2],resultado[3]); //Id;Nome;Idade;Email;Altura lista.add(String.format("%d;%s;%d;%s;%.2f",id,nome,idade,email,altura)); }

            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return lista;
    }

    /**
     *Essa função Apaga todos os dados da tabela selecionada e reinicia ela para começar do id 0 novamente
     */
    public static void Apagar_tudo(){

        String ComandoSQL1 = String.format("DELETE FROM %s",TabelaAtual);
        String ComandoSQL2 = String.format("DELETE FROM sqlite_sequence WHERE name = '%s'",TabelaAtual);
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(ComandoSQL1);
             PreparedStatement pstmt2 = conn.prepareStatement(ComandoSQL2)) {
            pstmt.executeUpdate();
            pstmt2.executeUpdate();
            System.out.println("Apaguei a porra tudo");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Essa função Cria todas tabelas necessarias, o valor de inicio dessa tabela é 'usuarios' por padrão
     */
    public static void criar_tabela(){
        ValoresAtuais = Arrays.copyOf(ValoresUsuarios,ValoresUsuarios.length);
        map.put(TabelaAtual,ValoresAtuais);
        Valores = map.get(TabelaAtual);
        System.out.println(map);

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();){
            if (conn != null) {
                System.out.println("Connection to SQLite has been established.");
            }
            System.out.printf("%s||%s \n",TabelaAtual,Arrays.toString(ValoresAtuais));
            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT, " +
                    "idade INTEGER, "+
                    "email TEXT, " +
                    "reputação TEXT)");

            stmt.execute("CREATE TABLE IF NOT EXISTS livros ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT, " +
                    "autor TEXT , "+
                    "quantidade INTEGER, " +
                    "categoria TEXT)");


            stmt.execute("CREATE TABLE IF NOT EXISTS autores ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT, " +
                    "idade INTEGER, "+
                    "cpf TEXT, " +
                    "categoria TEXT)");
            System.out.println("tabela criada/existe");


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Atualiza Variaveis Importantes, como ValoresAtuais, map e Valores
     */
    public static void Atualizar_dados(){
        System.out.printf("Mapa: %s \n",map);
        if (TabelaAtual.equals("usuarios")){
            ValoresAtuais = Arrays.copyOf(ValoresUsuarios,ValoresUsuarios.length);
        }
        if (TabelaAtual.equals("livros")){
            ValoresAtuais = Arrays.copyOf(ValoresLivros,ValoresLivros.length);
        }
        if (TabelaAtual .equals("autores")){
            ValoresAtuais = Arrays.copyOf(ValoresAutores,ValoresAutores.length);
        }
        map.clear();
        map.put(TabelaAtual,ValoresAtuais);
        System.out.printf("Tabela atual %s|| ValoresAtuais \n",TabelaAtual,Arrays.toString(ValoresAtuais));
        Valores = map.get(TabelaAtual);
        System.out.printf("Dados: %s|| Valores: %s\n",map,Arrays.toString(Valores));
    }
    public static void main(String[] args) {
        boolean continuar = true;
        String Escolha;
        executarDiretamente = true;
        Scanner scan = new Scanner(System.in);
        criar_tabela();




        System.out.println('u');
        while (continuar){
            System.out.printf("Tabela selecionada: %s \n", TabelaAtual);
            System.out.println("[C]olocar, [L]er, [A]pagar, Apagar Tudo[AP], [E]ditar,Filtrar Por[FP], Mostrar Tabelas[MT],Mudar De Tabela[MDT] ou [S]air? ");
            Escolha = scan.nextLine().toUpperCase();

            if (Escolha.equals("C")){
                String[] valores = map.get(TabelaAtual);
                System.out.printf("Coloque um valor para %s em %s\n",valores[0],TabelaAtual);
                String Valor1 = scan.nextLine();

                System.out.printf("Coloque um valor para %s em %s\n",valores[1],TabelaAtual);
                String Valor2 = scan.nextLine();

                System.out.printf("Coloque um valor para %s em %s\n",valores[2],TabelaAtual);
                String Valor3 = scan.nextLine();

                System.out.printf("Coloque um valor para %s em %s ",valores[3],TabelaAtual);
                if (valores[3].equals("reputação")){
                    System.out.println("[ruim/media/boa/desconhecida]");
                }
                System.out.println();
                String Valor4 = scan.nextLine();

                Colocar(Valor1,Valor2,Valor3,Valor4);


            }
            if (Escolha.equals("L")){
                Ler();
            }
            if (Escolha.equals("A")){
                Ler();
                System.out.printf("Qual item em %s você quer apagar? use o ID\n",TabelaAtual);
                String Id = scan.nextLine();
                String Escolha1;

                Ler_usuario_especifico(Integer.parseInt(Id));
                System.out.println("Apagar este item?[S/N]");
                Escolha1 = scan.nextLine().toUpperCase();

                if (Escolha1.equals("S")){
                    Apagar(Id);
                }

            }
            if (Escolha.equals("AP")){
                Apagar_tudo();
            }
            if (Escolha.equals("E")){
                System.out.printf("qual item em %s você quer editar? Use o ID: \n",TabelaAtual);
                String id = scan.nextLine();

                System.out.printf("item de %s escolhido: \n",TabelaAtual);
                Ler_usuario_especifico(Integer.parseInt(id));
                String escolha1;

                String Valor1 = "";
                String Valor2 = "";
                String Valor3 = "";
                String Valor4 = "";
                //String[] Valores = map.get(TabelaAtual);
                System.out.printf("Mudar o/a %s atual?[S/N] \n",Valores[0]);

                for (int i = 0;i<Valores.length;i++){
                    System.out.printf("Mudar o/a %s atual?[S/N]\n",Valores[i]);
                    String temp = scan.nextLine().toUpperCase();
                    String temp2 = "";
                    if (temp.equals("S")){
                        System.out.printf("Mudar %s para oque?",Valores[i]);
                        if (Valores[i].equals("reputação")){
                            System.out.println("[boa,media,ruim,desconhecida]");
                        } else{
                            System.out.println();
                        }
                        temp2 = scan.nextLine();
                    }
                    if (i == 0){
                        Valor1 = temp2;
                    }
                    if (i == 1){
                        Valor2 = temp2;
                    }
                    if (i == 2){
                        Valor3 = temp2;
                    }
                    if (i==3){
                        Valor4 = temp2;
                    }
                }

                Editar_item(Integer.parseInt(id),Valor1,Valor2,Valor3,Valor4);
            }
            if (Escolha.equals("FP")){
                //String[] valores = map.get(TabelaAtual);
                System.out.printf("Você quer filtrar por qual item?[%s/%s/%s/%s]\n",Valores[0],Valores[1],Valores[2],Valores[3]);
                String Filtro = scan.nextLine().toLowerCase();

                System.out.println("Filtrar por qual parametro?");
                String EscolhaDoFiltro = scan.nextLine().toLowerCase();

                Filtrar_item(Filtro,EscolhaDoFiltro);
            }
            if (Escolha.equals("MDT")){
                System.out.println("Mudar para qual tabela?[usuarios/autores/livros]");
                String TabelaEscolhida = scan.nextLine().toLowerCase();
                if (Arrays.asList(new String[]{"usuarios", "autores", "livros"}).contains(TabelaEscolhida)){
                    System.out.println("Tabela valida");
                    TabelaAtual = TabelaEscolhida;
                } else{
                    System.out.println("TABELA INVALIDA POURAAAAAAAA");
                    break;
                }

                System.out.printf("Tabela atual é %s \n", TabelaEscolhida);

                Atualizar_dados();
            }
            if (Escolha.equals("S")){
                continuar = false;
            }
        }
    }
}

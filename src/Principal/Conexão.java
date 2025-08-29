package Principal;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Conexão {
    static String url = "jdbc:sqlite:sample.db";
    static boolean executarDiretamente = false;
    public static void Colocar(String Nome,int Idade,String Email, double Altura){
        try(Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            PreparedStatement pstwt = conn.prepareStatement("INSERT INTO usuarios (nome, idade, email, altura) VALUES (?,?,?,?)");){


            pstwt.setString(1,Nome);
            pstwt.setInt(2,Idade);
            pstwt.setString(3,Email);
            pstwt.setDouble(4, Altura);
            pstwt.executeUpdate();
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static void Ler_usario_especifico(int Id){
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM usuarios WHERE id = ?");
        ){

            pstmt.setInt(1,Id);
            ResultSet rs = pstmt.executeQuery();
            int id = rs.getInt("id");
            String nome = rs.getString("nome");
            int idade = rs.getInt("idade");
            String email = rs.getString("email");
            double altura = rs.getDouble("altura");
            System.out.printf("Id: %d|Nome: %s|Idade: %d|Email: %s|Altura: %2.f \n",id,nome,idade,email,altura);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static List<String> Ler(){
        List<String> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios")){
            while (rs.next()){
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int idade = rs.getInt("idade");
                String email = rs.getString("email");
                double altura = rs.getDouble("altura");
                System.out.printf("Id: %d|Nome: %s|Idade: %d|Email: %s|Altura: %.2f \n",id,nome,idade,email,altura);
                lista.add(String.format("%d;%s;%d;%s;%.2f",id,nome,idade,email,altura));
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return lista;
    }
    public static void Editar_item(int Id,String Nome, String Idade,String email, String Altura){
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement("UPDATE usuarios SET nome = ?, idade = ?, email = ?, altura = ? WHERE id = ?");
            PreparedStatement pstmt2 = conn.prepareStatement("SELECT * FROM usuarios WHERE id = ?")){
            pstmt2.setInt(1,Id);
            ResultSet rs = pstmt2.executeQuery();

            String NovoNome = rs.getString("nome");
            int NovaIdade = rs.getInt("idade");
            String NovoEmail = rs.getString("email");
            double NovaAltura = rs.getDouble("altura");


            if (!Nome.isEmpty()) {
                NovoNome = Nome;
            }
            if (!Idade.isEmpty()) {
                NovaIdade = Integer.parseInt(Idade);
            }
            if (!email.isEmpty()){
                NovoEmail = email;
            }
            if (!Altura.isEmpty()){
                NovaAltura = Double.parseDouble(Altura);
            }
            System.out.printf("Nome: %s  Idade: %d  Email: %s  Altura: %f \n", NovoNome,NovaIdade,NovoEmail,NovaAltura);

            pstmt.setString(1,NovoNome);
            pstmt.setInt(2,NovaIdade);
            pstmt.setString(3,NovoEmail);
            pstmt.setDouble(4,NovaAltura);
            pstmt.setInt(5,Id);

            pstmt.execute();

            Ler();

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
    public static void Apagar(String Id){
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM usuarios WHERE id = ?")
        ){
            int IdInt = Integer.parseInt(Id);
            pstmt.setInt(1,IdInt);
            pstmt.executeUpdate();

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static List<String> Filtrar_item(String Filtro, String EscolhaDoFiltro){
        List<String> lista = new ArrayList<>();
        List<String> validos = Arrays.asList("id", "nome", "idade","email","altura");

        if (!validos.contains(Filtro)){
            throw new IllegalArgumentException("Filtro escolhido invalido");
        }

        try( Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM usuarios WHERE " + Filtro + " = ?");){
            System.out.println("Filto/EscolhaDoFiltro: "+ Filtro + "/" + EscolhaDoFiltro);
            if (Filtro.equals("idade")){
                pstmt.setInt(1,Integer.parseInt(EscolhaDoFiltro));
            } else if (Filtro.equals("altura")){
                pstmt.setDouble(1,Double.parseDouble(EscolhaDoFiltro));
            }else if (Filtro.equals("nome")) {
                pstmt.setString(1, EscolhaDoFiltro);
            }


            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int idade = rs.getInt("idade");
                String email = rs.getString("email");
                double altura = rs.getDouble("altura");
                System.out.printf("%d;%s;%d;%s;%.2f \n",id,nome,idade,email,altura);
                //Id;Nome;Idade;Email;Altura
                lista.add(String.format("%d;%s;%d;%s;%.2f",id,nome,idade,email,altura));
            }


        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return lista;
    }
    public static void Apagar_tudo(){
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM usuarios");
             PreparedStatement pstmt2 = conn.prepareStatement("DELETE FROM sqlite_sequence WHERE name='usuarios'")) {
            pstmt.executeUpdate();
            pstmt2.executeUpdate();
            System.out.println("Apaguei a porra tudo");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
    public static void criar_tabela(){
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();){
            if (conn != null) {
                System.out.println("Connection to SQLite has been established.");
            }

            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT, " +
                    "idade INTEGER, "+
                    "email TEXT, " +
                    "altura DOUBLE)");
            System.out.println("tabela criada/existe");



        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) {
        boolean continuar = true;
        String Escolha;
        executarDiretamente = true;
        Scanner scan = new Scanner(System.in);

        criar_tabela();


        while (continuar){
            System.out.println("[C]olocar, [L]er, [A]pagar, Apagar Tudo[AP], [E]ditar,Selecionar Por[SP] ou [S]air? ");
            Escolha = scan.nextLine().toUpperCase();

            if (Escolha.equals("C")){

                System.out.println("Qual é seu nome?");
                String Nome = scan.nextLine();

                System.out.println("Qual é a sua idade?");
                int Idade = scan.nextInt();

                System.out.println("Qual é a seu email?");
                scan.nextLine();
                String Email = scan.nextLine();

                System.out.println("Qual é a sua altura?");
                double Altura = scan.nextDouble();

                Colocar(Nome,Idade,Email,Altura);
            }
            if (Escolha.equals("L")){
                Ler();
            }
            if (Escolha.equals("A")){
                Ler();
                System.out.println("Qual usuario você quer apagar? use o Id");
                String Id = scan.nextLine();
                String Escolha1;

                Ler_usario_especifico(Integer.parseInt(Id));
                System.out.println("Apagar esse Usuario?[S/N]");
                Escolha1 = scan.nextLine().toUpperCase();

                if (Escolha1.equals("S")){
                    Apagar(Id);
                }

            }
            if (Escolha.equals("AP")){
                Apagar_tudo();
            }
            if (Escolha.equals("E")){
                System.out.println("Qual usuario você quer editar? Use o ID:");
                String id = scan.nextLine();

                System.out.println("USUARIO SELECIONADO:");
                Ler_usario_especifico(Integer.parseInt(id));
                String escolha1;

                String idade = "";
                String Nome = "";
                String Email = "";
                String Altura = "";
                System.out.println("Mudar o nome atual?[S/N]");
                escolha1 = scan.nextLine().toUpperCase();
                if (escolha1.equals("S")){
                    System.out.println("Qual nome você quer colocar nele?");
                    Nome = scan.nextLine();
                    escolha1 = null;
                }
                System.out.println("Mudar a idade atual?[S/N]");
                escolha1 = scan.nextLine().toUpperCase();
                if (escolha1.equals("S")){
                    System.out.println("Qual idade você quer colocar?");
                    idade = scan.nextLine();
                    escolha1 = null;
                }
                System.out.println("Mudar a Email atual?[S/N]");
                escolha1 = scan.nextLine().toUpperCase();
                if (escolha1.equals("S")){
                    System.out.println("Qual email você quer colocar?");
                    Email = scan.nextLine();
                    escolha1 = null;
                }
                System.out.println("Mudar a altura atual?[S/N]");
                escolha1 = scan.nextLine().toUpperCase();
                if (escolha1.equals("S")){
                    System.out.println("Qual altura você quer colocar?");
                    Altura = scan.nextLine();
                    escolha1 = null;
                }
                Editar_item(Integer.parseInt(id),Nome,idade,Email,Altura);
            }
            if (Escolha.equals("SP")){
                System.out.println("Você quer filtrar por qual parametro?[Nome/Idade/Email/Altura]");
                String Filtro = scan.nextLine().toLowerCase();

                System.out.println("Filtrar por qual parametri?");
                String EscolhaDoFiltro = scan.nextLine().toLowerCase();

                Filtrar_item(Filtro,EscolhaDoFiltro);
            }
            if (Escolha.equals("S")){
                continuar = false;
            }
        }
    }
}
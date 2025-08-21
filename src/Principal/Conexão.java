package Principal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Conexão {
    static String url = "jdbc:sqlite:sample.db"; // Path to your SQLite database file
    public static void Colocar(){
        try(Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();){

            Scanner scan = new Scanner(System.in);

            System.out.println("Qual é seu nome?");
            String Nome = scan.nextLine();

            System.out.println("Qual é a sua idade?");
            int Idade = scan.nextInt();

            PreparedStatement pstwt = conn.prepareStatement("INSERT INTO usuarios (nome, idade) VALUES (?,?)");
            pstwt.setString(1,Nome);
            pstwt.setInt(2,Idade);
            pstwt.executeUpdate();
        } catch(SQLException e){
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
                System.out.printf("Id: %d|Nome: %s|Idade: %d \n",id,nome,idade);
                lista.add(String.format("%d;%s;%d",id,nome,idade));
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return lista;
    }

    public static void Apagar(String Nome){
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM usuarios WHERE nome = ?");
        ){
            pstmt.setString(1,Nome);
            int linhas = pstmt.executeUpdate();
            if (linhas > 0){
                System.out.printf("Usuario: %s deletado \n",Nome);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) {
        boolean continuar = true;
        String Escolha;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();){
            if (conn != null) {
                System.out.println("Connection to SQLite has been established.");
            }

            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios ("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome TEXT, " +
                    "idade INTEGER)");
            System.out.println("tabela criada/existe");



        } catch (SQLException e) {
            System.out.println(e.getMessage());
    }


    }
}
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Trabalho {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Random random = new Random();
        int Senha=random.nextInt(100),NumeroEscolhido=-1;
        int tentativas = 0;
        while (Senha != NumeroEscolhido){
            tentativas += 1;
            System.out.println("Escolha um numero");
            NumeroEscolhido = scan.nextInt();
            if (NumeroEscolhido > Senha){
                System.out.println("Esse numero é maior que a senha");
            } else if (NumeroEscolhido < Senha){
                System.out.println("Esse Numero é menor que a senha");
            } else{
                System.out.println("Esse é o numero certo");
            }
        }
        System.out.printf("Você acertou em %d tentativas \n",tentativas);
    }
}

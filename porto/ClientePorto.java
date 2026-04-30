package porto;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClientePorto {
    public static void main(String[] args) {
        try {
            // Conecta ao servidor RMI
            Registry registry = LocateRegistry.getRegistry("localhost", 8085); // 8085 é a porta padrão
            IPorto porto = (IPorto) registry.lookup("PortoService");

            Scanner scanner = new Scanner(System.in);
            int opcao = -1;

            while (opcao != 0) {
                // Menu de opções para o cliente
                System.out.println("\n--- Sistema de Porto ---");
                System.out.println("1. Embarcar Carga");
                System.out.println("2. Relatório de Embarque");
                System.out.println("3. Cadastrar Navio");
                System.out.println("4. Remover Navio");
                System.out.println("5. Relatório de Navios");
                System.out.println("6. Cadastrar Carga");
                System.out.println("7. Remover Carga");
                System.out.println("8. Relatório de Cargas");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpar o buffer

                switch (opcao) {
                    case 1:
                        System.out.print("Descrição do embarque: ");
                        String descricaoEmbarque = scanner.nextLine();
                        double cargaEmbarcada = porto.embarcar(descricaoEmbarque);
                        System.out.println("Carga embarcada: " + cargaEmbarcada);
                        break;

                    case 2:
                        String relatorioEmbarque = porto.relatorio_embarque();
                        System.out.println(relatorioEmbarque);
                        break;

                    case 3:
                        System.out.print("Descrição do navio: ");
                        String descricaoNavio = scanner.nextLine();
                        System.out.print("Capacidade do navio: ");
                        int capacidadeNavio = scanner.nextInt();
                        Integer idNavio = porto.cadastrar_navio(descricaoNavio, capacidadeNavio);
                        System.out.println("Navio cadastrado com ID: " + idNavio);
                        break;

                    case 4:
                        System.out.print("ID do navio a ser removido: ");
                        int idRemoverNavio = scanner.nextInt();
                        porto.remover_navio(idRemoverNavio);
                        System.out.println("Navio removido com sucesso!");
                        break;

                    case 5:
                        String relatorioNavios = porto.relatorio_navio();
                        System.out.println(relatorioNavios);
                        break;

                    case 6:
                        System.out.print("Descrição da carga: ");
                        String descricaoCarga = scanner.nextLine();
                        System.out.print("Volume da carga: ");
                        int volumeCarga = scanner.nextInt();
                        Integer idCarga = porto.cadastrar_carga(descricaoCarga, volumeCarga);
                        System.out.println("Carga cadastrada com ID: " + idCarga);
                        break;

                    case 7:
                        System.out.print("ID da carga a ser removida: ");
                        int idRemoverCarga = scanner.nextInt();
                        porto.remover_carga(idRemoverCarga);
                        System.out.println("Carga removida com sucesso!");
                        break;

                    case 8:
                        String relatorioCargas = porto.relatorio_carga();
                        System.out.println(relatorioCargas);
                        break;

                    case 0:
                        System.out.println("Saindo...");
                        break;

                    default:
                        System.out.println("Opção inválida.");
                        break;
                }
            }

            scanner.close();

        } catch (Exception e) {
            System.out.println("Erro ao conectar ao servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
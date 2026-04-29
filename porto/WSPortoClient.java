package porto;

import java.net.URL;
import java.util.Scanner;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class WSPortoClient {
    public static void main(String[] args) {
        try {
            // Configuração da conexão com o Web Service
            URL url = new URL("http://localhost:8085/ws/porto?wsdl");
            QName qname = new QName("http://porto/", "WSPortoServerImplService");
            
            Service service = Service.create(url, qname);
            WSPortoServer portoWS = service.getPort(WSPortoServer.class);

            Scanner scanner = new Scanner(System.in);
            int opcao = -1;

            System.out.println("--- Cliente Web Service Porto Conectado ---");

            while (opcao != 0) {
                System.out.println("\n--- Menu ---");
                System.out.println("1. Embarcar Carga");
                System.out.println("2. Relatório de Embarque");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                
                try {
                    opcao = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, insira um número válido.");
                    continue;
                }

                switch (opcao) {
                    case 1:
                        System.out.print("Descrição do embarque: ");
                        String descricao = scanner.nextLine();
                        double valor = portoWS.embarcar(descricao);
                        System.out.println("Sucesso! Valor embarcado: " + valor);
                        break;

                    case 2:
                        String relatorio = portoWS.relatorio_embarque();
                        System.out.println("\n[RELATÓRIO RECEBIDO]");
                        System.out.println(relatorio);
                        break;

                    case 0:
                        System.out.println("Encerrando cliente WS...");
                        break;

                    default:
                        System.out.println("Opção inválida.");
                        break;
                }
            }
            scanner.close();

        } catch (Exception e) {
            System.err.println("Erro ao conectar ao Web Service: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
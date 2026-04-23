package porto;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Porto extends UnicastRemoteObject implements IPorto {

    private ArrayList<Carga> cargas;
    private ArrayList<Navio> navios;
    private double totalEmbarcado;

    public Porto() throws RemoteException {
        super();
        this.navios = new ArrayList<>();
        this.cargas = new ArrayList<>();
        this.totalEmbarcado = 0.0;
    }

    @Override
    public double embarcar(String descricao) throws RemoteException {
        double cargaEmbarcada = Math.random() * 1000;
        totalEmbarcado += cargaEmbarcada;
        System.out.println("Embarque realizado: " + descricao + " - Carga: " + cargaEmbarcada);
        return cargaEmbarcada;
    }

    @Override
    public String relatorio_embarque() throws RemoteException {
        return "Total embarcado até agora: " + totalEmbarcado + " unidades.";
    }

    @Override
    public Integer cadastrar_navio(String descricao, Integer capacidade) throws RemoteException {
        Navio navio = new Navio(descricao, capacidade);
        navios.add(navio);
        System.out.println("Navio cadastrado: " + descricao);
        return navio.getId();
    }

    @Override
    public void remover_navio(Integer id) throws RemoteException {
        navios.removeIf(navio -> navio.getId().equals(id));
        System.out.println("Navio removido com ID: " + id);
    }

    @Override
    public String relatorio_navio() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append("Relatório dos navios cadastrados:\n");
        for (Navio navio : navios) {
            sb.append("ID: ").append(navio.getId())
              .append(", Descrição: ").append(navio.getDescricao())
              .append(", Capacidade: ").append(navio.getCapacidade())
              .append("\n");
        }
        return sb.toString();
    }

    @Override
    public Integer cadastrar_carga(String descricao, Integer volume) throws RemoteException {
        Carga carga = new Carga(descricao, volume);
        cargas.add(carga);
        System.out.println("Carga cadastrada: " + descricao);
        return carga.getId();
    }

    @Override
    public void remover_carga(Integer id) throws RemoteException {
        cargas.removeIf(carga -> carga.getId().equals(id));
        System.out.println("Carga removida com ID: " + id);
    }

    @Override
    public String relatorio_carga() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append("Relatório das cargas cadastradas:\n");
        for (Carga carga : cargas) {
            sb.append("ID: ").append(carga.getId())
              .append(", Descrição: ").append(carga.getDescricao())
              .append(", Volume: ").append(carga.getVolume())
              .append("\n");
        }
        return sb.toString();
    }

    // Classe interna para representar um navio
    private class Navio {
        private int contadorNavios = 0;
        private Integer id;
        private String descricao;
        private Integer capacidade;

        public Navio(String descricao, Integer capacidade) {
            this.id = ++contadorNavios;
            this.descricao = descricao;
            this.capacidade = capacidade;
        }

        public Integer getId() {
            return id;
        }

        public String getDescricao() {
            return descricao;
        }

        public Integer getCapacidade() {
            return capacidade;
        }
    }

    // Classe interna para representar uma carga
    private class Carga {
        private int contadorCargas = 0;
        private Integer id;
        private String descricao;
        private Integer volume;

        public Carga(String descricao, Integer volume) {
            this.id = ++contadorCargas;
            this.descricao = descricao;
            this.volume = volume;
        }

        public Integer getId() {
            return id;
        }

        public String getDescricao() {
            return descricao;
        }

        public Integer getVolume() {
            return volume;
        }
    }

    // Método main que inicializa o servidor RMI
    public static void main(String[] args) {
        try {
            // Cria o registro RMI na porta 1099
            Registry registry = LocateRegistry.createRegistry(1099);
            // Cria o objeto do servidor
            Porto porto = new Porto();
            // Registra o serviço no registro RMI com o nome "PortoService"
            registry.rebind("PortoService", porto);
            System.out.println("Servidor RMI iniciado. Serviço 'PortoService' está disponível.");
        } catch (Exception e) {
            System.out.println("Erro ao inicializar o servidor RMI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
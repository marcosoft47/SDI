package porto;
 
import javax.jws.WebService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
 
@WebService(endpointInterface = "porto.WSPortoServer")
public class WSPortoServerImpl implements WSPortoServer {
 
    private IPorto getPortoRMI() throws Exception {
        // Conecta ao registro RMI para obter a instância do serviço existente
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        return (IPorto) registry.lookup("PortoService");
    }

    @Override
    public String relatorio_embarque() {
        try {
            return getPortoRMI().relatorio_embarque();
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao acessar relatório RMI: " + e.getMessage();
        }
    }

    @Override
    public double embarcar(String descricao) {
        try {
            return getPortoRMI().embarcar(descricao);
        } catch (Exception e) {
            e.printStackTrace();
            return -1.0;
        }
    }
}
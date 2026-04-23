package porto;

import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class WSPortoClient {
    public static void main(String[] args) {
        try {
            URL url = new URL("http://localhost:8085/ws/porto?wsdl");
            
            // Qualifiers do serviço (TargetNamespace e Name definidos no WSDL)
            QName qname = new QName("http://porto/", "WSPortoServerImplService");
            
            Service service = Service.create(url, qname);
            WSPortoServer portoWS = service.getPort(WSPortoServer.class);
            
            // Testando a integração
            System.out.println("--- Testando WS Client ---");
            double valor = portoWS.embarcar("Carga via Web Service");
            System.out.println("Resposta Embarque WS: " + valor);
            System.out.println("Relatório WS: " + portoWS.relatorio_embarque());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
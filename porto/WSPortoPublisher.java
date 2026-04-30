package porto;

import javax.xml.ws.Endpoint;

public class WSPortoPublisher {
    public static void main(String[] args) {
        // Publica o serviço SOAP em uma URL acessível
        String url = "http://localhost:1099/WSPorto";
        Endpoint.publish(url, new WSPortoServerImpl());
        
        System.out.println("Web Service Porto publicado em: " + url);
        System.out.println("WSDL disponível em: " + url + "?wsdl");
    }
}
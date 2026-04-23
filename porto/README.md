## Alunos:
- Marco Antonio Reche Rigon
- Maria Eduarda Campos
- Matheus Roberto da Silva Correa

## Compilar
Antes de compilar, volte para a pasta raiz do projeto (```cd ..```) e então execute
```
javac porto/*.java
```
Não esqueça de verificar se está usando Java 8

## Executar
1. Inicie o servidor RMI
    ```
    java porto.Porto
    ```

2. Inicie o servidor SOAP
    ```
    java porto.WSPortoPublisher
    ```
3. Inicie o cliente por WS
    ```
    java porto.WSPortoCliente
    ```

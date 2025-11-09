# Sistema Bancário

Este projeto busca implementar um sistema bancário com as funcionalidades básicas que um sistema deste porte necessita, como cadastro de clientes e contas, depósitos de saques, 
além do relatório dessas contas e a aplicação do rendimento nessas contas. Este projeto foi feito com alicerçado nas boas práticas de programação presentes na literatura, com foco
no Clean Code e nos princípios do SOLID.

# Estrutura do Projeto
```
├── bin
├── data/ 
├── src/br/com/banco 
└── README.md
```

# Como executar
1. Clone o repositório:
   ```
   git clone https://github.com/Jeremiasp7/sistema-bancario.git
   cd sistema-bancario
   ```
2. Compilação do código fonte:
   ```
   mkdir bin
   cd src
   javac -d ../bin br/com/banco/*.java br/com/banco/model/*.java br/com/banco/repository/*.java br/com/banco/service/*.java
   cd ..
   ```
3. Execute o programa:
   ```
   java -cp bin br.com.banco.Main
   ``` 

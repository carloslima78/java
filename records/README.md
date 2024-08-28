# Records em Java

## Introdução

Com o lançamento do JDK 16, o Java introduziu uma nova feature chamada Records. Essa funcionalidade foi criada para facilitar a definição de objetos imutáveis, que geralmente podem ser usados para a transferência de dados, como DTOs (Data Transfer Objects), de maneira mais simples e eficaz.

Os *Records* são ideais para quando você precisa de uma estrutura que apenas contenha dados. A ideia é evitar o código boilerplate, como a criação de construtores, `getters`, `toString`, `equals` e `hashCode`, que normalmente são necessários em classes tradicionais.

## Características dos Records

Um Record em Java:

- Estende implicitamente a classe java.lang.Record.
- É declarado como `final` por padrão, o que significa que não pode ser estendido.
- Pode implementar interfaces, permitindo que seja utilizado em cenários onde a implementação de contratos é necessária.
- Não possui a necessidade de criar getters, construtores e métodos de comparação.
- Suporta validações de entrada, utilizando as anotações do pacote *Jakarta.Validation**, como `@NotBlank`, `@NotNull`, entre outras.

## Casos de Uso

Os Records são particularmente indicados para representar DTOs ou até mesmo os objetos de valor (Value Objects). 

## Record vs Classe

Comparando Records com classes tradicionais, os Records se destacam por:

- Gerarem automaticamente os métodos getters, toString, hashCode e equals.
- Serem imutáveis, pois seus atributos são final por padrão.
- Não suportarem atributos não estáticos, o que reforça sua natureza imutável.

## Matriz de Decisão: Classe vs Record

Aqui está uma matriz de decisão que pode ajudar a escolher entre uma classe tradicional ou um Record em Java, com base em alguns critérios importantes:

| Critério | Quando Usar Classe | Quando Usar Record |
| -------- | -------- | -------- |
| Imutabilidade | Se você precisar de objetos mutáveis, onde os valores dos atributos possam ser alterados após a criação. | Quando os objetos devem ser imutáveis e os valores não podem ser alterados após a criação. |
| Comportamento e Estado | Se a classe precisa manter estado mutável ou encapsular lógica complexa, além de apenas armazenar dados. | Quando a classe se destina a representar uma estrutura de dados simples sem estado mutável. |
| Herança e Extensibilidade | Se você precisa criar hierarquias de classes ou utilizar herança para estender funcionalidades. | Quando não há necessidade de herança, e a simplicidade e a finalidade são mais importantes. |

## Mão na Massa

### Classe Convencional

Um exemplo de uma classe tradicional em Java para representar os dados de um cliente seria:

```java

public class ClientClass {

    private String name;
    private String email;

    public ClientClass(String name, String email){
        this.name = name;
        this.email = email;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    @Override
    public String toString() {
        return "ClientClass{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

```

### Utilizando um Record

Agora, veja como o mesmo objeto pode ser representado com um Record:

```java

public record ClientRecord(String name, String email) {
}

```

Com essa simples linha, o Java já cuida de criar o construtor, `getters`, `toString`, `hashCode` e `equals` para você.

### Comparando a Utilização

Preenchendo e imprimindo os atributos utilizando uma classe:

```java

public class Main {

    public static void main(String[] args) {
        var clientClass  = new ClientClass("Carlos", "carlos@email.com");
        System.out.println(clientClass);
    }
}

```

Resultado esperado:

```bash
ClientClass{name='Carlos', email='carlos@email.com'}
```

E agora, utilizando um Record:

```java

public class Main {
    public static void main(String[] args) {
        var clientRecord = new ClientRecord("Kelli", "kelli@email.com");
        System.out.println(clientRecord);
    }
}

```

Resultado esperado:

```bash
ClientRecord[name=Kelli, email=kelli@email.com]
```

### Adicionando Métodos Internos

Embora sejam simples, os Records também permitem a adição de métodos internos:

```java

public record ClientRecord(String name, String email) {

    public void printName(){
        System.out.println("Name: " + name);
    }
}

```

Chamando o método `printName` a partir da classe `Main`:

```java

public class Main {
    public static void main(String[] args) {
        var clientRecord = new ClientRecord("Kelli", "kelli@email.com");
        clientRecord.printName();
    }
}

```

Resultado esperado:

```bash
Name: Kelli
```

### Atributos e Métodos Estáticos

Os Records também suportam atributos e métodos estáticos:

```java

public record ClientRecord(String name, String email) {

    public static final String MESSAGE = "PONG";

    public static void printMessage(){
        System.out.println("PING: " + MESSAGE);
    }
}

```

Chamando o método estático, sem a necessidade de instanciar o Record:

```java

public class Main {
    public static void main(String[] args) {
        ClientRecord.printMessage();
    }
}

```

Resultado esperado:

```bash
PING: PONG
```

### Utilizando Listas com Records

Você pode facilmente utilizar Records em listas. Vamos criar uma lista de clientes e imprimir seus nomes utilizando uma expressão lambda do método `forEach`:

```java

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<ClientRecord> clients = new ArrayList<>();

        clients.add(new ClientRecord("João", "joao@email.com"));
        clients.add(new ClientRecord("Maria", "maria@email.com"));

        clients.forEach(client -> System.out.println(client.name()));
    }
}

```

Resultado esperado:

```bash
João
Maria
```

Aqui, estamos usando a expressão lambda `client -> System.out.println(client.name())` dentro do método `forEach`. 

Esse método é parte da interface Iterable e é utilizado para percorrer a lista clients. O lambda, nesse caso, acessa o método name() de cada objeto ClientRecord e imprime o nome na saída padrão.

### Records como Objetos de Valor

Objetos de valor tratam-se de objetos imutáveis que representam atributos ou propriedades específicas no domínio

Os Records se encaixam perfeitamente nesse cenário, pois são imutáveis e já implementam os métodos equals e hashCode, que são essenciais para comparação de objetos de valor.

#### Exemplo de Objeto de Valor

Vamos considerar um objeto de valor que representa um endereço:

```java

public record AddressRecord(String street, String city, String state, String zipCode) {
}

```

Aqui, temos um Record `AddressRecord` que encapsula os detalhes de um endereço. Como é imutável e já possui os métodos `equals`, `hashCode` e `toString`, podemos usá-lo como um objeto de valor em qualquer aplicação.

#### Utilização do Objeto de Valor

Agora, vamos criar dois objetos Address, compará-los e imprimir suas representações textuais:

```java

public class Main {
    public static void main(String[] args) {

        var address1 = new Address("123 Main St", "Springfield", "IL", "62704");
        var address2 = new Address("123 Main St", "Springfield", "IL", "62704");

        System.out.println("Address 1: " + address1);
        System.out.println("Address 2: " + address2);
        System.out.println("Addresses are equal: " + address1.equals(address2));
        System.out.println("Address 1 toString: " + address1.toString());
    }
}

```

Resultado esperado:

```bash

Address 1: Address[street=123 Main St, city=Springfield, state=IL, zipCode=62704]
Address 2: Address[street=123 Main St, city=Springfield, state=IL, zipCode=62704]
Addresses are equal: true
Address 1 toString: Address[street=123 Main St, city=Springfield, state=IL, zipCode=62704]

```

Neste exemplo, `address1` e `address2` são considerados iguais porque todos os seus atributos são iguais. O método `toString` gerado automaticamente também oferece uma representação clara e útil dos dados encapsulados pelo Record, o que é extremamente útil.

## Conclusão

Os Records em Java oferecem uma maneira eficiente de criar classes imutáveis com menos código, ideal para DTOs e objetos de valor. 

Com sua capacidade de gerar automaticamente métodos essenciais e integração com validações, os Records simplificam o desenvolvimento e garantem dados consistentes e claros. 

Adotar Records pode resultar em um código mais limpo e fácil de manter, tornando-os uma escolha valiosa para seus projetos Java.
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.Scanner;

public class Cliente {

    static int id;
    static private Channel channel;
    static ConnectionFactory factory;
    private static Connection connection;


    public static void main(String[] args) throws Exception {


        System.out.println("Informe seu id. Caso novo jogador, somente digite enter:");
        Scanner teclado = new Scanner(System.in);
        factory = new ConnectionFactory();
//        factory.setPort(61613);
//        factory.setHost("localhost");
//        factory.setUsername("guest");
//        factory.setPassword("guest");
        connection = factory.newConnection();
        try {
            id = Integer.parseInt(teclado.next());
        }catch (NumberFormatException n){
            System.out.print("Id incorreto ou inexistente. Iniciando novo jogador...");
            send("inclusão","novo jogador");
            recvID();
            System.out.println(String.format("Você é de ID número: %d",id));
        }

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    recvPlacar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();

        while (true){
            System.out.print("Digite a letra: ");
            String letra = teclado.nextLine();
            send("jogada",String.format("%d;%s",id,letra));
        }



    }

    public static void send(String queue, String message) throws Exception {
        channel = connection.createChannel();


        channel.queueDeclare(queue, true, false, false, null);
        channel.basicPublish("", queue, null, message.getBytes());
        //System.out.println(" [x] Sent '" + message + "'");



    }

    public static void recvID() throws Exception {
        channel = connection.createChannel();


        channel.queueDeclare("id", true, false, false, null);
        //System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            id = Integer.parseInt(message);
            //System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume("id", true, deliverCallback, consumerTag -> { });
    }

    public static void recvPlacar() throws Exception {

        channel = connection.createChannel();

        channel.exchangeDeclare("placar", "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, "placar", "");

        //System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.print(message);
            //System.out.println(" [x] Received '" + message + "'");
        };
        while (true){
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
        }
    }


}

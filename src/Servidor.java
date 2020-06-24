import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Servidor {


    private static final String QUEUE_NAME = " ";
    static private Channel channel;
    static ConnectionFactory factory;
    private static TratarJogo jogo;


    public static void main(String [] args) throws IOException, TimeoutException {

        factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        channel = connection.createChannel();

        jogo = new TratarJogo();

        //thread para ouvir entrada de novos jogadores
        Thread ouvir_inclusao = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    recvInclusão();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            });

        ouvir_inclusao.start();

        Thread ouvir_jogada = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    recv("jogada");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        ouvir_jogada.start();


    }


    public static void send(String queue, String message) throws Exception {


        channel.queueDeclare(queue, true, false, false, null);
        channel.basicPublish("", queue, null, message.getBytes());
        //System.out.println(" [x] Sent '" + message + "'");



    }

    public static void recv(String queue) throws Exception {

        channel.queueDeclare(queue, true, false, false, null);
        //System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            //System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }

    public static void recvInclusão() throws Exception {

        channel.queueDeclare("inclusão", true, false, false, null);
        //System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            //String message = new String(delivery.getBody(), "UTF-8");
            int id = jogo.addJogador();
            try {
                send("id",id + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println(" [x] Received '" + message + "'");
        };
        while (true){
            channel.basicConsume("inclusão", true, deliverCallback, consumerTag -> { });
        }
    }

    public static void recvJogada() throws Exception {

        channel.queueDeclare("jogada", true, false, false, null);
        //System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            String[] jogada = message.split(";");
            jogo.processarRodada(Integer.parseInt(jogada[0]),jogada[1]);
            try {
                sendPlacar();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        while (true){
            channel.basicConsume("jogada", true, deliverCallback, consumerTag -> { });
        }
    }

    public static void sendPlacar() throws Exception {

            channel.exchangeDeclare("placar", "fanout");

            String message = jogo.exibirPlacar();

            channel.basicPublish("placar", "", null, message.getBytes("UTF-8"));
            //System.out.println(" [x] Sent '" + message + "'");

    }


}
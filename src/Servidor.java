import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Servidor {


    private static final String QUEUE_NAME = " ";
    static private Channel channel;
    static ConnectionFactory factory;
    private static TratarJogo jogo;
    private static Connection connection;


    public static void main(String [] args) throws IOException, TimeoutException {

        factory = new ConnectionFactory();
//        factory.setPort(61613);
//        factory.setHost("localhost");
//        factory.setUsername("guest");
//        factory.setPassword("guest");
        factory.setConnectionTimeout(10000);
        connection = factory.newConnection();
        System.out.print("Conexão criada");

        jogo = new TratarJogo();
        System.out.print("Criado jogo");

        //thread para ouvir entrada de novos jogadores
        Thread ouvir_inclusao = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    channel = connection.createChannel();
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
                    channel = connection.createChannel();
                    sendPlacar();
                    recvJogada();
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
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });
    }

    /**
     * Ouve a solicitacao de entrada de novos jogadores
     * @throws Exception
     */
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
//        while (true){
//            channel.basicConsume("inclusão", true, deliverCallback, consumerTag -> { });
//        }

        channel.basicConsume("inclusão", false, "delivery",
                new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag,
                                               Envelope envelope,
                                               AMQP.BasicProperties properties,
                                               byte[] body)
                            throws IOException
                    {
                        String routingKey = envelope.getRoutingKey();
                        String contentType = properties.getContentType();

                        long deliveryTag = envelope.getDeliveryTag();
                        int id = jogo.addJogador();
                        try {
                            send("id",id + "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // (process the message components here ...)
                        channel.basicAck(deliveryTag, false);
                    }
                });
    }

    /**
     * Ouve novas jogadas na rede e envia o placarr atualizado em resposta
     * @throws Exception
     */
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

    /**
     * Envia o placar do jogo atualizado
     * @throws Exception
     */
    public static void sendPlacar() throws Exception {

            channel.exchangeDeclare("placar", "fanout");

            String message = jogo.exibirPlacar();

            channel.basicPublish("placar", "", null, message.getBytes("UTF-8"));
            //System.out.println(" [x] Sent '" + message + "'");

    }


}
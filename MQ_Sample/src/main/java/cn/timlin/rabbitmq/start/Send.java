package cn.timlin.rabbitmq.start;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Sender The sender will connect to RabbitMQ, send a single message, then exit.
 * 
 * @author linyu
 *
 */
public class Send {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args) throws IOException, TimeoutException {
		// create a connection to the server
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		//create a channel
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    String message = "Hello World-2!";
	    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
	    System.out.println(" [x] Sent '" + message + "'");
	    //close 
	    channel.close();
	    connection.close();

	}

}

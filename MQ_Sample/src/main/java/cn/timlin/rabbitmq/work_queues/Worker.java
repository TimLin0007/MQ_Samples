package cn.timlin.rabbitmq.work_queues;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class Worker {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] argv) throws Exception {
		// create a connection to the server
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		// create a channel
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		// close
		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope,
					AMQP.BasicProperties properties, byte[] body)
					throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
				try {
					doWork(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println(" [x] Done");
				}
			}
		};
		/*
		 * String com.rabbitmq.client.Channel.basicConsume(String arg0, boolean arg1, Consumer arg2) throws IOException
		 * arg0：消息队列的名字
		 * arg1：
		 *       true 为true时消息队列把任务下发到执行者之后就会从队列中删除此任务，
		 * 		      因此无法保证任务被顺利的执行。
		 * 		 false 表示采取的消息防丢失的模式。即消息队列发送了任务到执行者之后，
		 * 			   收到执行者执行成功的反馈后才会从消息队列之中删除。
		 */
		boolean autoAck = false; // acknowledgment is covered below
		channel.basicConsume(QUEUE_NAME, autoAck, consumer);
	}

	private static void doWork(String task) throws InterruptedException {
	    for (char ch: task.toCharArray()) {
	        if (ch == '.') Thread.sleep(1000);
	    }
	}

}

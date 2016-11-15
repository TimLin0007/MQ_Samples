package cn.timlin.rabbitmq.work_queues;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * 复制Send.java的内容，然后做修改
 * 
 * @author linyu
 *
 */
public class NewTask {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args) throws IOException, TimeoutException {
		// create a connection to the server
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		// create a channel
		/*
		 * DeclareOk com.rabbitmq.client.Channel.queueDeclare(String arg0,
		 * boolean arg1, boolean arg2, boolean arg3, Map<String, Object> arg4)
		 * throws IOException
		 * 
		 * 注：队列创建之后将不允许再修改队列的属性
		 * 
		 * arg1:设置为true会开启队列退出后重新回到推出前的状态，保护队列之中未执行的任务继续执行
		 * 	   
		 */
		boolean durable = true;
		channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
		
		String[] strs = {
				"Hello........",
				"ABC..5" };
		String message = getMessage(strs);
		/*
		 * void com.rabbitmq.client.Channel.basicPublish(String arg0, String arg1, BasicProperties arg2, byte[] arg3) throws IOException
		 * 
		 * arg2 ：设置为MessageProperties.PERSISTENT_TEXT_PLAIN则开启了队列退出后的保护机制
		 * 
		 */
		channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");

		// close
		channel.close();
		connection.close();

	}

	private static String getMessage(String[] strings) {
		if (strings.length < 1)
			return "Hello World!";
		return joinStrings(strings, " ");
	}

	private static String joinStrings(String[] strings, String delimiter) {
		int length = strings.length;
		if (length == 0)
			return "";
		StringBuilder words = new StringBuilder(strings[0]);
		for (int i = 1; i < length; i++) {
			words.append(delimiter).append(strings[i]);
		}
		return words.toString();
	}

}

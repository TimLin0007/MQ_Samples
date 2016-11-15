package cn.timlin.rabbitmq.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLogDirect {

	private static final String EXCHANGE_NAME = "direct_logs";

	public static void main(String[] argv) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		// 修改exchange的类型为direct
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");

		String severity = getSeverity(argv);
		String message = getMessage(argv);

		/*
		 * void com.rabbitmq.client.Channel.basicPublish(String arg0, String
		 * arg1, BasicProperties arg2, byte[] arg3) throws IOException
		 * 
		 * arg1 : 为routing key的标示，即为向目标表示的key的客户队列发送此信息
		 */
		channel.basicPublish(EXCHANGE_NAME, severity, null,
				message.getBytes("UTF-8"));
		System.out.println(" [x] Sent '" + severity + "':'" + message + "'");

		channel.close();
		connection.close();
	}

	private static String getSeverity(String[] strings) {
		if (strings.length < 1)
			return "info";
		return strings[0];
	}

	private static String getMessage(String[] strings) {
		if (strings.length < 2)
			return "Hello World!";
		return joinStrings(strings, " ", 1);
	}

	private static String joinStrings(String[] strings, String delimiter,
			int startIndex) {
		int length = strings.length;
		if (length == 0)
			return "";
		if (length < startIndex)
			return "";
		StringBuilder words = new StringBuilder(strings[startIndex]);
		for (int i = startIndex + 1; i < length; i++) {
			words.append(delimiter).append(strings[i]);
		}
		return words.toString();
	}

}

//package robin.discordbot2;
//
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
//import robin.discordbot2.config.botBean;
//
//import javax.security.auth.login.LoginException;
//
//
//@SpringBootApplication
//
//public class discordbotapplication {
//	public static void main(String[] args) {
//		// 设置HTTP代理
//		System.setProperty("http.proxyHost", "127.0.0.1");
//		System.setProperty("http.proxyPort", "7892");
//
//		// 设置HTTPS代理
//		System.setProperty("https.proxyHost", "127.0.0.1");
//		System.setProperty("https.proxyPort", "7892");
//		try {
//			botBean botBean = new botBean();
//		} catch (LoginException e) {
//			System.out.println("invalid");
//		}
//		SpringApplication.run(discordbotapplication.class, args);
//	}
//}



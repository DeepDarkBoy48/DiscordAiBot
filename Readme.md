# 代理设置
```java
        //这行代码设置了一个 HTTP 代理：
        Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1",7892));
        //这里使用了 OkHttpClient 的构建器来设置 HTTP 请求代理：
        // 所有通过这个客户端发出的 HTTP 请求都会通过 127.0.0.1:7892 代理。
        OkHttpClient.Builder builder2 = new OkHttpClient.Builder().proxy(proxy);
        //WebSocketFactory 实例，它用于构建 WebSocket 连接,也要设置代理
        WebSocketFactory factory = new WebSocketFactory();
        factory.getProxySettings().setHost("127.0.0.1").setPort(7892);
```

@PostConstruct 方法中执行代码：把 bot 的初始化代码放到 @PostConstruct 注解的方法中，以确保在 Spring Boot 启动后执行




# html2img
`HtmlImageGenerator` 是 `html2image` 库提供的一个类，用于将 HTML 转换为图像。这个类提供了一些方法来配置和生成图像。以下是 `HtmlImageGenerator` 类的常用方法及其讲解：

## 常用方法

1. **`loadHtml(String html)`**
    - **描述**：加载要转换的 HTML 内容。
    - **参数**：`html` - 要转换的 HTML 内容字符串。
    - **用法**：
      ```java
      imageGenerator.loadHtml("<html><body><h1>Hello World</h1></body></html>");
      ```

2. **`saveAsImage(String fileName)`**
    - **描述**：将当前加载的 HTML 内容保存为图像文件。
    - **参数**：`fileName` - 保存图像的文件名（包括路径和扩展名，如 "output.png"）。
    - **用法**：
      ```java
      imageGenerator.saveAsImage("output.png");
      ```

3. **`saveAsImage(String fileName, int width, int height)`**
    - **描述**：将当前加载的 HTML 内容保存为指定尺寸的图像文件。
    - **参数**：
        - `fileName` - 保存图像的文件名。
        - `width` - 图像的宽度。
        - `height` - 图像的高度。
    - **用法**：
      ```java
      imageGenerator.saveAsImage("output.png", 800, 600);
      ```

4. **`saveAsHtmlWithMap(String fileName, String mapFileName)`**
    - **描述**：将 HTML 内容保存为 HTML 文件，同时保存图像映射信息（用于后续图像的生成）。
    - **参数**：
        - `fileName` - 保存 HTML 内容的文件名。
        - `mapFileName` - 保存图像的文件名。
    - **用法**：
      ```java
      imageGenerator.saveAsHtmlWithMap("output.html", "output.png");
      ```

5. **`setViewportSize(int width, int height)`**
    - **描述**：设置视口的尺寸（宽度和高度）。这影响生成图像的尺寸。
    - **参数**：
        - `width` - 视口的宽度。
        - `height` - 视口的高度。
    - **用法**：
      ```java
      imageGenerator.setViewportSize(1024, 768);
      ```

6. **`getImage()`**
    - **描述**：获取当前加载的 HTML 内容生成的图像对象。返回一个 `BufferedImage` 对象。
    - **返回**：`BufferedImage` - 生成的图像。
    - **用法**：
      ```java
      BufferedImage image = imageGenerator.getImage();
      ```

7. **`setBackgroundColor(Color color)`**
    - **描述**：设置图像的背景颜色。
    - **参数**：`color` - 背景颜色（`Color` 对象）。
    - **用法**：
      ```java
      imageGenerator.setBackgroundColor(Color.WHITE);
      ```

8. **`setCssMediaType(String mediaType)`**
    - **描述**：设置 CSS 媒体类型。可以是 `screen`、`print` 等，决定 CSS 样式的应用。
    - **参数**：`mediaType` - 媒体类型字符串。
    - **用法**：
      ```java
      imageGenerator.setCssMediaType("print");
      ```

9. **`setJavaScriptEnabled(boolean enabled)`**
    - **描述**：启用或禁用 JavaScript 执行。
    - **参数**：`enabled` - 是否启用 JavaScript。
    - **用法**：
      ```java
      imageGenerator.setJavaScriptEnabled(true);
      ```

## 示例代码

以下是如何使用这些方法的示例：

```java
import gui.ava.html.image.generator.HtmlImageGenerator;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class HtmlToImageExample {
    
    public static void main(String[] args) {
        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
        
        // 加载 HTML 内容
        String htmlContent = "<html><body><h1>Hello, World!</h1></body></html>";
        imageGenerator.loadHtml(htmlContent);

        // 设置视口大小
        imageGenerator.setViewportSize(800, 600);

        // 设置背景颜色
        imageGenerator.setBackgroundColor(Color.LIGHT_GRAY);

        // 保存图像到文件
        imageGenerator.saveAsImage("output.png");

        // 获取生成的图像对象
        BufferedImage image = imageGenerator.getImage();
        
        System.out.println("HTML content converted to image and saved as output.png");
    }
}
```

## 总结

- **`loadHtml`** 用于加载 HTML 内容。
- **`saveAsImage`** 保存生成的图像。
- **`saveAsHtmlWithMap`** 保存 HTML 内容及图像映射信息。
- **`setViewportSize`** 设置视口大小。
- **`getImage`** 获取生成的图像对象。
- **`setBackgroundColor`** 设置图像背景颜色。
- **`setCssMediaType`** 设置 CSS 媒体类型。
- **`setJavaScriptEnabled`** 启用或禁用 JavaScript。

这些方法可以帮助你根据需求自定义和生成 HTML 到图像的转换。
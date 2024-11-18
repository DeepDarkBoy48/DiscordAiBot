package robin.discordbot2.utils;

import gui.ava.html.image.generator.HtmlImageGenerator;

import java.awt.*;

public class html2picture {
    static public void htmlTransferImage(String htmlContent){
        // 实例化 HtmlImageGenerator
        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
        // 加载 HTML 内容
        imageGenerator.setSize(new Dimension(100,100));
        imageGenerator.loadHtml(htmlContent);
        // 保存图片到指定路径
        imageGenerator.saveAsImage("output.png");

    }
}

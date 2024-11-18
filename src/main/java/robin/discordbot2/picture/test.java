package robin.discordbot2.picture;

import gui.ava.html.image.generator.HtmlImageGenerator;

import java.awt.*;

public class test {
    static class html {
        public static void html() {
            System.out.println("hello");
        }

        public static void main(String[] args) {
            html();
        }
    }


    static class HtmlToImageExample {

        public static void main(String[] args) {
            // HTML 内容
            // HTML 表格代码
            String htmlContent = """
                    <!DOCTYPE html>
                           <html lang="zh-CN">
                           <head>
                               <meta charset="UTF-8">
                               <meta name="viewport" content="width=device-width, initial-scale=1.0">
                               <title>Canvas 小猫</title>
                               <style>
                                   body {
                                       font-family: Arial, sans-serif;
                                       text-align: center;
                                       background-color: #f0f0f0;
                                   }
                                   canvas {
                                       border: 1px solid black;
                                       max-width: 100%;
                                       margin-top: 20px;
                                   }
                               </style>
                           </head>
                           <body>
                               <h1>Canvas 小猫</h1>
                               <canvas id="catCanvas" width="500" height="500"></canvas>
                           
                               <script>
                                   // 获取 Canvas 和 2D 绘图上下文
                                   const canvas = document.getElementById('catCanvas');
                                   const ctx = canvas.getContext('2d');
                           
                                   // 绘制小猫的头部
                                   function drawCat() {
                                       // 绘制头部（圆形）
                                       ctx.beginPath();
                                       ctx.arc(250, 250, 100, 0, Math.PI * 2, true); // x, y, 半径, 起始角度, 结束角度
                                       ctx.fillStyle = "#FFCC99";
                                       ctx.fill();
                                       ctx.stroke();
                           
                                       // 绘制左耳朵（三角形）
                                       ctx.beginPath();
                                       ctx.moveTo(180, 150);
                                       ctx.lineTo(220, 200);
                                       ctx.lineTo(150, 220);
                                       ctx.closePath();
                                       ctx.fillStyle = "#FFCC99";
                                       ctx.fill();
                                       ctx.stroke();
                           
                                       // 绘制右耳朵（三角形）
                                       ctx.beginPath();
                                       ctx.moveTo(320, 200);
                                       ctx.lineTo(280, 150);
                                       ctx.lineTo(350, 220);
                                       ctx.closePath();
                                       ctx.fillStyle = "#FFCC99";
                                       ctx.fill();
                                       ctx.stroke();
                           
                                       // 绘制左眼睛
                                       ctx.beginPath();
                                       ctx.arc(220, 230, 15, 0, Math.PI * 2, true);  // x, y, 半径
                                       ctx.fillStyle = "black";
                                       ctx.fill();
                           
                                       // 绘制右眼睛
                                       ctx.beginPath();
                                       ctx.arc(280, 230, 15, 0, Math.PI * 2, true);  // x, y, 半径
                                       ctx.fillStyle = "black";
                                       ctx.fill();
                           
                                       // 绘制鼻子（三角形）
                                       ctx.beginPath();
                                       ctx.moveTo(250, 260);
                                       ctx.lineTo(240, 280);
                                       ctx.lineTo(260, 280);
                                       ctx.closePath();
                                       ctx.fillStyle = "pink";
                                       ctx.fill();
                                       ctx.stroke();
                           
                                       // 绘制嘴巴
                                       ctx.beginPath();
                                       ctx.arc(250, 290, 20, 0, Math.PI, false);  // 半圆
                                       ctx.stroke();
                           
                                       // 绘制胡须
                                       // 左边胡须
                                       ctx.beginPath();
                                       ctx.moveTo(190, 280);
                                       ctx.lineTo(230, 280);
                                       ctx.moveTo(190, 290);
                                       ctx.lineTo(230, 295);
                                       ctx.moveTo(190, 300);
                                       ctx.lineTo(230, 310);
                                       ctx.stroke();
                           
                                       // 右边胡须
                                       ctx.beginPath();
                                       ctx.moveTo(270, 280);
                                       ctx.lineTo(310, 280);
                                       ctx.moveTo(270, 295);
                                       ctx.lineTo(310, 290);
                                       ctx.moveTo(270, 310);
                                       ctx.lineTo(310, 300);
                                       ctx.stroke();
                                   }
                           
                                   // 执行绘图
                                   drawCat();
                               </script>
                           </body>
                           </html>
                           
                    """;
            // 实例化 HtmlImageGenerator
            HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
            // 加载 HTML 内容
            imageGenerator.setSize(new Dimension(100, 100));
            imageGenerator.loadHtml(htmlContent);

            // 保存图片到指定路径
            imageGenerator.saveAsImage("output.png");

            // 如果需要，也可以保存为 HTML 文件
            imageGenerator.saveAsHtmlWithMap("output.html", "output.png");

            System.out.println("HTML converted to image and saved as output.png");
        }
    }
}

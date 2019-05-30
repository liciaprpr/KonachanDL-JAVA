package com.konachan;

import com.konachan.bean.Picture;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KonachanAuto {

    /**
     * 保存图片的根目录
     */
    public static String ROOT_URL;
    public static String DEFAULT_URL_WIN = "D:\\KAuto";
    public static String DEFAULT_URL_UNIX = "/Pictures/KAuto";


    public static void main(String[] args) throws Exception {
        try {
            Scanner scanner = new Scanner(System.in);

            //1.输入位置
            rootHomeSetter();
            System.out.println("请输入要保存的位置（直接回车默认为" + ROOT_URL + "）："); 
            String path = scanner.nextLine();
            ROOT_URL = Utils.isBlank(path) ? ROOT_URL : path;
            File file = new File(ROOT_URL);
            if (!file.exists()) {
                file.mkdir();
            }
            if (!File.separator.equals(ROOT_URL.substring(ROOT_URL.length() - 1))) {
                ROOT_URL += File.separator;
            }

            //2.输入Tags
            System.out.println("文件将保存在" + ROOT_URL + "下对应的相关标签文件夹中");
            System.out.println("请输入标签，多个标签以半角空格分隔（直接回车默认搜全部）：");
            String tags = scanner.nextLine();
            tags = Utils.isBlank(tags) ? "" : tags.replaceAll(" ", "%20");

            //3.输入页码（每页100张 * 10）
            System.out.println("每次下载1000张，请保证能访问Konachan，开始下载请直接回车，否则请输入任意字符或直接结束程序...");


            //4.开始下载
            String var = scanner.nextLine();
            System.out.println(Utils.isBlank(var));
            int page = 1;
            List<DownloadThread> threads = null;
            do {
                threads = new ArrayList();
                for (int i = page++ * 10 - 9; i <= page++ * 10 + 10; i++) {
                    String xmlBody = getKonachanXMLFromPageNo(i, tags);
                    Vector<String> imgURLs = getIMGPathByXML(xmlBody);
                    if (imgURLs == null || imgURLs.size() == 0) {
                        System.out.println("无图片可下载，程序结束");
                        return;
                    }
                    DownloadThread thread = new DownloadThread(imgURLs, tags, "线程" + i % 10);
                    threads.add(thread);
                    thread.start();
                }
                standBy(threads);
                threads.clear();
                System.out.println("继续下载请直接回车，否则请输入任意字符或直接结束程序...");
                var = scanner.nextLine();
            } while (Utils.isBlank(var));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    //类Unix系统的情况下获得userhome
    public static void rootHomeSetter() {
        boolean isUnix = "/".equals(File.separator);
        if (isUnix) {
            ROOT_URL = DEFAULT_URL_UNIX;
            ROOT_URL = System.getProperty("user.home") + ROOT_URL;
        } else {
            ROOT_URL = DEFAULT_URL_WIN;
        }
    }


    //通过Page页获取Konachan的XML
    private static String getKonachanXMLFromPageNo(int pageNo, String tags) throws Exception {
        String xmlBody = null;
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            headers.put("Accept-Encoding", "gzip, deflate");
            headers.put("Accept-Language", "zh-CN,zh;q=0.9,zh-TW;q=0.8,ja;q=0.7");
            headers.put("Host", "konachan.com");
            headers.put("Proxy-Connection", "keep-alive");
            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36");

            String tagParam = (tags == null ? "" : ("&tags=" + tags));
            String requestURL = "http://konachan.com/post.xml?limit=100" + tagParam + "&page=" + pageNo;
            xmlBody = HttpClient.httpClient(requestURL, null, null, "utf-8", headers);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return xmlBody;
    }

    //通过XML获取图片地址
    private static Vector<String> getIMGPathByXML(String xmlStr) throws Exception {
        Vector<String> list = new Vector();

        Document document = DocumentHelper.parseText(xmlStr);
        Element root = document.getRootElement();
        List<Element> elements = root.elements();
        for (Iterator<Element> it = elements.iterator(); it.hasNext();) {
            Element element = it.next();
            List<Attribute> attributes = element.attributes();
            Picture p = new Picture();
            for (int i = 0; i < attributes.size(); i++) {
                Attribute attribute = attributes.get(i);
                if ("file_url".equals(attribute.getName())) {
                    list.add(attribute.getText());
                }

                //得到类对象
                Class userCla = (Class) p.getClass();

                /*
                 * 得到类中的所有属性集合
                 */
                Field[] fs = userCla.getDeclaredFields();
                f1:
                for (int j = 0; j < fs.length; j++) {
                    Field f = fs[j];
                    f.setAccessible(true);
                    String type = f.getType().toString();
                    String fieldName = f.getName();
                    String attributeName = attribute.getName();
                    if ("id".equals(attributeName) && "kid".equals(fieldName)) {
                        f.set(p, attribute.getText());
                        break f1;
                    } else if (fieldName.toLowerCase().equals(attributeName) && !"id".equals(attributeName)) {
                        if (type.endsWith("String")) {
                            f.set(p, attribute.getText());
                        } else if (type.endsWith("int") || type.endsWith("Integer")) {
                            f.set(p, Integer.parseInt(attribute.getText()));       //给属性设值
                        }
                        break f1;
                    }
                }
            }

            System.out.println("P:" + p);
        }

        return list;
    }

    private synchronized static void standBy(List<DownloadThread> threads) {
        int aliveNum = 0;
        while (true) {
            for (DownloadThread thread : threads) {
                int var = thread.isAlive() ? 1 : 0;
                aliveNum += var;
            }
            if (aliveNum == 0) {
                break;
            }
        }
    }

}

class DownloadThread extends Thread {
    private Vector<String> imgURLs;
    private String tags;
    private String threadName;

    public DownloadThread(Vector<String> imgURLs, String tags, String threadName) {
        this.imgURLs = imgURLs;
        this.tags = tags;
        this.threadName = threadName;
    }

    @Override
    public void run() {
        try {
            download(imgURLs, tags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void download(Vector<String> downloadList, String tags){
        // 线程池
        ExecutorService pool = null;
        HttpURLConnection connection = null;
        //循环下载
        try {
            //记录开始时间，每隔一分钟重新连接
//            long startTime = System.currentTimeMillis();
            for (int i = 0; i < downloadList.size(); i++) {
                pool = Executors.newCachedThreadPool();
                final String url = downloadList.get(i);
                String filename = getFilename(downloadList.get(i));
                System.out.println(threadName + "正在下载第" + (i + 1) + "个文件，地址：" + url);
                File f = new File(KonachanAuto.ROOT_URL + URLDecoder.decode(filename,"UTF-8"));
                if (f.exists()) {
                    continue;
                }
                Future<HttpURLConnection> future = pool.submit(new Callable<HttpURLConnection>(){
                    @Override
                    public HttpURLConnection call() throws Exception {
                        HttpURLConnection connection = null;
                        connection = (HttpURLConnection) new URL(url).openConnection();
                        connection.setConnectTimeout(10000);//连接超时时间
                        connection.setReadTimeout(10000);// 读取超时时间
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setRequestMethod("GET");
                        //断点续连,每次要算出range的范围,请参考Http 1.1协议
                        //connection.setRequestProperty("Range", "bytes=0");
                        connection.connect();
                        return connection;
                    }
                });
                connection = future.get();
//                System.out.println("下载完成.响应码:"+ connection.getResponseCode());
                // 写入文件
                writeFile(new BufferedInputStream(connection.getInputStream()), URLDecoder.decode(filename,"UTF-8"), tags);

//                if (System.currentTimeMillis() - startTime > 1000L * 60) {
//                    if (null != connection)
//                        connection.disconnect();
//                    if (null != pool)
//                        pool.shutdown();
//                    startTime = System.currentTimeMillis();
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != connection)
                connection.disconnect();
            if (null != pool)
                pool.shutdown();
        }
    }

    static String getFilename(String url){
        return ("".equals(url) || null == url) ? "" : url.substring(url.lastIndexOf("/") + 1,url.length());
    }

    /**
     * 写入文件
     */
    static void writeFile(BufferedInputStream bufferedInputStream,String filename, String tags){
        if (tags == null || "".equals(tags.trim())) {
            tags = "Default";
        } else {
            tags = tags.replaceAll("%20", " ");
        }

        //创建本地文件
        File destfileFile = new File(KonachanAuto.ROOT_URL + tags + File.separator + filename);
        if (destfileFile.exists()) {
            System.out.println("文件已存在，跳过");
            return;
        }
        if (!destfileFile.getParentFile().exists()) {
            destfileFile.getParentFile().mkdir();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(destfileFile);
            byte[] b = new byte[10240];
            int len = 0;
            // 写入文件
//            System.out.println("开始写入本地文件.");
            while ((len = bufferedInputStream.read(b, 0, b.length)) != -1) {
                fileOutputStream.write(b, 0, len);
            }
            System.out.println("写入本地文件完成.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fileOutputStream) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                if (null != bufferedInputStream)
                    bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
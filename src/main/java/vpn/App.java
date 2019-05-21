package vpn;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        String ip = "192.168.1.70";
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (isConnect(ip)) {
                    System.out.println("网络状态："+"网络能ping通");
                } else {
                    System.out.println("网络状态："+"网络ping不通");
                }
            }
        };
        Timer timer = new Timer();
        long delay = 0;
        long intevalPeriod = 60 * 1000;
        // schedules the task to be run in an interval
        timer.scheduleAtFixedRate(task, delay, intevalPeriod);
    }

    /**
     * 根据ip判断当前ip是否能够ping通
     *  
     *
     * @param ip
     * @return
     */
    public static boolean isConnect(String ip) {
        boolean bool = false;
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("ping " + ip);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
// 优化速度
                if (line.indexOf("请求超时") >= 0) {
// System.out.println(ip + "网络断开，时间 " + new Date());
                    return false;
                }
            }
            is.close();
            isr.close();
            br.close();


            if (null != sb && !sb.toString().equals("")) {
                if (sb.toString().indexOf("TTL") > 0) {
// 网络畅通
// System.out.println(ip + "网络正常 ，时间" + new Date());
                    bool = true;
                } else {
// 网络不畅通
// System.out.println(ip + "网络断开，时间 " + new Date());
                    bool = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }
}

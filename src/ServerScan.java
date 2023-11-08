
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;



public class ServerScan {
    private int minPort = 1;
    private int maxPort = 1024;
    private String host = "192.168.1.100";
    private int timeout = 100;

    public ServerScan(int minPort, int maxPort, String host, int timeout) {
        this.minPort = minPort;
        this.maxPort = maxPort;
        this.host = host;
        this.timeout = timeout;
    }

    public ServerScan() {

    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMinPort() {
        return minPort;
    }

    public void setMinPort(int minPort) {
        this.minPort = minPort;
    }

    public int getMaxPort() {
        return maxPort;
    }

    public void setMaxPort(int maxPort) {
        this.maxPort = maxPort;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "ScanServerMain{" +
                "minPort=" + minPort +
                ", maxPort=" + maxPort +
                ", host='" + host + '\'' +
                ", timeout=" + timeout +
                '}';
    }

    public List<Integer> scan() {
        try {
            InetAddress ia = InetAddress.getByName(getHost());
            return scan(ia);
        } catch (IOException exception) {
            return null;
        }
    }

    List<Integer> scan(InetAddress inetAddress) {
        List<Integer> openPortsList = new ArrayList<Integer>(1);
        System.out.println("Сканируем порт: ");
        for (int port = minPort; port <= maxPort; port++) {
            System.out.print(port);
            try {
                InetSocketAddress isa = new InetSocketAddress(inetAddress,port);
                Socket socket = new Socket();
                socket.connect(isa,timeout);
                System.out.println("Открыто");
                openPortsList.add(port);
                socket.close();
            } catch (IOException exception) {
                System.out.println("Ошибка");
            }
        }
        return openPortsList;
    }
    public static void main(String[] args) {
        if (args.length < 1) {
            usage();
            return;
        }

        String host = args[0];
        System.out.println("Сканируем адрес хостинга "+host);

        ServerScan scanServer = new ServerScan();

        if (args.length==2) {
            if (args[1].indexOf("-")>-1) {

                String[] ports = args[1].split("-");
                try {
                    int minPort = Integer.parseInt(ports[0]);
                    int maxPort = Integer.parseInt(ports[1]);
                    scanServer.setMinPort(minPort);
                    scanServer.setMaxPort(maxPort);
                } catch (NumberFormatException numberFormatException) {
                    System.out.println("Внимание!Неправильный адрес порта");
                    return;
                }
            } else {

                try {
                    int port = Integer.parseInt(args[1]);
                    scanServer.setMinPort(port);
                    scanServer.setMaxPort(port);
                } catch (NumberFormatException numberFormatException) {
                    System.out.println("Внимание! Неправильный адрес порта");
                    return;
                }
            }
        }

        scanServer.setHost(host);
        List<Integer> openPortsList = scanServer.scan();
        if (openPortsList != null) {
            if (openPortsList.size() >0) {
                System.out.println("Список полученных портов :");
                for (Integer openedPort : openPortsList) {
                    System.out.println(openedPort);
                }
            } else {
                System.out.println("Порт не открыт !");
            }
        } else {
            System.out.println("Ошибка!");
        }
    }

    static void usage() {
        System.out.println(" Проверяем работу сканера сети : ");
        System.out.println("Сканируются порты 192.168.1.100 1-1024");


    }
}


package io.skalogs.skaetl.generator.secuRules;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static java.util.UUID.randomUUID;
import static org.apache.commons.lang.RandomStringUtils.randomNumeric;

@Component
@Slf4j
public class UtilsProxy {

    private final UtilsSecu utilsSecu;
    private static Random RANDOM = new Random();
    public UtilsProxy(UtilsSecu utilsSecu){
        this.utilsSecu = utilsSecu;
    }

    public void generateData(int minute) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date newDate = utilsSecu.addMinutesAndSecondsToTime(minute, RANDOM.nextInt(50), new Date());
        ClientData client = utilsSecu.getClient();
        RequestGen rq = randomRequest(client.ipClient);
        utilsSecu.sendToKafka("proxy", HTTPProxy.builder()
                .componentProxy("artica")
                .cookieSession(rq.cookieSession)
                .hostname(client.hostname)
                .proxyIp("123.15.18."+RANDOM.nextInt(3))
                .remoteIp(rq.remoteIp)
                .sizeRequest(rq.sizeRequest)
                .sizeResponse(rq.sizeResponse)
                .timeConnexionRequest(RANDOM.nextInt(100 - 20) + 20)
                .timeGlobalRequest(RANDOM.nextInt(4000 - (RANDOM.nextInt(100 - 20) + 20)) + RANDOM.nextInt(100 - 20) + 20)
                .timestamp(df.format(newDate))
                .typeHTTP(rq.typeHTTP)
                .uri(rq.uri)
                .urlHost(rq.urlHost)
                .urlMethod(rq.urlMethod)
                .urlPort(rq.urlPort)
                .urlStatus(rq.urlStatus)
                .user(client.username)
                .userAgent(rq.userAgent)
                .build());
    }

    private RequestGen randomRequest(String remoteIp) {
        String jsessionid = "JSESSIONID=" + randomUUID();
        int rand = RANDOM.nextInt(4);
        if(rand == 3) {
            return randomPutRequest(jsessionid, remoteIp);
        }
        return randomGETRequest(jsessionid, remoteIp);
    }

    private RequestGen randomGETRequest(String jsessionid, String rip) {
        return RequestGen.builder()
                .cookieSession(jsessionid)
                .remoteIp(rip)
                .sizeRequest(0)
                .sizeResponse(RANDOM.nextInt(3200 - 80) + 80)
                .typeHTTP("https")
                .urlHost(randomHost())
                .urlPort(randomPort())
                .urlMethod("GET")
                .urlStatus(randomStatus())
                .userAgent("")
                .uri(randomUri())
                .build();
    }

    private RequestGen randomPutRequest(String jsessionid, String rip) {
        return RequestGen.builder()
                .cookieSession("JSESSIONID=" + jsessionid)
                .remoteIp(rip)
                .sizeRequest(RANDOM.nextInt(1200 - 20) + 20)
                .sizeResponse(RANDOM.nextInt(60 - 10) + 10)
                .typeHTTP("https")
                .urlHost(randomHost())
                .urlPort(randomPort())
                .urlMethod(randomMethod())
                .urlStatus(randomStatus())
                .uri("/")
                .userAgent("")
                .build();
    }

    public String randomHost() {
        return random(tabHost);
    }

    public String randomIntranetHost() {
        return random(tabIntranetHost);
    }

    public String randomUri() {
        return random(tabUri);
    }

    public String randomMethod() {
        return random(tabMethod);
    }

    public int randomStatus() {
        return random(tabStatus);
    }

    public int randomPort() {
        return random(tabPort);
    }

    private String[] tabHost = new String[]{
            "www.google.fr",
            "www.nbc.com",
            "weather.com",
            "vs-business-contract-prod-application.intranet.infra",
            "vs-business-contract-prod-application.intranet.infra",
            "vs-business-contract-prod-application.intranet.infra",
            "vs-business-product-prod-application.intranet.infra",
            "vs-business-product-prod-application.intranet.infra",
            "vs-business-product-prod-application.intranet.infra",
            "vs-business-support-prod-application.intranet.infra",
            "vs-business-support-prod-application.intranet.infra",
            "alerting-application.intranet.com",
            "platform.client.support.intranet.com",
            "platform.client.support.intranet.com",
            "platform.client.support.intranet.com",
            "platform.client.support.intranet.com",
            "platform.client.support.intranet.com",
            "mysupport.intranet.com",
            "mypasswport.intranet.com",
            "mypasswport.intranet.com"
    };

    private String[] tabIntranetHost = new String[]{
            "vs-business-contract-prod-application.intranet.infra",
            "vs-business-contract-prod-application.intranet.infra",
            "vs-business-contract-prod-application.intranet.infra",
            "vs-business-product-prod-application.intranet.infra",
            "vs-business-product-prod-application.intranet.infra",
            "vs-business-product-prod-application.intranet.infra",
            "vs-business-support-prod-application.intranet.infra",
            "vs-business-support-prod-application.intranet.infra",
            "alerting-application.intranet.com",
            "platform.client.support.intranet.com",
            "platform.client.support.intranet.com",
            "platform.client.support.intranet.com",
            "platform.client.support.intranet.com",
            "platform.client.support.intranet.com",
            "mysupport.intranet.com",
            "mypasswport.intranet.com",
            "mypasswport.intranet.com"
    };

    private Integer[] tabPort = new Integer[]{
            443,
            80,
            8170,
            8180,
            8180,
            9090,
            9090,
            443,
            443,
            443,
            443,
            443
    };

    private Integer[] tabStatus = new Integer[]{
            200,
            200,
            201,
            202,
            200,
            200,
            201,
            201,
            500,
            502,
            200,
            201
    };

    private String[] tabMethod = new String[]{
            "PUT",
            "PUT",
            "PUT",
            "POST",
            "POST",
            "POST",
            "POST",
            "PUT",
            "PUT",
            "PATCH",
            "PUT",
            "DELETE"
    };

    private String[] tabUri = new String[]{
            "/user/" + randomUUID() + "/findAll",
            "/product/" + randomUUID() + "/findAll",
            "/client/" + randomUUID() + "/deleteAll",
            "/",
            "/index.html",
            "/",
            "/index.html",
            "/contract/" + randomNumeric(10) + "/client/" + randomNumeric(10),
            "/contract/" + randomNumeric(10) + "/client/" + randomNumeric(10),
            "/contract/" + randomNumeric(10) + "/reporting/" + randomNumeric(10),
            "/contract/" + randomNumeric(10) + "/product/" + randomNumeric(10),
            "/user/" + randomUUID() + "/findByUserId",
            "/user/" + randomUUID() + "/findByUserId",
            "/user/" + randomUUID() + "/findByUserId",
            "/product/" + randomNumeric(6) + "/findByOrderName",
            "/product/" + randomNumeric(6) + "/findByOrderName",
            "/product/" + randomNumeric(6) + "/findByOrderName"

    };


    public static  <T> T random(T[] tab) {
        return tab[RANDOM.nextInt(tab.length)];
    }

    public static <T> T random(List<T> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }

}

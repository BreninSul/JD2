package com.gmail.breninsul.jd2.dao.registry;


import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Log4j2
@Repository
public class GetInfoFromKZ implements RegestryDAO{
    private static String home = System.getProperty("user.home");
    private static final String CERT_POST_URL_MAIN = Headers.get("kz.CERT_POST_URL_MAIN");
    private static final String TC = Headers.get("kz.TC");;
    private static final String CERT_POST_URL_FOR_EACH = Headers.get("kz.CERT_POST_URL_FOR_EACH");
    private static final String DECL_POST_URL_FOR_EACH = Headers.get("kz.DECL_POST_URL_FOR_EACH");
    private static final String DECL_POST_URL_MAIN = Headers.get("kz.DECL_POST_URL_MAIN");
    private static final String CERT_PRE_STRING = Headers.get("kz.CERT_PRE_STRING");
    private static final String CERT_POST_STRING = Headers.get("kz.CERT_POST_STRING");
    private static final String DECL_PRE_STRING = Headers.get("kz.DECL_PRE_STRING");
    private static final String DECL_POST_STRING = Headers.get("kz.DECL_POST_STRING");
    private static SimpleDateFormat format = new SimpleDateFormat();

    static {
        format.applyPattern("dd.MM.yyyy");
    }

    /**
     * Searching info about certificate in KZ registry
     *
     * @param searchValue Put here name of the product
     * @return List  -it's a List of List's with cert info, null
     */
    public List getCertificates(String searchValue, int resultSize, int timeOut) throws ServerNotAvailableException {
        try {
            URL regAdress = new URL(CERT_POST_URL_MAIN);
            HttpURLConnection regCon = (HttpURLConnection) regAdress.openConnection();
            timeOut=timeOut*1000;
            regCon.setConnectTimeout(timeOut);
            regCon.setReadTimeout(timeOut);
            //Setting headers for post Reqest
            setHeaders(regCon);
            //Setting body
            String postData = CERT_PRE_STRING + URLEncoder.encode(searchValue, "windows-1251") + CERT_POST_STRING;
            //seding post
            StringBuilder sb = getStringBuilder(regCon, postData);
            String result = sb.toString();
            //Parsing result
            Document infotable = Jsoup.parse(result);
            Elements elements = infotable.getElementsByAttributeValue("width", "800");
            regCon.disconnect();
            return parse(elements, 2, resultSize,timeOut);
        } catch (ServerNotAvailableException e) {
            log.warn("Server is not avilable again",e);
            throw new ServerNotAvailableException("KZ_CERT");
        } catch (Exception e) {
            log.error("Error getting info from reg");
            return new ArrayList();
        }
    }


    private List parse(Elements elements, int power, int resultSize, int timeOut) throws IOException, ServerNotAvailableException {
        Document infotable;
        LocalResultTable localResultTable = new LocalResultTable();
        if (elements.size() < 2) {
            return new ArrayList();
        }
        int counterForResultCycle = 0;
        for (Element element : elements) {
            if (counterForResultCycle >= resultSize) {
                break;
            }
            counterForResultCycle++;
            element = element.getElementsByTag("a").first();
            //Getting cert adress
            StringBuilder sb = new StringBuilder();
            char[] adress = element.attr("href").toString().toCharArray();
            char adresType = adress[29];
            if (power == 2) {
                sb.append(CERT_POST_URL_FOR_EACH).append(adresType).append(TC);
            } else {
                if (power == 1) {
                    sb.append(DECL_POST_URL_FOR_EACH).append(adresType).append(TC);
                }
            }
            for (int i = 33; i < 55; i++) {
                sb.append(adress[i]);
            }
            URL regAdress = new URL(sb.toString());
            //And parsing cert page
            try {
                infotable = Jsoup.parse(regAdress, timeOut);
            } catch (SocketTimeoutException e) {
                log.info("Server is not avilable again",e);
                throw new ServerNotAvailableException("");
            }
            elements = infotable.getElementsByAttributeValue("cellpadding", "2");
            element = elements.get(2);
            elements = element.getElementsByAttributeValue("bgcolor", "#FFFFFF");
            // Finally adding info
            String certN = (elements.get(2).text());
            Date beginDate = null;
            Date endDate = null;
            try {
                Date docDate = format.parse(elements.get(5).text());
                beginDate = docDate;
                if (elements.get(8).text().length() < 5) {
                    endDate = beginDate;
                } else {
                    docDate = format.parse(elements.get(8).text());
                    endDate = docDate;
                }
            } catch (ParseException e) {
                //Normal situation, just no info about date
                beginDate = new Date();
                endDate = new Date();
            }
            String applicant;
            String producer;
            String productName;
            if (power==2) {
                applicant = (elements.get(14).text());
                producer = (elements.get(23).text());
                productName = (elements.get(41).text());
            } else {
                applicant = (elements.get(11).text());
                producer = (elements.get(20).text());
                productName = (elements.get(35).text());
            }
            localResultTable.add(power, certN, beginDate, endDate, applicant, producer, productName);
        }
        return localResultTable.get();
    }

    public List getDeclarations(String searchValue, int resultSize, int timeOut) throws ServerNotAvailableException {
        try {
            URL regAdress = new URL(DECL_POST_URL_MAIN);
            HttpURLConnection regCon = (HttpURLConnection) regAdress.openConnection();
            regCon = (HttpURLConnection) regAdress.openConnection();
            timeOut=timeOut*1000;
            regCon.setConnectTimeout(timeOut);
            regCon.setReadTimeout(timeOut);
            //We have to encode our search value for correct work with KZ register =(
            searchValue = URLEncoder.encode(searchValue, "windows-1251");
            //Setting headers for post Request
            setHeaders(regCon);
            //Setting body
            String postData = DECL_PRE_STRING + searchValue + DECL_POST_STRING;
            //sending post
            StringBuilder sb = getStringBuilder(regCon, postData);
            String result = sb.toString();
            //Parsing result
            Document infotable = Jsoup.parse(result);
            Elements elements = infotable.getElementsByAttributeValue("width", "800");
            return parse(elements, 1,resultSize,timeOut);
        } catch (ServerNotAvailableException e) {
            log.warn("Server is not avilable again",e);
            throw new ServerNotAvailableException("KZ_DECL");
        } catch (Exception e) {
            log.error("Error getting info from reg");
            return new ArrayList();
        }
    }

    private StringBuilder getStringBuilder(HttpURLConnection regCon, String postData) throws IOException, ServerNotAvailableException {
        regCon.connect();
        //Getting response and build it to line
        byte[] postDataBytes = postData.getBytes();
        regCon.getOutputStream().write(postDataBytes);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
           br=new BufferedReader(new InputStreamReader(regCon.getInputStream(), "cp1251"));
        } catch (java.io.IOException e){
            log.error("Error getting info from reg");
            throw new ServerNotAvailableException("");
        }
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        br.close();
        return sb;
    }

    private void setHeaders(HttpURLConnection regCon) throws ProtocolException {
        regCon.setDoOutput(true);
        regCon.setRequestMethod(Headers.get("method.post"));
        regCon.setRequestProperty("accept-encoding", Headers.get("kz.accept-encoding"));
        regCon.setRequestProperty("Referer", Headers.get("kz.Referer"));
        regCon.setRequestProperty("Content-Type",Headers.get("kz.Content-Type"));
    }

}

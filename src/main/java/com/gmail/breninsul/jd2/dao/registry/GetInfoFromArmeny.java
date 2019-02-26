package com.gmail.breninsul.jd2.dao.registry;


import com.gmail.breninsul.jd2.pojo.Certificate;
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
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
@Log4j2
@Repository
public class GetInfoFromArmeny implements RegestryDAO{
    private static final String CERT_PRE_STRING = Headers.get("arm.CERT_PRE_STRING");
    private static final String CERT_POST_STRING = Headers.get("arm.CERT_POST_STRING");
    private static final String CERT_POST_URL = Headers.get("arm.CERT_POST_URL");
    private static final String DECL_POST_URL = Headers.get("arm.DECL_POST_URL");
    private static final String DECL_PRE_STRING = Headers.get("arm.DECL_PRE_STRING");
    private static final String DECL_POST_STRING = Headers.get("arm.DECL_POST_STRING");
    private static SimpleDateFormat format = new SimpleDateFormat();
    static {
        format.applyPattern("dd/MM/yyyy");
    }

    private List<Certificate> parse(Elements elements, int vigor, int resultSize) {
        LocalResultTable localResultTable = new LocalResultTable();
        if (elements.size() < 1) {
            return new ArrayList();
        }
        //Making result tables
        int counterForResultCycle = 0;
        for (Element element : elements) {
            if (counterForResultCycle > resultSize) {
                break;
            }
            if (counterForResultCycle < 1) {
                counterForResultCycle++;
                continue;
            }
            String certN = element.child(1).text();
            Date beginDate = null;
            Date endDate = null;
            try {
                Date docDate = format.parse(element.child(2).text());
                beginDate = docDate;
                docDate = format.parse(element.child(3).text());
                endDate = docDate;
            } catch (ParseException e) {
                log.info("Error parsing date from AM reg",e.getMessage());
                beginDate = new Date();
                endDate = new Date();
            }
            String applicant = element.child(4).text();
            String producer = element.child(5).text();
            String productName = element.child(6).text();
            localResultTable.add(vigor, certN, beginDate, endDate, applicant, producer, productName);
            counterForResultCycle++;
        }
        return localResultTable.get();
    }

    /**
     * Searching info about certificate in Armenian registry
     *
     * @param searchValue Put here name of the product
     * @return List  -it's a List of List's with cert info
     */
    public List<Certificate> getCertificates(String searchValue, int resultSize, int timeOut) throws ServerNotAvailableException {
        try {
            URL regAdress = new URL(CERT_POST_URL);
            HttpURLConnection
                    regCon = (HttpURLConnection) regAdress.openConnection();
            timeOut = timeOut * 1000;
            regCon.setConnectTimeout(timeOut);
            regCon.setReadTimeout(timeOut);
            //Setting headers
            settingHeader(regCon);
            //Setting body
            String postData = CERT_PRE_STRING + URLEncoder.encode(searchValue, "UTF-8") + CERT_POST_STRING;
            regCon.connect();
            //Geting response
            byte[] postDataBytes = postData.getBytes();
            regCon.getOutputStream().write(postDataBytes);
            //Gzipping response
            String result = getResultString(regCon);
            //Parsing result
            Elements elements = getElements(result);
            return parse(elements, 2, resultSize);
        } catch (ServerNotAvailableException e) {
            log.info("Error parsing resultpage from AM !!");
            throw new ServerNotAvailableException("ARM_CERT");
        }catch (Exception e) {
            log.info("Error parsing resultpage from AM !!");
            return new ArrayList<Certificate>();
        }
    }

    private String getResultString(HttpURLConnection regCon) throws ServerNotAvailableException {
        StringBuilder sb = new StringBuilder();
        try {
            GZIPInputStream gis = null;
            try {
                gis = new GZIPInputStream(regCon.getInputStream());
            } catch (java.io.IOException e) {
                log.error("Error setting inputstream  !!",e);
                throw new ServerNotAvailableException("");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
            //Making result html table
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            log.error("Error parsing resultpage from AM !!");
        }
        return sb.toString();
    }

    private void settingHeader(HttpURLConnection regCon) throws ProtocolException {
        regCon.setDoOutput(true);
        regCon.setRequestMethod(Headers.get("method.post"));
        regCon.setRequestProperty("accept-encoding", Headers.get("arm.accept-encoding"));
        regCon.setRequestProperty("Cookie", Headers.get("arm.Cookie"));
    }

    /**
     * Searching info about declarations in Armenian registry
     *
     * @param searchValue Put here name of the product
     * @return List  -it's a List of List's with cert info
     */
    public List<Certificate> getDeclarations(String searchValue, int resultSize, int timeOut) throws ServerNotAvailableException {
        try {
            URL regAdress = new URL(DECL_POST_URL);
            HttpURLConnection
                    regCon = (HttpURLConnection) regAdress.openConnection();
            timeOut = timeOut * 1000;
            regCon.setConnectTimeout(timeOut);
            regCon.setReadTimeout(timeOut);
            //Setting headers
            settingHeader(regCon);
            //Setting body
            String postData = DECL_PRE_STRING + URLEncoder.encode(searchValue, "UTF-8") + DECL_POST_STRING;
            regCon.connect();
            //Geting response
            byte[] postDataBytes = postData.getBytes();
            regCon.getOutputStream().write(postDataBytes);
            StringBuilder sb = new StringBuilder();
            //Gzipping response
            String result = getResultString(regCon);
            //Parsing result
            Elements elements = getElements(result);
            regCon.disconnect();
            return parse(elements, 1, resultSize);
        } catch (ServerNotAvailableException e) {
            log.info("Server not avilable, again!");
            throw new ServerNotAvailableException("ARM_DECL");
        } catch (Exception e) {
            log.info("Error getting info from AM !!");
            return new ArrayList<Certificate>();
        }
    }

    private Elements getElements(String result) {
        Document infoTable = Jsoup.parse(result);
     //   Element elementTemp = infoTable.getElementById("MainContent_ContentRU_gvDocs");
        Element elementTemp = infoTable.getElementById("MainContent_ContentRU_gvSearchResult");
        return elementTemp.getElementsByTag("tr");
    }
}

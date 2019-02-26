package com.gmail.breninsul.jd2.dao.registry;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
@Log4j2
@Repository
public class GetInfoFromRF implements RegestryDAO{
    private static final String CERT_POST_URL = Headers.get("ru.CERT_POST_URL");
    private static final String DECL_POST_URL = Headers.get("ru.DECL_POST_URL");
    private static final String CERT_PRE_STRING = Headers.get("ru.CERT_PRE_STRING");
    private static final String CERT_POST_STRING = Headers.get("ru.CERT_POST_STRING");
    private static final String DECL_PRE_STRING = Headers.get("ru.DECL_PRE_STRING");
    private static final String DECL_POST_STRING = Headers.get("ru.DECL_POST_STRING");

    /**
     * Searching info about certificate in RF registry
     *
     * @param searchValue Put here name of the product
     * @return List  -it's a List of List's with cert info
     */
    public List getCertificates(String searchValue, int resultSize, int timeOut) throws ServerNotAvailableException {
        try {
            URL regAdress = new URL(CERT_POST_URL);
            HttpURLConnection
                    regCon = (HttpURLConnection) regAdress.openConnection();
            timeOut = timeOut * 1000;
            regCon.setConnectTimeout(timeOut);
            regCon.setReadTimeout(timeOut);
            //Setting headers
            settingHeadersCert(regCon);
            //Setting body
            String postData = CERT_PRE_STRING + searchValue + CERT_POST_STRING;
            regCon.connect();
            //Geting response
            String result = getResultHTML(regCon, postData);
            LocalResultTable localResultTable = getLocalResultTable(resultSize, result);
            regCon.disconnect();
            return localResultTable.get();
        } catch (ServerNotAvailableException e) {
            log.info("Server is not responding, again",e);
            throw new ServerNotAvailableException("RF_CERT");
        } catch (Exception e) {
            log.info("Problem getting info",e);
            return new ArrayList();
        }
    }

    private LocalResultTable getLocalResultTable(int resultSize, String result) {
        StringBuilder sb;
        sb = new StringBuilder();
        //Parsing result
        Document infotable = Jsoup.parse(result);
        Element elementTemp = infotable.getElementById("bodyTableData");
        Elements elements = elementTemp.getElementsByTag("td");
        String temp = "";
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd.MM.yyyy");
        Integer vigor = null;
        String certN = null;
        Date beginDate = null;
        Date endDate = null;
        String applicant = null;
        String producer = null;
        String productName = null;
        LocalResultTable localResultTable = new LocalResultTable();
        int counter = 0;
        //Making result tables
        int counterForResultCycle = 0;
        for (Element element : elements) {
            if (counterForResultCycle >= resultSize) {
                break;
            }
            counter++;
            switch (counter) {
                case 1:
                    vigor = 2;
                    break;
                case 2:
                    certN = element.child(0).text();
                    break;
                case 3:
                    try {
                        Date docDate = format.parse(element.child(0).text());
                        beginDate = docDate;
                    } catch (ParseException e) {
                        beginDate = new Date();
                        log.warn("problem parsing date",e);
                    }
                    break;
                case 4:
                    try {
                        if (element.child(0).text().length() < 5) {
                            endDate = beginDate;
                        } else {
                            Date docDate = format.parse(element.child(0).text());
                            endDate = docDate;
                        }
                    } catch (ParseException e) {
                        endDate = new Date();
                        log.warn("problem parsing date",e);
                    }
                    break;
                case 5:
                    applicant = element.text();
                    break;
                case 6:
                    producer = element.text();
                    break;
                case 7:
                    productName = element.text();
                    break;
                case 8:
                    counter = 0;
                    counterForResultCycle++;
                    localResultTable.add(vigor, certN, beginDate, endDate, applicant, producer, productName);
                    break;
            }
        }
        return localResultTable;
    }

    private String getResultHTML(HttpURLConnection regCon, String postData) throws IOException, ServerNotAvailableException {
        byte[] postDataBytes = postData.getBytes();
        regCon.getOutputStream().write(postDataBytes);
        StringBuilder sb = new StringBuilder();
        //Gzipping response
        GZIPInputStream gis = null;
        try {
            gis = new GZIPInputStream(regCon.getInputStream());
        } catch (java.io.IOException e) {
            log.error("problem setting input",e);
            throw new ServerNotAvailableException("");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(gis, "cp1251"));
        int counter = 0;
        String line;
        //Making result html table
        while ((line = br.readLine()) != null) {
            sb.append(line)
            .append("\n");
        }
        br.close();
        gis.close();
        return sb.toString();
    }

    private static void settingHeadersCert(HttpURLConnection regCon) throws ProtocolException {
        regCon.setDoOutput(true);
        regCon.setRequestMethod(Headers.get("method.post"));
        regCon.setRequestProperty("accept-encoding", Headers.get("ru.cert.accept-encoding"));
        regCon.setRequestProperty("accept", Headers.get("ru.cert.accept"));
        regCon.setRequestProperty("Content-Length", Headers.get("ru.cert.Content-Length"));
        regCon.setRequestProperty("content-type", Headers.get("ru.cert.content-type"));
    }

    /**
     * Searching info about declarations in RF registry
     *
     * @param searchValue Put here name of the product
     * @return List  -it's a List of List's with cert info
     */
    public List getDeclarations(String searchValue, int resultSize, int timeOut) throws ServerNotAvailableException {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd.MM.yyyy");
        try {
            URL regAdress = new URL(DECL_POST_URL);
            HttpURLConnection
                    regCon = (HttpURLConnection) regAdress.openConnection();
            timeOut = timeOut * 1000;
            regCon.setConnectTimeout(timeOut);
            regCon.setReadTimeout(timeOut);
            //Setting headers
            settingHeadersDecl(regCon);
            //Setting body
            String postData = DECL_PRE_STRING + searchValue + DECL_POST_STRING;
            regCon.connect();
            //Geting response
            String result = getResultHTML(regCon, postData);
            LocalResultTable localResultTable = getLocalResultTable(resultSize, result);
            regCon.disconnect();
            return localResultTable.get();
        } catch (ServerNotAvailableException e) {
            log.info("server is not responding,again",e);            throw new ServerNotAvailableException("RF_DECL");
        } catch (Exception e) {
            log.warn("problem getting info",e);
            return new ArrayList();
        }
    }

    //The way of working with declarations search has changed, it's the old method
    //TODO delete 01.06.2019
    @Deprecated
    public List getDeclarationsOld(String searchValue, int resultSize, int timeOut) {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd-MM-yyyy");
        LocalResultTable localResultTable = new LocalResultTable();
        try {
            URL regAdress = new URL(DECL_POST_URL);
            HttpURLConnection
                    regCon = (HttpURLConnection) regAdress.openConnection();
            timeOut = timeOut * 1000;
            regCon.setConnectTimeout(timeOut);
            regCon.setReadTimeout(timeOut);
            //Setting headers
            settingHeadersDecl(regCon);
            //Setting json body
            String postData = DECL_PRE_STRING + searchValue + DECL_POST_STRING;
            regCon.connect();
            //Getting json response
            byte[] postDataBytes = postData.getBytes();
            regCon.getOutputStream().write(postDataBytes);
            BufferedReader br;
            try {
                br = new BufferedReader(new InputStreamReader(regCon.getInputStream()));
            } catch (Exception e) {
                log.error("problem getting info",e);
                return new ArrayList();
            }
            String result = br.readLine();
            br.close();
            JSONObject stream;
            JSONArray json = null;
            int total = 0;
            //Setting info List's
            try {
                stream = new JSONObject(result);
                total = stream.getInt("total");
                if (total > resultSize) {
                    total = resultSize;
                }
                json = stream.getJSONArray("items");
                for (int i = 0; i < total; i++) {
                    JSONObject temp = json.getJSONObject(i);
                    int vigor = (1);
                    Date beginDate = null;
                    Date endDate = null;
                    try {
                        Date docDate = format.parse(temp.getString("declDate"));
                        beginDate = docDate;
                        if (temp.getString("declEndDate").length() < 6) {
                            endDate = beginDate;
                        } else {
                            docDate = format.parse(temp.getString("declEndDate"));
                            endDate = docDate;
                        }
                    } catch (ParseException e) {
                        beginDate = new Date();
                        endDate = new Date();
                        log.warn("problem parsing date",e);
                    }
                    String certN = temp.getString("number");
                    String applicant = (temp.getString("applicantName"));
                    String producer = temp.getString("manufacterName");
                    String productName = temp.getString("productFullName");
                    localResultTable.add(vigor, certN, beginDate, endDate, applicant, producer, productName);
                }
            } catch (Exception e) {
                log.error("problem getting info",e);
                return new ArrayList();
            }
            regCon.disconnect();
            return localResultTable.get();
        } catch (Exception e) {
            log.warn("error getting  info",e);
            return new ArrayList();
        }
    }

    private void settingHeadersDecl(HttpURLConnection regCon) throws ProtocolException {
        regCon.setDoOutput(true);
        regCon.setRequestMethod(Headers.get("method.post"));
        regCon.setRequestProperty("Host", Headers.get("ru.decl.host"));
        regCon.setRequestProperty("Referer", Headers.get("ru.decl.referer"));
        String[] cookies = Headers.get("ru.decl.cookies").split(" ");
        for (String cookie : cookies) {
            regCon.addRequestProperty("Cookie", cookie);
        }
        regCon.setRequestProperty("accept-encoding", Headers.get("ru.decl.accept-encoding"));
        regCon.setRequestProperty("accept", Headers.get("ru.decl.accept"));
        regCon.setRequestProperty("content-type", Headers.get("ru.decl.content-type"));
        regCon.setRequestProperty("X-Requested-With", Headers.get("ru.decl.X-Requested-With"));
        regCon.setRequestProperty("Content-Length", Headers.get("ru.decl.Content-Length"));
    }

}

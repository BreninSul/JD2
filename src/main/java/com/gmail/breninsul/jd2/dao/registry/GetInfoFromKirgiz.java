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
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
@Log4j2
@Repository
public class GetInfoFromKirgiz implements RegestryDAO {
    private static final String CERT_PRE_STRING = Headers.get("kirgiz.CERT_PRE_STRING");
    private static final String CERT_POST_STRING = Headers.get("kirgiz.CERT_POST_STRING");
    private static final String DECL_PRE_STRING = Headers.get("kirgiz.DECL_PRE_STRING");
    private static final String DECL_POST_STRING = Headers.get("kirgiz.DECL_POST_STRING");

    /**
     * Searching info about certificate in Kirgizhstan registry
     *
     * @param searchValue Put here name of the product
     * @return List  -it's a List of List's with cert info, null
     */
    public List<Certificate> getCertificates(String searchValue, int resultSize, int timeOut) throws ServerNotAvailableException {
        try {
            //Setting headers for post Reqest
            String postData = CERT_PRE_STRING + URLEncoder.encode(searchValue, "UTF-8") + CERT_POST_STRING;
            //seding post
            URL regAdress = new URL(postData);
            HttpURLConnection regCon = (HttpURLConnection) regAdress.openConnection();
            timeOut = timeOut * 1000;
            regCon.setConnectTimeout(timeOut);
            regCon.setReadTimeout(timeOut);
            regCon.setRequestMethod(Headers.get("method.get"));
            regCon.connect();
            //Getting response and build it to line
            String result = getResultString(regCon);
            //Parsing result
            Elements elements = getElements(result);
            return parse(elements, 1, resultSize);
        } catch (ServerNotAvailableException e) {
            log.info("Server is not avilable again",e);
            throw new ServerNotAvailableException("KIRGIZ_CERT");
        } catch (Exception e) {
            log.error("Error parsing resultpage from AM !!",e);
            return new ArrayList<Certificate>();
        }
    }

    /**
     * Searching info about declarations in Kirgizhstan registry
     *
     * @param searchValue Put here name of the product
     * @return List  -it's a List of List's with cert info, null
     */
    public List<Certificate> getDeclarations(String searchValue, int resultSize, int timeOut) throws ServerNotAvailableException {
        try {
            //Setting headers for post Reqest
            String postData = DECL_PRE_STRING + URLEncoder.encode(searchValue, "UTF-8") + DECL_POST_STRING;
            //seding post
            URL regAdress = new URL(postData);
            HttpURLConnection regCon = (HttpURLConnection) regAdress.openConnection();
            timeOut = timeOut * 1000;
            regCon.setConnectTimeout(timeOut);
            regCon.setReadTimeout(timeOut);
            regCon.setRequestMethod(Headers.get("method.get"));
            regCon.connect();
            //Getting response and build it to line
            String result = getResultString(regCon);
            //Parsing result
            Elements elements = getElements(result);
            regCon.disconnect();
            return parse(elements, 1, resultSize);
        } catch (ServerNotAvailableException e) {
            log.info("Server is not avilable again",e);
            throw new ServerNotAvailableException("KIRGIZ_DECL");
        } catch (Exception e) {
            log.error("Error parsing resultpage from AM !!",e);
            return new ArrayList<Certificate>();
        }
    }

    private Elements getElements(String result) {
        Document infotable = Jsoup.parse(result);
        Element elementTemp = infotable.getElementById("reportTable");
        Elements elements;
        elements = elementTemp.getElementsByTag("tr");
        return elements;
    }

    private static String getResultString(HttpURLConnection regCon) throws IOException, ServerNotAvailableException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(regCon.getInputStream(), "UTF-8"));
        } catch (java.io.IOException e) {
            log.error("Error parsing resultpage from AM !!",e);
            throw new ServerNotAvailableException("");
        }
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        br.close();
        return sb.toString();
    }

    private List<Certificate> parse(Elements elements, int vigor, int resultSize) {
        if (elements.size() < 1) {
            return new ArrayList();
        }
        LocalResultTable localResultTable = new LocalResultTable();
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
            String certN = (element.child(1).text());
            Date beginDate = null;
            Date endDate = null;
            beginDate = formatDate((String) element.child(7).text());
            endDate = formatDate((String) element.child(8).text());
            if (endDate == null) {
                endDate = beginDate;
            }
            String applicant = (element.child(5).text());
            String producer = (element.child(6).text());
            String productName = (element.child(4).text());
            localResultTable.add(vigor, certN, beginDate, endDate, applicant, producer, productName);
            counterForResultCycle++;
        }
        return localResultTable.get();
    }

    private Date formatDate(String dateString) {
        if (dateString.length() < 10) {
            return new Date();
        }
        String[] parts = dateString.split(" ");
        String dayString = parts[0];
        String yearString = parts[2];
        String monthString = parts[2];
        int day = Integer.parseInt(dayString.toString());
        int month = 1;
        int year = Integer.parseInt(yearString.toString());
        if (monthString.equals("январь")) {
            month = 1;
        } else {
            if (monthString.equals("февраль")) {
                month = 2;
            } else {
                if (monthString.equals("март")) {
                    month = 3;
                } else {
                    if (monthString.equals("апрель")) {
                        month = 4;
                    } else {
                        if (monthString.equals("май")) {
                            month = 5;
                        } else {
                            if (monthString.equals("июнь")) {
                                month = 6;
                            } else {
                                if (monthString.equals("июль")) {
                                    month = 7;
                                } else {
                                    if (monthString.equals("август")) {
                                        month = 8;
                                    } else {
                                        if (monthString.equals("сентябрь")) {
                                            month = 9;
                                        } else {
                                            if (monthString.equals("октябрь")) {
                                                month = 10;
                                            } else {
                                                if (monthString.equals("ноябрь")) {
                                                    month = 11;
                                                } else {
                                                    if (monthString.equals("декабрь")) {
                                                        month = 12;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Calendar calendar = new GregorianCalendar();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

}

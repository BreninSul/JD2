package com.gmail.breninsul.jd2.dao.registry;

import com.gmail.breninsul.jd2.pojo.Certificate;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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
public class GetInfoFromRB {
    private static final String CERT_PRE_STRING = Headers.get("by.CERT_PRE_STRING");
    private static final String CERT_POST_STRING = Headers.get("by.CERT_POST_STRING");
    private static SimpleDateFormat format = new SimpleDateFormat();
    static {
        format.applyPattern("dd.MM.yyyy");
    }
    /**
     * Searching info about certificate in RB registry
     *
     * @param searchValue Put here name of the product
     * @return List  -it's a List of List's with cert info, null
     */
    public List getCertificatesAndDeclorations(String searchValue, int resultSize, int timeOut) throws ServerNotAvailableException {
        LocalResultTable localResultTable = new LocalResultTable();
        try {
            //Setting headers for post Reqest
            String postData = CERT_PRE_STRING + resultSize + CERT_POST_STRING + URLEncoder.encode(searchValue, "UTF-8");
            //seding post
            URL regAdress = new URL(postData);
            HttpURLConnection regCon = (HttpURLConnection) regAdress.openConnection();
            timeOut = timeOut * 1000;
            regCon.setConnectTimeout(timeOut);
            regCon.setReadTimeout(timeOut);
            setHead(regCon);
            //Setting body
            regCon.connect();
            //Getting response and build it to line
            StringBuilder sb = new StringBuilder();
            GZIPInputStream gis=null;
            try {
                gis = new GZIPInputStream(regCon.getInputStream());
            } catch (java.io.IOException e){
                log.error("error setting input strem ",e);
                throw new ServerNotAvailableException("RB_CERT_DECL");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(gis));
            String result = br.readLine();
            br.close();
            JSONObject stream;
            JSONArray json = null;
            int total = 0;
            //Parsing result
            try {
                stream = new JSONObject(result);
                JSONObject meta = stream.getJSONObject("_meta");
                json = stream.getJSONArray("items");
                total = json.length();
                if (total > resultSize) {
                    total = resultSize;
                }
                for (int i = 0; i < total; i++) {
                    JSONObject temp = json.getJSONObject(i);
                    temp = temp.getJSONObject("certdecltr_ConformityDocDetails");

                    Date beginDate = null;
                    Date endDate = null;
                    try {
                        Date docDate = format.parse(temp.getString("DocStartDate"));
                        beginDate = docDate;
                        try {
                            docDate = format.parse(temp.getString("DocValidityDate"));
                            endDate = docDate;
                        } catch (ParseException |JSONException e) {
                            endDate = beginDate;
                        }
                    } catch (ParseException | JSONException e) {
                        beginDate = new Date();
                        endDate = new Date();
                        log.warn("error parsing date",e);
                    }
                    String certN = temp.getString("DocId");
                    String applicant = (temp.getJSONObject("ApplicantDetails").getString("BusinessEntityName"));
                    String producer = temp.getJSONObject("TechnicalRegulationObjectDetails").getJSONArray("ManufacturerDetails").getJSONObject(0).getString("BusinessEntityName");
                    String productName = (temp.getJSONObject("TechnicalRegulationObjectDetails").getJSONArray("ProductDetails").getJSONObject(0).getString("ProductName"));
                    int vigor = temp.getInt("ConformityDocKindCode");
                    if (vigor == 10) {
                        vigor = 1;
                    } else {
                        if (vigor == 15) {
                            vigor = 2;
                        } else {
                            vigor = 1;
                        }
                    }
                    localResultTable.add(vigor, certN, beginDate, endDate, applicant, producer, productName);
                }
                regCon.disconnect();
            } catch (JSONException e) {
                log.error("error parsing json",e);
            }

            return localResultTable.get();
        } catch (IOException e) {
            log.error("some error",e);
        }
        return new ArrayList();
    }
    private void setHead(HttpURLConnection regCon) throws ProtocolException {
        regCon.setRequestMethod(Headers.get("method.get"));
        regCon.setRequestProperty("accept-encoding",Headers.get("by.accept-encoding"));
        regCon.setRequestProperty("Accept",Headers.get("by.Accept"));
        regCon.setRequestProperty("Referer",Headers.get("by.Referer"));
        regCon.setRequestProperty("X-Requested-With",Headers.get("by.X-Requested-With"));
    }


}

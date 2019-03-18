package com.gmail.breninsul.jd2.service.impl;


import com.gmail.breninsul.jd2.dao.registry.*;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.service.RegestryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Log4j2
@Service
public class RegestryServiceImpl implements RegestryService {
    protected static final int STANDART_CACHE_LIFETIME_HOURS = 2;
    public static final String CACHE_NAME = "searchCache";
    public static final String BELARUS = "BR";
    public static final String RF = "RF";
    public static final String ARMENY = "ARM";
    public static final String KZ = "KZ";
    public static final String KIRGIZ = "KZ";
    public static final String DECLARATION = "_DECL";
    public static final String CERTIFICATE = "_CERT";
    protected static CachingProvider provider = Caching.getCachingProvider();
    //protected Cache<String, List> cache;
    protected Cache<String, InfoEntity> cache2;

    protected static CacheManager cacheManager = provider.getCacheManager();
    private final static int THREADS_SIZE = 9;
    private static ThreadLocal<ExecutorService> tl = new ThreadLocal<>();
    private ExecutorService executor = getExecutor();
    private static final int CERTIFICATE_CACHE_LIFETIME_HOURS = 24;
    @Autowired
    GetInfoFromArmeny armRep;
    @Autowired
    GetInfoFromRB rbRep;
    @Autowired
    GetInfoFromKirgiz kgRep;
    @Autowired
    GetInfoFromKZ kzRep;
    @Autowired
    GetInfoFromRF rfRep;

    @PostConstruct
    public void init() {
        // cache = createCache(CERTIFICATE_CACHE_LIFETIME_HOURS);
        cache2 = createCache2(CERTIFICATE_CACHE_LIFETIME_HOURS);
    }

    /* private Cache createCache(long hours) {
         MutableConfiguration config = new MutableConfiguration<>();
         config.setTypes(String.class, List.class)
                 .setStoreByValue(false)
                 .setStatisticsEnabled(true)
                 .setExpiryPolicyFactory(FactoryBuilder.factoryOf(
                         new AccessedExpiryPolicy(new Duration(TimeUnit.HOURS, hours))));
         return cacheManager.createCache(CACHE_NAME, config);
     }*/
      /*private void addToCache(List<Certificate> entity, String value, String country, String certType) {
        try {
            if (cache == null) {
                throw new NullPointerException();
            } else {
                try {
                    cache.put(value + country + certType, entity);
                } catch (NullPointerException e) {
                    log.info("Trying to put in cache serch result " + country + certType);
                }
            }
        } catch (NullPointerException e) {
            log.error("Cache has not been initialised for regestry");
        }
    }

    protected List<Certificate> getFromCache(String value, String country, String certType) {
        List<Certificate> entity = cache.get(value + country + certType);
        if (entity == null) {
            log.info("There is no result in cache for " + value + country + certType + ", returning null");
        } else {
            log.info("There is entity with id=" + value + country + certType + " returning it");
        }
        return entity;
    }*/

    public void clearCache2() {
        cache2.clear();
    }

    private Cache createCache2(long hours) {
        MutableConfiguration config = new MutableConfiguration<>();
        config.setTypes(String.class, InfoEntity.class)
                .setStoreByValue(false)
                .setStatisticsEnabled(true)
                .setExpiryPolicyFactory(FactoryBuilder.factoryOf(
                        new AccessedExpiryPolicy(new Duration(TimeUnit.HOURS, hours))));
        return cacheManager.createCache("InfoEntity", config);
    }


    private void addToCache2(InfoEntity entity, String value) {
        try {
            if (cache2 == null) {
                throw new NullPointerException();
            } else {
                try {
                    cache2.put(value, entity);
                } catch (NullPointerException e) {
                    log.info("Trying to put in cache serch result ");
                }
            }
        } catch (NullPointerException e) {
            log.error("Cache has not been initialised for regestry");
        }
    }

    protected InfoEntity getFromCache2(String value) {
        InfoEntity entity = cache2.get(value);
        if (entity == null) {
            log.info("There is no result in cache for " + value + ", returning null");
        } else {
            log.info("There is entity with id=" + value + " returning it");
        }
        return entity;
    }

    /**
     * This method returns search result from third-party registrees
     *
     * @param searchValue   Name of the product we want to search in registrees
     * @param resultSparrow Number of results
     * @param timeOut       How long we can wait until result
     * @return null if there server not avalable
     * @throws ExecutionException
     * @throws InterruptedException
     */


    private Future<List<Certificate>> executeDeclInNewThread(RegestryDAO dao, String searchValue, int resultSparrow, int timeOut) {
        return executor.submit(new Callable<List<Certificate>>() {
            @Override
            public List<Certificate> call() {
                try {
                    return dao.getDeclarations(searchValue, resultSparrow, timeOut);
                } catch (ServerNotAvailableException e) {
                    log.info("Decl server is not avilable");
                    return null;
                }
            }
        });
    }

    private Future<List<Certificate>> executeCertslInNewThread(RegestryDAO dao, String searchValue, int resultSparrow, int timeOut) {
        return executor.submit(new Callable<List<Certificate>>() {
            @Override
            public List<Certificate> call() {
                try {
                    return dao.getCertificates(searchValue, resultSparrow, timeOut);
                } catch (ServerNotAvailableException e) {
                    log.info("Cert server is not avilable");
                    return null;
                }
            }
        });
    }

    public InfoEntity get(String value, int resultSparrow, int timeOut) {
        InfoEntity infoEntity = getFromCache2(value);
        boolean hasCache = infoEntity != null;
        if (hasCache) {
            log.info("There is result for value=" + value + "=" + infoEntity);
            if (infoEntity.noErrors()) {
                return infoEntity;
            } else {
                log.info("But some server has been not avalible, trying to get new result");
            }
        }
        try {
            ExecutorService executor = getExecutor();
            List<Future<List<Certificate>>> futureList = new ArrayList<>();
            List<RegestryDAO> repList = new ArrayList();
            repList.add(armRep);
            repList.add(kgRep);
            repList.add(kzRep);
            repList.add(rfRep);
            for (RegestryDAO o : repList) {
                futureList.add(executeDeclInNewThread(o, value, resultSparrow, timeOut));
                futureList.add(executeCertslInNewThread(o, value, resultSparrow, timeOut));
            }
            Future<List<Certificate>> futureBelarusCeritificatesAndDeclarations = executor.submit(new Callable<List<Certificate>>() {
                @Override
                public List<Certificate> call() {
                    try {
                        return rbRep.getCertificatesAndDeclorations(value, resultSparrow, timeOut);
                    } catch (ServerNotAvailableException e) {
                        log.info("Bel server is not avilable");
                        return null;
                    }
                }
            });
            List<List<Certificate>> results = new ArrayList<>();
            int conter = 0;
            for (Future<List<Certificate>> f : futureList) {
                if (hasCache) {
                    if (infoEntity.getAlphabetical(conter) != null) {
                        results.add(infoEntity.getAlphabetical(conter));
                    } else {
                        results.add(getCertificates(f));
                    }
                }else {
                    results.add(getCertificates(f));
                }
                conter++;
            }
            List<Certificate> belarusCeritificatesAndDeclarations;
            belarusCeritificatesAndDeclarations = getCertificates(futureBelarusCeritificatesAndDeclarations);
            //Closing threads if smth went wrong
            executor.shutdownNow();
            List<Certificate> belarusCeritificates = null;
            List<Certificate> belarusDeclarations = null;
            log.info(belarusCeritificatesAndDeclarations);
            if (belarusCeritificatesAndDeclarations != null) {
                belarusCeritificates = new ArrayList();
                belarusDeclarations = new ArrayList();
                for (Certificate Document : belarusCeritificatesAndDeclarations) {
                    if (Document.getCertType() > 1) {
                        belarusCeritificates.add(Document);
                    } else if (Document.getCertType() < 2) {
                        belarusDeclarations.add(Document);
                    }
                }
            }
            results.add(belarusDeclarations);
            results.add(belarusCeritificates);
            infoEntity = new InfoEntity(results);
            addToCache2(infoEntity, value);
        } catch (Exception e) {
            log.info(e);
        }
        return infoEntity;
    }

    private List<Certificate> getCertificates(Future<List<Certificate>> futureCertificates) {
        List<Certificate> certificates = null;
        try {
            certificates = futureCertificates.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("", e);
        }
        return certificates;
    }

    private static ExecutorService getExecutor() {
        if (tl.get() == null) {
            ExecutorService executor = Executors.newFixedThreadPool(THREADS_SIZE);
            tl.set(Executors.newFixedThreadPool(THREADS_SIZE));
        }
        return tl.get();
    }
}

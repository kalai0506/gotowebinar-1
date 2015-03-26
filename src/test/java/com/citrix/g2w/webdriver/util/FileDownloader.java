/*
 * Copyright (c) 1998-2014 Citrix Online LLC
 * All Rights Reserved Worldwide.
 *
 * THIS PROGRAM IS CONFIDENTIAL AND PROPRIETARY TO CITRIX ONLINE
 * AND CONSTITUTES A VALUABLE TRADE SECRET. Any unauthorized use,
 * reproduction, modification, or disclosure of this program is
 * strictly prohibited. Any use of this program by an authorized
 * licensee is strictly subject to the terms and conditions,
 * including confidentiality obligations, set forth in the applicable
 * License and Co-Branding Agreement between Citrix Online LLC and
 * the licensee.
 */

package com.citrix.g2w.webdriver.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.citrix.g2w.webdriver.pages.BasePage;

/**
 * 

 */

public class FileDownloader extends BasePage {

    private final WebDriver driver;
    private final boolean followRedirects = true;
    private boolean mimicWebDriverCookieState = true;

    public FileDownloader(WebDriver driverObject) {
        this.driver = driverObject;
    }

    /**
     * Perform the file download.
     * 
     * @param localDownloadPath
     * @return
     * @throws IOException
     * @throws NullPointerException
     * @throws URISyntaxException
     */
    public String downloader(String localDownloadPath) throws IOException, NullPointerException,
            URISyntaxException {
        return downloader(this.driver.getCurrentUrl(), localDownloadPath);
    }

    public String downloader(String fileToDownloadLocation, String localDownloadPath) throws IOException, URISyntaxException {
        URL fileToDownload = new URL(fileToDownloadLocation);
        String fileNameURI = fileToDownload.getFile();
        String filePath = "";
        if (fileNameURI.contains("?")) {
            filePath = localDownloadPath
                    + fileNameURI.substring(fileNameURI.substring(0, fileNameURI.indexOf("?")).lastIndexOf("/") + 1,
                    fileNameURI.indexOf("?"));
        } else {
            filePath = localDownloadPath + fileNameURI.substring(fileNameURI.lastIndexOf("/") + 1);
        }
        File downloadedFile = new File(filePath);
        if (downloadedFile.canWrite() == false) {
            downloadedFile.setWritable(true);
        }

        HttpClient client = new DefaultHttpClient();
        BasicHttpContext localContext = new BasicHttpContext();

        if (this.mimicWebDriverCookieState) {
            localContext.setAttribute(ClientContext.COOKIE_STORE,
                    this.mimicCookieState(this.driver.manage().getCookies()));
        }

        HttpGet httpget = new HttpGet(fileToDownload.toURI());
        HttpParams httpRequestParameters = httpget.getParams();
        httpRequestParameters.setParameter(ClientPNames.HANDLE_REDIRECTS, this.followRedirects);
        httpget.setParams(httpRequestParameters);

        HttpResponse response = client.execute(httpget, localContext);

        FileUtils.copyInputStreamToFile(response.getEntity().getContent(), downloadedFile);
        response.getEntity().getContent().close();

        String downloadedFileAbsolutePath = downloadedFile.getAbsolutePath();
        this.logger.log("File downloaded to '" + downloadedFileAbsolutePath + "'");

        return downloadedFileAbsolutePath;
    }

    /**
     * Load in all the cookies WebDriver currently knows about so that we can
     * mimic the browser cookie state.
     * 
     * @param seleniumCookieSet
     * @return mimicWebDriverCookieStore
     */
    private BasicCookieStore mimicCookieState(Set<Cookie> seleniumCookieSet) {
        BasicCookieStore mimicWebDriverCookieStore = new BasicCookieStore();
        for (Cookie seleniumCookie : seleniumCookieSet) {
            BasicClientCookie duplicateCookie = new BasicClientCookie(seleniumCookie.getName(),
                    seleniumCookie.getValue());
            duplicateCookie.setDomain(seleniumCookie.getDomain());
            duplicateCookie.setSecure(seleniumCookie.isSecure());
            duplicateCookie.setExpiryDate(seleniumCookie.getExpiry());
            duplicateCookie.setPath(seleniumCookie.getPath());
            mimicWebDriverCookieStore.addCookie(duplicateCookie);
        }

        return mimicWebDriverCookieStore;
    }

    /**
     * Mimic the cookie state of WebDriver (Defaults to true) This will enable
     * you to access files that are only available when logged in. If set to
     * false the connection will be made as an anonymouse user
     * 
     * @param value
     *            (value)
     */
    public void mimicWebDriverCookieState(final boolean value) {
        this.mimicWebDriverCookieState = value;
    }
}

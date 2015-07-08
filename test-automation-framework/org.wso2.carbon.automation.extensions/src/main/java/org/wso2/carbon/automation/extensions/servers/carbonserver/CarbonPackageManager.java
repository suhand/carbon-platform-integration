/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.carbon.automation.extensions.servers.carbonserver;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.automation.extensions.ExtensionConstants;
import org.wso2.carbon.automation.extensions.FrameworkExtensionUtils;
import org.wso2.carbon.automation.extensions.servers.utils.FileManipulator;

import java.io.File;
import java.io.IOException;

public class CarbonPackageManager {
    private static final Log log = LogFactory.getLog(CarbonPackageManager.class);

    public String unzipCarbonPackage() throws ZipException {
        String carbonTempHome = FrameworkExtensionUtils.getCarbonTempLocation();
        String carbonHome;
        String carbonZipFileLocation = FrameworkExtensionUtils.getCarbonZipLocation();
        if (new File(carbonTempHome).exists()) {
            FileManipulator.deleteDir(new File(carbonTempHome));
        }
        try {
            ZipFile zipFile = new ZipFile(FrameworkExtensionUtils.getCarbonZipLocation());
            carbonHome = carbonTempHome + File.separator + carbonZipFileLocation.
                    substring(carbonZipFileLocation.lastIndexOf(File.separator) + 1,
                              carbonZipFileLocation.indexOf(".zip"));
            log.info("Extracting carbon distribution into : " + carbonTempHome);
            zipFile.extractAll(carbonTempHome);
        } catch (ZipException e) {
            log.error("Unable to extract distribution at " +
                      FrameworkExtensionUtils.getCarbonZipLocation());
            throw new ZipException("Unable to extract distribution at " +
                                   FrameworkExtensionUtils.getCarbonZipLocation(), e);
        }
        return carbonHome;
    }

    public void copySecurityVerificationService() throws IOException {
        String secVerifierDir = System.getProperty(ExtensionConstants.SYSTEM_PROPERTY_SEC_VERIFIER_DIRECTORY);
        File srcFile = new File(secVerifierDir + ExtensionConstants.SERVICE_FILE_SEC_VERIFIER);
        String deploymentPath = FrameworkExtensionUtils.getCarbonServerAxisServiceDirectory();

        File depFile = new File(deploymentPath);
        if (!depFile.exists() && !depFile.mkdir()) {
            throw new IOException("Error while creating the deployment folder : " + deploymentPath);
        }

        File dstFile = new File(depFile.getAbsolutePath() + File.separator +
                                ExtensionConstants.SERVICE_FILE_SEC_VERIFIER);

        log.info("Copying " + srcFile.getAbsolutePath() + " => " + dstFile.getAbsolutePath());
        FileManipulator.copyFile(srcFile, dstFile);
    }
}

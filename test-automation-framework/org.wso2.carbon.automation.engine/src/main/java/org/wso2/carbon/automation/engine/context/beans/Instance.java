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
* */
package org.wso2.carbon.automation.engine.context.beans;

import java.util.HashMap;

/*
 * Represents the data structure for Instance node in automation.xml
 */
public class Instance {
    private String name;
    private String type;
    private boolean isNonBlockingTransportEnabled;
    private HashMap<String, String> hosts = new HashMap<String, String>();
    private HashMap<String, String> ports = new HashMap<String, String>();
    private HashMap<String, String> properties = new HashMap<String, String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, String> getHosts() {
        return hosts;
    }

    public void setHosts(HashMap<String, String> hosts) {
        this.hosts = hosts;
    }

    public HashMap<String, String> getPorts() {
        return ports;
    }

    public void setPorts(HashMap<String, String> ports) {
        this.ports = ports;
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public boolean isNonBlockingTransportEnabled() {
        return isNonBlockingTransportEnabled;
    }

    public void setNonBlockingTransportEnabled(boolean isNonBlockingTransportEnabled) {
        this.isNonBlockingTransportEnabled = isNonBlockingTransportEnabled;
    }
}


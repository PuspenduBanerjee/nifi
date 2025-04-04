/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.nifi.toolkit.client.impl.request;

import org.apache.commons.lang3.StringUtils;
import org.apache.nifi.security.proxied.entity.StandardProxiedEntityEncoder;
import org.apache.nifi.toolkit.client.RequestConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of RequestConfig that produces headers for a request with
 * proxied-entities.
 */
public class ProxiedEntityRequestConfig implements RequestConfig {

    private static final String PROXIED_ENTITIES_CHAIN_HEADER = "X-ProxiedEntitiesChain";

    private final String[] proxiedEntities;

    public ProxiedEntityRequestConfig(final String... proxiedEntities) {
        this.proxiedEntities = Objects.requireNonNull(proxiedEntities);
    }

    @Override
    public Map<String, String> getHeaders() {
        final String proxiedEntitiesValue = getProxiedEntitiesValue(proxiedEntities);

        final Map<String, String> headers = new HashMap<>();
        if (proxiedEntitiesValue != null) {
            headers.put(PROXIED_ENTITIES_CHAIN_HEADER, proxiedEntitiesValue);
        }
        return headers;
    }

    private String getProxiedEntitiesValue(final String[] proxiedEntities) {
        if (proxiedEntities == null) {
            return null;
        }

        final List<String> proxiedEntityChain = Arrays.stream(proxiedEntities)
                .map(StandardProxiedEntityEncoder.getInstance()::getEncodedEntity)
                .collect(Collectors.toList());
        return StringUtils.join(proxiedEntityChain, "");
    }

}

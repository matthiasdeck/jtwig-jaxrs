/*
 * Copyright 2014 Matthias Deck <matthias.deck@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.jtwig.jaxrs;

import com.lyncode.jtwig.tree.api.Content;
import java.util.concurrent.ConcurrentHashMap;

public class JtwigCache {

    private static final JtwigCache INSTANCE = new JtwigCache();
    private final ConcurrentHashMap<String, Content> cache = new ConcurrentHashMap<>();
    
    private JtwigCache() {}
    
    public static final JtwigCache getInstance() {
        return INSTANCE;
    }

    public Content get(String key) {
        return cache.get(key);
    }

    public Content put(String key, Content value) {
        return cache.put(key, value);
    }

    public Content putIfAbsent(String key, Content value) {
        return cache.putIfAbsent(key, value);
    }

    public int size() {
        return cache.size();
    }
    
    public synchronized void clear() {
        cache.clear();
    }
}

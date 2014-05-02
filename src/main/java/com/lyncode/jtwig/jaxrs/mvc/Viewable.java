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

package com.lyncode.jtwig.jaxrs.mvc;

import java.util.Map;

public class Viewable {

    private final String name;
    private final Map<String, Object> model;

    public Viewable(String name, Map<String, Object> model) {
        this.name = name;
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}

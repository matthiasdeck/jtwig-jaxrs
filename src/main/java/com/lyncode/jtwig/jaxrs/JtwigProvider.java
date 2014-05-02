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

import com.lyncode.jtwig.JtwigContext;
import com.lyncode.jtwig.JtwigModelMap;
import com.lyncode.jtwig.JtwigTemplate;
import com.lyncode.jtwig.exception.CompileException;
import com.lyncode.jtwig.exception.ParseException;
import com.lyncode.jtwig.exception.RenderException;
import com.lyncode.jtwig.jaxrs.mvc.Viewable;
import com.lyncode.jtwig.tree.api.Content;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider
@Produces({MediaType.WILDCARD})
public final class JtwigProvider implements MessageBodyWriter<Viewable> {

    private static final Logger LOGGER = Logger.getLogger(JtwigProvider.class.getName());
    
    private final File templatePath;
    private final boolean cacheEnabled;
    
    private final JtwigCache cache = JtwigCache.getInstance();

    public JtwigProvider(File templatePath, boolean cacheEnabled) {
        this.templatePath = templatePath;
        this.cacheEnabled = cacheEnabled;
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Viewable.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(Viewable viewable, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(Viewable viewable, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {

        long time = System.nanoTime();
        
        try {

            Content content;
            
            if (cacheEnabled) {
                
                content = cache.get(viewable.getName());
                
                if (content == null) {
                    File file = new File(templatePath, viewable.getName());
                    JtwigTemplate template = new JtwigTemplate(file);
                    content = template.compile();
                    cache.putIfAbsent(viewable.getName(), content);
                }
                
            } else {
                
                File file = new File(templatePath, viewable.getName());
                JtwigTemplate template = new JtwigTemplate(file);
                content = template.compile();
            }
                
            JtwigModelMap model = new JtwigModelMap();
            model.add(viewable.getModel());

            content.render(entityStream, new JtwigContext(model));
            
        } catch (ParseException | CompileException | RenderException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        } finally {
            LOGGER.log(Level.INFO, "{0} : {1} ms", new Object[]{viewable.getName(),((System.nanoTime()-time)/(float)1000000)});
        }
    }
}

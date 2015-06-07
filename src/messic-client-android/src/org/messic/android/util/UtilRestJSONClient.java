/*
 * Copyright (C) 2013
 *
 *  This file is part of Messic.
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.messic.android.util;

import java.util.Arrays;

import org.messic.android.R.string;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import android.os.AsyncTask;

public class UtilRestJSONClient
{

    public interface RestListener<T>
    {
        void response( T response );

        void fail( Exception e );
    }

    /**
     * Rest POST petition to the server at the url param, sending all the post parameters defiend at formData. This post
     * return an object (json marshalling) of class defined at clazz parameter. You should register a
     * {@link RestListener} in order to obtain the returned object, this is because the petition is done in an async
     * process.
     * 
     * @param url {@link string} URL to attack
     * @param formData {@link MultiValueMap}<?,?/> map of parameters to send
     * @param clazz Class<T/> class that you will marshall to a json object
     * @param rl {@link RestListener} listener to push the object returned
     */
    public static <T> void post( final String url, MultiValueMap<?, ?> formData, final Class<T> clazz,
                                 final RestListener<T> rl )
    {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add( new MappingJackson2HttpMessageConverter() );

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept( Arrays.asList( MediaType.APPLICATION_JSON ) );
        // Sending multipart/form-data
        requestHeaders.setContentType( MediaType.APPLICATION_FORM_URLENCODED );
        // Populate the MultiValueMap being serialized and headers in an HttpEntity object to use for the request
        final HttpEntity<MultiValueMap<?, ?>> requestEntity =
            new HttpEntity<MultiValueMap<?, ?>>( formData, requestHeaders );

        AsyncTask<Void, Void, Void> at = new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground( Void... params )
            {
                try
                {
                    ResponseEntity<T> response = restTemplate.exchange( url, HttpMethod.POST, requestEntity, clazz );
                    rl.response( response.getBody() );
                }
                catch ( Exception e )
                {
                    rl.fail( e );
                }
                return null;
            }

        };

        at.execute();
    }

    /**
     * Rest GET petition to the server at the url param, sending all the post parameters defiend at formData. This post
     * return an object (json marshalling) of class defined at clazz parameter. You should register a
     * {@link RestListener} in order to obtain the returned object, this is because the petition is done in an async
     * process.
     * 
     * @param url {@link string} URL to attack
     * @param clazz Class<T/> class that you will marshall to a json object
     * @param rl {@link RestListener} listener to push the object returned
     */
    public static <T> void get( final String url, final Class<T> clazz, final RestListener<T> rl )
    {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add( new MappingJackson2HttpMessageConverter() );

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept( Arrays.asList( MediaType.APPLICATION_JSON ) );
        // Populate the MultiValueMap being serialized and headers in an HttpEntity object to use for the request
        final HttpEntity<MultiValueMap<?, ?>> requestEntity =
            new HttpEntity<MultiValueMap<?, ?>>( new LinkedMultiValueMap<String, Object>(), requestHeaders );

        AsyncTask<Void, Void, Void> at = new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground( Void... params )
            {
                try
                {
                    ResponseEntity<T> response = restTemplate.exchange( url, HttpMethod.GET, requestEntity, clazz );
                    rl.response( response.getBody() );
                }
                catch ( Exception e )
                {
                    rl.fail( e );
                }
                return null;
            }

        };

        at.execute();
    }
}

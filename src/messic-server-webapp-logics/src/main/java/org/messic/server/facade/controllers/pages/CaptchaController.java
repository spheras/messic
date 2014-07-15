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
package org.messic.server.facade.controllers.pages;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.XmlRootElement;

import org.jsondoc.core.annotation.ApiError;
import org.jsondoc.core.annotation.ApiErrors;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;
import org.jsondoc.core.annotation.ApiParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.pojo.ApiParamType;
import org.jsondoc.core.pojo.ApiVerb;
import org.messic.server.Util;
import org.messic.server.facade.controllers.rest.exceptions.CaptchaNotValidMessicRESTException;
import org.messic.server.facade.controllers.rest.exceptions.UnknownMessicRESTException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.image.gimpy.DeformedBaffleListGimpyEngine;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;

@Controller
@RequestMapping( "/captcha" )
public class CaptchaController
{

    @XmlRootElement
    @ApiObject( name = "Captcha", description = "Captcha informatino" )
    static class Captcha
    {
        @ApiObjectField( description = "identificator of the captcha" )
        public String id;

        @ApiObjectField( description = "b64 captcha image" )
        public String captchaImage;
    }

    private static CaptchaEngine ce = new DeformedBaffleListGimpyEngine();

    private static ImageCaptchaService service =
        new DefaultManageableImageCaptchaService( new FastHashMapCaptchaStore(), ce, 180, 100000, 75000 );;

    @ApiMethod( path = "/captcha", verb = ApiVerb.GET, description = "Get a captcha", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ) } )
    @RequestMapping( value = "", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public Captcha getCaptcha()
        throws UnknownMessicRESTException
    {

        try
        {
            UUID idOne = UUID.randomUUID();
            // create the image with the text
            BufferedImage bi = service.getImageChallengeForID( idOne.toString() );
            BufferedImage bi2 = Util.ImagedeepCopy( bi );
            Graphics2D g2d = (Graphics2D) bi.getGraphics();
            float alpha = 0.25f;
            int type = AlphaComposite.SRC_OVER;
            AlphaComposite composite = AlphaComposite.getInstance( type, alpha );
            g2d.setComposite( composite );

            final int Min = 5;
            final int Max = 30;
            int random1 = Min + (int) ( Math.random() * ( ( Max - Min ) + 1 ) );
            int random2 = Min + (int) ( Math.random() * ( ( Max - Min ) + 1 ) );

            g2d.drawImage( bi2, random1, random2, null );

            
            alpha = 0.80f;
            type = AlphaComposite.SRC_OVER;
            composite = AlphaComposite.getInstance( type, alpha );
            g2d.setComposite( composite );

            int MinX = 0;
            int MaxX = bi.getWidth();
            int MinY = 0;
            int MaxY = bi.getHeight();
            g2d.setColor( Color.black );
            for ( int i = 0; i < random2; i++ )
            {
                int random3 = MinX + (int) ( Math.random() * ( ( MaxX - MinX ) + 1 ) );
                int random4 = MinX + (int) ( Math.random() * ( ( MaxX - MinX ) + 1 ) );
                int random5 = MinY + (int) ( Math.random() * ( ( MaxY - MinY ) + 1 ) );
                int random6 = MinY + (int) ( Math.random() * ( ( MaxY - MinY ) + 1 ) );
                g2d.drawLine( random3, random5, random4, random6 );
            }

            Captcha result = new Captcha();
            result.id = idOne.toString();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( bi, "jpg", baos );
            byte[] resultb64 = Base64.encode( baos.toByteArray() );
            result.captchaImage = new String( resultb64 );
            return result;
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }
    }


    private HashMap<String, Long> validatedIds = new HashMap<String, Long>();

    @ApiMethod( path = "/captcha/validate?id=XXXX&response=XXXXX", verb = ApiVerb.GET, description = "Validate a captcha", produces = {
        MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE } )
    @ApiErrors( apierrors = { @ApiError( code = UnknownMessicRESTException.VALUE, description = "Unknown error" ) } )
    @RequestMapping( value = "/validate", method = RequestMethod.GET )
    @ResponseStatus( HttpStatus.OK )
    @ResponseBody
    @ApiResponseObject
    public boolean validateCaptcha( @RequestParam( value = "id", required = true )
                                    @ApiParam( name = "id", description = "id for the captcha validation.. This id must correspond to the generated id when the captcha was generated", paramType = ApiParamType.QUERY, required = true )
                                    String id,
                                    @RequestParam( value = "response", required = true )
                                    @ApiParam( name = "response", description = "response for the captcha that will be validated", paramType = ApiParamType.QUERY, required = true )
                                    String response )
        throws UnknownMessicRESTException, CaptchaNotValidMessicRESTException
    {
        try
        {
            // removing old keys
            Iterator<String> ids = validatedIds.keySet().iterator();
            while ( ids.hasNext() )
            {
                String key = ids.next();
                Long time = validatedIds.get( key );
                if ( System.currentTimeMillis() - time > ( 1000 * 60 * 10 ) )
                {
                    validatedIds.remove( key );
                }
            }

            if ( validatedIds.get( id ) != null )
            {
                return true;
            }

            boolean result = service.validateResponseForID( id, response );
            if ( !result )
            {
                throw new CaptchaNotValidMessicRESTException( new Exception( "Captcha validation error!" ) );
            }
            else
            {
                validatedIds.put( id, System.currentTimeMillis() );
            }

            return result;
        }
        catch ( Exception e )
        {
            throw new UnknownMessicRESTException( e );
        }
    }

}

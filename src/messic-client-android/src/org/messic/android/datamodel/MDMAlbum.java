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
package org.messic.android.datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.messic.android.datamodel.dao.DAOAuthor;
import org.messic.android.datamodel.dao.DAOGenre;
import org.messic.android.datamodel.dao.DAOSong;

import android.content.Context;
import android.database.Cursor;

public class MDMAlbum
    extends MDMFile
    implements Serializable
{
    public static final String COLUMN_LOCAL_SID = "lsid";

    public static final String COLUMN_SERVER_SID = "sid";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_YEAR = "year";

    public static final String COLUMN_COMMENTS = "comments";

    public static final String COLUMN_FILENAME = "filename";

    public static final String COLUMN_FK_AUTHOR = "fk_author";

    public static final String COLUMN_FK_GENRE = "fk_genre";

    public static final String TABLE_NAME = "albums";

    public static final String TABLE_CREATE = "create table " + TABLE_NAME + "(" + COLUMN_LOCAL_SID
        + " integer primary key autoincrement, " + COLUMN_SERVER_SID + " integer not null, " + COLUMN_NAME
        + " text not null," + COLUMN_YEAR + " integer," + COLUMN_COMMENTS + " text," + COLUMN_FILENAME
        + " text not null," + COLUMN_FK_AUTHOR + " integer not null," + COLUMN_FK_GENRE + " integer" + ");";

    public static String[] getColumns()
    {
        return new String[] { COLUMN_LOCAL_SID, COLUMN_SERVER_SID, COLUMN_NAME, COLUMN_YEAR, COLUMN_COMMENTS,
            COLUMN_FILENAME, COLUMN_FK_AUTHOR, COLUMN_FK_GENRE };
    }

    public MDMAlbum( Cursor cursor, Context context, boolean loadSongs )
    {
        this.lsid = cursor.getInt( 0 );
        this.sid = cursor.getInt( 1 );
        this.name = cursor.getString( 2 );
        this.year = cursor.getInt( 3 );
        this.comments = cursor.getString( 4 );
        this.fileName = cursor.getString( 5 );
        DAOGenre daogenre = new DAOGenre( context );
        daogenre.open();
        int sidGenre = cursor.getInt( 7 );
        Cursor cGenre = daogenre._get( sidGenre );
        this.genre = new MDMGenre( cGenre );
        DAOAuthor daoauthor = new DAOAuthor( context );
        daoauthor.open();
        int sidAuthor = cursor.getInt( 6 );
        Cursor cAuthor = daoauthor._get( sidAuthor );
        this.author = new MDMAuthor( cAuthor );
        daoauthor.close();
        daogenre.close();

        if ( loadSongs )
        {
            DAOSong daoSong = new DAOSong( context );
            List<MDMSong> songs=daoSong.getSongs( this.lsid );
            setSongs( songs );
        }
    }

    /**
     * 
     */
    private static final long serialVersionUID = -1992627692327048300L;

    private long sid;

    private int lsid;

    private String code;

    private String name;

    private Integer year;

    private MDMAuthor author;

    private MDMFile cover;

    private List<MDMSong> songs = new ArrayList<MDMSong>();

    private List<MDMFile> artworks = new ArrayList<MDMFile>();

    private List<MDMFile> others = new ArrayList<MDMFile>();

    private MDMGenre genre;

    private String comments;

    /**
     * Default constructor
     */
    public MDMAlbum()
    {

    }

    public final long getSid()
    {
        return sid;
    }

    public final void setSid( long sid )
    {
        this.sid = sid;
    }

    public final String getName()
    {
        return name;
    }

    public final void setName( String name )
    {
        this.name = name;
    }

    public final Integer getYear()
    {
        return year;
    }

    public final void setYear( Integer year )
    {
        this.year = year;
    }

    public final MDMAuthor getAuthor()
    {
        return author;
    }

    public final void setAuthor( MDMAuthor author )
    {
        this.author = author;
    }

    public final List<MDMSong> getSongs()
    {
        return songs;
    }

    public final void setSongs( List<MDMSong> songs )
    {
        this.songs = songs;
    }

    public final void addSong( MDMSong song )
    {
        if ( this.songs == null )
        {
            this.songs = new ArrayList<MDMSong>();
        }
        this.songs.add( song );
    }

    public final List<MDMFile> getArtworks()
    {
        return artworks;
    }

    public final void setArtworks( List<MDMFile> artworks )
    {
        this.artworks = artworks;
    }

    public final void addArtwork( MDMFile artwork )
    {
        if ( this.artworks == null )
        {
            this.artworks = new ArrayList<MDMFile>();
        }
        this.artworks.add( artwork );
    }

    public final List<MDMFile> getOthers()
    {
        return others;
    }

    public final void setOthers( List<MDMFile> others )
    {
        this.others = others;
    }

    public final void addOther( MDMFile other )
    {
        if ( this.others == null )
        {
            this.others = new ArrayList<MDMFile>();
        }
        this.others.add( other );
    }

    public MDMGenre getGenre()
    {
        return genre;
    }

    public void setGenre( MDMGenre genre )
    {
        this.genre = genre;
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments( String comments )
    {
        this.comments = comments;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }

    public MDMFile getCover()
    {
        return cover;
    }

    public void setCover( MDMFile cover )
    {
        this.cover = cover;
    }

    public String calculateExternalStorageFolder()
    {
        return getAuthor().calculateExternalStorageFolder() + "/" + "al" + getSid();
    }

    /**
     * @return the lsid
     */
    public int getLsid()
    {
        return lsid;
    }

    /**
     * @param lsid the lsid to set
     */
    public void setLsid( int lsid )
    {
        this.lsid = lsid;
    }
}

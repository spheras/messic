package org.messic.android.activities;

import org.messic.android.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class WelcomeActivity
    extends Activity
{

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_welcome );
        getActionBar().setDisplayHomeAsUpEnabled( true );

        TextView htmlTextView = (TextView) findViewById( R.id.welcome_content );
        htmlTextView.setText( Html.fromHtml( getString( R.string.help_welcome_content_html ), new ImageGetter(), null ) );

        findViewById( R.id.welcome_continue ).setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View v )
            {
                finish();
            }
        } );
    }

    private class ImageGetter
        implements Html.ImageGetter
    {

        public Drawable getDrawable( String source )
        {
            int id = 0;
            if ( source.equals( "messic.jpg" ) )
            {
                // id = R.drawable.messic;
            }
            else
            {
                return null;
            }

            Drawable d = getResources().getDrawable( id );
            d.setBounds( 0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight() );
            return d;
        }
    };

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        // Handle presses on the action bar items
        switch ( item.getItemId() )
        {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }

    }

}

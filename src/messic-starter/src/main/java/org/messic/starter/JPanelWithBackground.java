package org.messic.starter;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class JPanelWithBackground
    extends JPanel
{

    private Image backgroundImage;

    // Some code to initialize the background image.
    // Here, we use the constructor to load the image. This
    // can vary depending on the use case of the panel.
    public JPanelWithBackground( URL fileName )
        throws IOException
    {
        backgroundImage = ImageIO.read( fileName );
    }

    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );

        // Draw the background image.
        g.drawImage( backgroundImage, 0, 0, this );
    }
}
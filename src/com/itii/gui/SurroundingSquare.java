package com.itii.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class SurroundingSquare extends Square
{

//    int pSquareSize= 40;
    final String mName;

    public SurroundingSquare( short pX, short pY )
    {
        super(pX,pY);

        if ( pX == 0 )
        {
            mName= "" + ( pY );
        }
        else if ( pY == 0 )
        {
            mName= "" + (char) ( 64 + pX );
        }
        else
        {
            mName= "error";
        }
//        System.out.println("mname : " + mName);
    }

//    @Override
//    public void paintSquare(Graphics pGraphic, int pSquareSize)
    @Override
    public void paint(Graphics pGraphic)
    {

        Graphics2D g= ( Graphics2D ) pGraphic;
        g.setColor( Color.black );

        g.fillRect( 0,//pSquareSize * mCoordinates.getmX(),
                    0,//       pSquareSize * mCoordinates.getmY(),
                    getWidth(),//       pSquareSize,
                    getHeight() //       pSquareSize 
                    );

        g.setColor( Color.white );

        g.drawString( mName,
                      //pSquareSize * mCoordinates.getmX() 
                      pSquareSize / 3 ,
                      //pSquareSize * mCoordinates.getmY()
                      pSquareSize / 2 );

    }
}

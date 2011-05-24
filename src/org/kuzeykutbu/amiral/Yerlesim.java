/*
    Amiral Battı, Java ME
    Copyright (C) 2009  Kaya Oğuz

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.kuzeykutbu.amiral;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;

public class Yerlesim extends GameCanvas implements Runnable
{
    private Uygulama uyg;
    private boolean devam, sec, kon;
    private int prevKey;
    private int width, height, bx, by; 
    private Koordinat ko;
    private Image arka;
    private Sprite gemi, mesajlar, gemiler;
    private int state;
    private final int kose = 35;
    private final int kare = 17;
    private int alan[][];
    private long ctime, mtime;

    // sabitler
    private static final int GEMI_5 = 0;
    private static final int GEMI_4 = 1;
    private static final int GEMI_31 = 2;
    private static final int GEMI_32 = 3;
    private static final int GEMI_2 = 4;
    private static final int HAZIR = 5;

    public Yerlesim(Uygulama o)
    {
        super(true);
        uyg = o;
        devam = true;
        kon = true;
        sec = false;
        prevKey = 0;
        state = GEMI_5;
        alan = new int[10][10];
        ko = new Koordinat(5, 5, 5);
        ctime = mtime = System.currentTimeMillis();
        try 
        {
            arka = Image.createImage("/amiral.png");
            gemi = new Sprite(Image.createImage("/gemisp.png"), 17, 17);
            gemiler = new Sprite(Image.createImage("/gemiler.png"), 169, 18);
            mesajlar = new Sprite(Image.createImage("/mesajlar.png"), 225, 20);
        }
        catch (IOException ex) { ex.printStackTrace(); }
    }

    public int[][] getAlan()
    {
        return alan;
    }

    public void start()
    {
        width = getWidth();
        height = getHeight();
        bx = (width - arka.getWidth()) / 2;
        by = (height - arka.getHeight()) / 2;
        gemiler.setPosition(35+bx, 11+by);
        mesajlar.setPosition(7+bx, 212+by);
        Thread t = new Thread(this);
        t.start();
    }

    public void stop()
    {
        devam = false;
    }

    public void run()
    {
        Graphics g = getGraphics();
        while(devam)
        {
            if (System.currentTimeMillis() - ctime > 600)
            {
                gemi.nextFrame();
                ctime = System.currentTimeMillis();
            }
            if(System.currentTimeMillis() - mtime > 4000)
            {
                mesajlar.nextFrame();
                mtime = System.currentTimeMillis();
            }
            draw(g);
            input();
            try { Thread.sleep(30); }
            catch (Exception e) { stop(); }
        }
        uyg.oyunBasla(alan);
    }

    private void draw(Graphics g)
    {
        g.drawImage(arka, bx, by, 0);
        g.setColor(0x4c770e);
        if (sec)
            g.setColor(0xff0000);
        if (state != HAZIR)
            g.fillRect((bx+kose)+(ko.getX()*kare), (by+kose)+(ko.getY()*kare), kare, kare);

        int tx, ty;
        if (state != HAZIR)
        {
            for(int i=1;i<5;i++)
            {
                tx = ko.getXCorner(i); ty = ko.getYCorner(i);
                if ( (tx >= 0) && (tx < 10) && (ty >= 0) && (ty < 10)  )
                {
                    if (sec && ko.getSec() == i)
                        g.setColor(0xff0000);
                    else
                        g.setColor(0x87b546);

                    g.fillRect((bx+kose)+(tx*kare), (by+kose)+(ty*kare), kare, kare);
                }
            }
        }

        g.setColor(0x77633d);
        tx = bx+kose; ty = by+kose;
        for(int i=0;i<10;i++)
        {
            for(int j=0;j<10;j++)
            {
                if (alan[i][j] == Sabit.KARE_GEMI)
                {
                    gemi.setPosition(tx+(i*kare), ty+(j*kare));
                    gemi.paint(g);
                }
            }
        }
        gemiler.paint(g);
        mesajlar.paint(g);
        flushGraphics();
    }

    private boolean inputKon(int t, int k)
    {
        if ((t & k) != 0 && kon)
        {
            prevKey = k;
            kon = false;
            return true;
        }
        else if ((t&k) == 0 && prevKey == k)
            kon = true;
        
        return false;
    }

    private void input()
    {
        int tuslar = getKeyStates();
        if (inputKon(tuslar, LEFT_PRESSED))
        {
            if (!sec) ko.updateVal(-1, 0);
            else ko.koseSec(Koordinat.BATI);
        }
        else if (inputKon(tuslar, RIGHT_PRESSED))
        {
            if (!sec) ko.updateVal(1, 0);
            else ko.koseSec(Koordinat.DOGU);
        }
        else if (inputKon(tuslar, UP_PRESSED))
        {
            if (!sec) ko.updateVal(0, -1);
            else ko.koseSec(Koordinat.KUZEY);
        }
        else if (inputKon(tuslar, DOWN_PRESSED))
        {
            if (!sec) ko.updateVal(0, 1);
            else ko.koseSec(Koordinat.GUNEY);
        }
        else if (inputKon(tuslar, FIRE_PRESSED))
        {
            if (state == HAZIR) 
            {
                devam = false;
                return;
            }
            if (sec && ko.yerlestir(alan))
            {
                sec = false;
                switch(state)
                {
                    case GEMI_5:
                        state = GEMI_4;
                        ko = new Koordinat(5, 5, 4);
                        gemiler.nextFrame();
                        break;
                    case GEMI_4:
                        state = GEMI_31;
                        ko = new Koordinat(5, 5, 3);
                        gemiler.nextFrame();
                        break;
                    case GEMI_31:
                        state = GEMI_32;
                        ko = new Koordinat(5, 5, 3);
                        gemiler.nextFrame();
                        break;
                    case GEMI_32:
                        state = GEMI_2;
                        ko = new Koordinat(5, 5, 2);
                        gemiler.nextFrame();
                        break;
                    case GEMI_2:
                        state = HAZIR;
                        gemiler.nextFrame();
                        break;
                }
            }
            else if (!sec)
            {
                if (alan[ko.getX()][ko.getY()] == Sabit.KARE_GEMI)
                    return;
                sec = true;
            }
            else if (sec)
                sec = false;
        }
    }
}

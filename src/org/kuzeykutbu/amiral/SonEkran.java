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

/**
 *
 * @author kaya
 */
public class SonEkran extends GameCanvas implements Runnable
{
    Uygulama uyg;
    private boolean devam;
    private Image resim;
    private long sure;
    private int px, py;
    public SonEkran(Uygulama u, boolean kazan)
    {
        super(true);
        uyg = u;
        devam = true;
        try
        {
            if (kazan)
            {
                resim = Image.createImage("/kazan.png");
            }
            else
            {
                resim = Image.createImage("/kaybet.png");
            }
        }
        catch (IOException ex) { ex.printStackTrace();}
    }

    public void start()
    {
        px = (getWidth() - resim.getWidth()) / 2;
        py = (getHeight() - resim.getHeight()) / 2;
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
            draw(g);
            input();
            try { Thread.sleep(300); }
            catch (InterruptedException e) { stop(); }
        }
        uyg.menu();
    }

    private void draw(Graphics g)
    {
        g.drawImage(resim, px, py, 0);
        flushGraphics();
    }

    private void input()
    {
        int k = getKeyStates();
        if ((k & FIRE_PRESSED) != 0)
            devam = false;
    }
    
}

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

/**
 * 
 * @author kaya
 */
public class Ekran extends GameCanvas implements Runnable
{
    private boolean devam;
    private Sprite kk;
    private long sure;
    private int px, py;
    private Uygulama oyun;
    public Ekran(Uygulama o)
    {
        super(true);
        oyun = o;
        devam = true;
        try { kk = new Sprite(Image.createImage("/kk.png"), 68, 74); }
        catch (IOException ex) { ex.printStackTrace();}

        sure = System.currentTimeMillis();
    }

    public void start()
    {
        px = (getWidth() - kk.getWidth()) / 2;
        py = (getHeight() - kk.getHeight()) / 2;
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
            try { Thread.sleep(100); }
            catch (InterruptedException e) { stop(); }
        }
        oyun.menu();
    }

    private void draw(Graphics g)
    {
        kk.setPosition(px, py);
        kk.paint(g);
        if (System.currentTimeMillis() - sure > 100 && kk.getFrame() < 4 )
        {
            kk.nextFrame();
            sure = System.currentTimeMillis();
        }
        flushGraphics();
    }

    private void input()
    {
        int k = getKeyStates();
        if ((k & FIRE_PRESSED) != 0)
            devam = false;
    }
}

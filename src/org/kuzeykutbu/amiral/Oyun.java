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

import java.io.InputStream;
import java.util.Random;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;


/**
 * Oyunun olduğu ekran
 * @author kaya
 */
public class Oyun extends GameCanvas implements Runnable
{
    private Uygulama uyg;
    private int oyuncu[][];
    private int telefon[][];
    private Yon yon[][];
    private Image arka;
    private Sprite gemi, patla, iska, mesajlar, kriptik;
    private long gtime, ptime, itime, ktime;
    private Random r;
    private int bx, by, px, py;
    private final int kose = 35;
    private final int kare = 17;
    private boolean devam;
    private boolean sira;
    private boolean bekle;
    private long bekleSure;
    private int sayOyuncu, sayTelefon;
    private boolean kon;
    private int prevKey;
    private boolean ses;
    private int sonYon;
    private Player atesSes, patlama, karavana;

    public Oyun(Uygulama u, int[][] a, boolean s)
    {
        super(true);
        uyg = u;
        oyuncu = a;
        devam = true;
        sira = true;
        bekle = false;
        kon = true;
        sonYon = Sabit.YON_BOS;
        prevKey = 0;
        bekleSure = 0;
        sayOyuncu = 0;
        sayTelefon = 0;
        ses = s;
        gtime = itime = ptime = ktime = System.currentTimeMillis();
        telefon = new int[10][10];
        yon = new Yon[10][10];
        r = new Random(System.currentTimeMillis());
        gemiYerlesimi(0);
        
        try
        {
            arka = Image.createImage("/amiral.png");
            gemi = new Sprite(Image.createImage("/gemisp.png"), 17, 17);
            iska = new Sprite(Image.createImage("/iskasp.png"), 17, 17);
            patla = new Sprite(Image.createImage("/patlasp.png"), 17, 17);
            mesajlar = new Sprite(Image.createImage("/smsglar.png"), 169, 18);
            kriptik = new Sprite(Image.createImage("/m2.png"), 225, 20);

            if(ses)
            {
                InputStream is = getClass().getResourceAsStream("/ates.wav");
                atesSes = Manager.createPlayer(is, "audio/X-wav");

                is = getClass().getResourceAsStream("/patlama.wav");
                patlama = Manager.createPlayer(is, "audio/X-wav");

                is = getClass().getResourceAsStream("/iska.wav");
                karavana = Manager.createPlayer(is, "audio/X-wav");
            }
        }
        catch(Exception e) { stop(); }
        for(int i=0;i<10;i++)
        {
            for(int j=0;j<10;j++)
            {
                yon[i][j] = new Yon(0, Sabit.YON_BOS);
            }
        }
    }

    public void start()
    {
        bx = (getWidth() - arka.getWidth()) / 2;
        by = (getHeight() - arka.getHeight()) / 2;
        mesajlar.setPosition(35+bx, 11+by);
        kriptik.setPosition(7+bx, 212+by);
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
            update();
            input();
            draw(g);
            try { Thread.sleep(30); }
            catch (Exception e) { stop(); }
        }
        uyg.sonuc(sayTelefon == 17);
    }

    private void ates(int[][] alan, int x, int y)
    {
        switch(alan[x][y])
        {
            case Sabit.KARE_BOS:
                alan[x][y] = Sabit.KARE_KARAVANA;
                bekle = true;
                bekleSure = System.currentTimeMillis();
                if(!sira)
                {
                    yon[x][y].setVals(-1, yon[x][y].getYon());
                    sonYon = Sabit.YON_BOS;
                }
                mesajlar.setFrame(2);
                if (ses)
                {
                    try { karavana.start(); }
                    catch (Exception e) {}
                }
                break;
            case Sabit.KARE_GEMI:
                alan[x][y] = Sabit.KARE_VUR;
                if(ses)
                {
                    try { patlama.start(); }
                    catch (Exception e) {}
                }
                if (sira)
                {
                    sayTelefon++;
                    mesajlar.setFrame(3);
                }
                else
                {
                    sayOyuncu++;
                    mesajlar.setFrame(4);
                    int v = yon[x][y].getVal();
                    v = v + 1;
                    sonYon = yon[x][y].getYon();
                    if (x+1<10 && (sonYon == Sabit.YON_DOGU || sonYon == Sabit.YON_BOS))
                        yon[x+1][y].setVals(v, Sabit.YON_DOGU);
                    if (x-1 >= 0 && (sonYon == Sabit.YON_BATI || sonYon == Sabit.YON_BOS))
                        yon[x-1][y].setVals(v, Sabit.YON_BATI);
                    if (y+1<10 && (sonYon == Sabit.YON_GUNEY || sonYon == Sabit.YON_BOS))
                        yon[x][y+1].setVals(v, Sabit.YON_GUNEY);
                    if (y-1 >= 0 && (sonYon == Sabit.YON_KUZEY || sonYon == Sabit.YON_BOS))
                        yon[x][y-1].setVals(v, Sabit.YON_KUZEY);
                }
                bekle = true;
                bekleSure = System.currentTimeMillis();
                break;
            case Sabit.KARE_KARAVANA:
            case Sabit.KARE_VUR:
            default:
                break;
        }
    }

    private void draw(Graphics g)
    {
        g.drawImage(arka, bx, by, 0);

        int tx, ty;
        int temp[][];
        for(int i=0;i<10;i++)
        {
            for(int j=0;j<10;j++)
            {
                tx = bx + kose + (i*kare);
                ty = by + kose + (j*kare);

                if(sira) temp = telefon;
                else temp = oyuncu;

                switch(temp[i][j])
                {
                    case Sabit.KARE_GEMI:
                        if (!sira)
                        {
                            gemi.setPosition(tx, ty);
                            gemi.paint(g);
                        }
                        break;
                    case Sabit.KARE_KARAVANA:
                        iska.setPosition(tx, ty);
                        iska.paint(g);
                        break;
                    case Sabit.KARE_VUR:
                        patla.setPosition(tx, ty);
                        patla.paint(g);
                        break;
                }

                if (px == i && py == j && sira)
                {
                    g.setColor(0x000000);
                    g.drawRect(tx, ty, kare-1, kare-1);
                }
            }
        }
        mesajlar.paint(g);
        kriptik.paint(g);
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
        if(bekle)
            return;
        int tuslar = getKeyStates();
        if(sira)
        {
            if (inputKon(tuslar, LEFT_PRESSED))
            {
                px--;
                if (px < 0) px = 9;
            }
            else if (inputKon(tuslar, RIGHT_PRESSED))
            {
                px++;
                if (px > 9) px = 0;
            }
            else if (inputKon(tuslar, UP_PRESSED))
            {
                py--;
                if (py < 0) py = 9;
            }
            else if (inputKon(tuslar, DOWN_PRESSED))
            {
                py++;
                if (py > 9) py = 0;
            }
            else if (inputKon(tuslar, FIRE_PRESSED))
            {
                ates(telefon, px, py);
                if(ses)
                {
                    try { atesSes.start(); }
                    catch (Exception e) {}
                }
            }
        }
        else
        {
            if (!bekle)
            {
                yapayZeka();
                // ates(oyuncu, r.nextInt(10), r.nextInt(10));
            }
                
        }
    }

    private void yapayZeka()
    {
        boolean t = true;
        int x, y, v;
        x = 0; y = 0; v = 0; //yon[x][y].getVal();
        for(int i=0;i<10;i++)
        {
            for(int j=0;j<10;j++)
            {
                if (yon[i][j].getVal() > v && (oyuncu[i][j] == Sabit.KARE_BOS || oyuncu[i][j] == Sabit.KARE_GEMI) )
                {
                    v = yon[i][j].getVal();
                    x = i; y = j;
                    t = false;
                }
            }
        }
        if(t)
        {
            do
            {
                x = r.nextInt(10); y = r.nextInt(10);
            }
            while(oyuncu[x][y] == Sabit.KARE_KARAVANA || oyuncu[x][y] == Sabit.KARE_VUR);
        }
        ates(oyuncu, x, y);
    }

    private void gemiYerlesimi(int a)
    {
        if (a == 5)
            return;
        int gs[] = { 5, 4, 3, 3, 2 };
        Koordinat temp = new Koordinat( r.nextInt(10), r.nextInt(10), gs[a]);
        temp.koseSec( (r.nextInt(4)) + 1 );
        while(!temp.yerlestir(telefon))
        {
            temp.setVal(r.nextInt(10), r.nextInt(10));
            temp.koseSec((r.nextInt(4)) + 1);
        }
        gemiYerlesimi(a+1);
    }

    private void update()
    {
        if(System.currentTimeMillis() - ptime > 500)
        {
            patla.nextFrame();
            ptime = System.currentTimeMillis();
        }
        if(System.currentTimeMillis() - itime > 200)
        {
            iska.nextFrame();
            itime = System.currentTimeMillis();
        }
        if(System.currentTimeMillis() - gtime > 600)
        {
            gemi.nextFrame();
            gtime = System.currentTimeMillis();
        }
        if(System.currentTimeMillis() - ktime > 5000)
        {
            kriptik.nextFrame();
            ktime = System.currentTimeMillis();
        }

        // game con

        if (sayOyuncu == 17 || sayTelefon == 17)
        {
            devam = false;
            return;
        }
        if (!bekle)
            return;
        if (System.currentTimeMillis() - bekleSure < 1000)
            return;
        else 
        {
            bekle = false;
            sira = !sira;
            if (sira)
                mesajlar.setFrame(0);
            else mesajlar.setFrame(1);
        }
    }
}

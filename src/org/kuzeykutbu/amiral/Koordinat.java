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

/**
 * Yerleşim sırasında kullanılan destek sınıfı
 * @author kaya
 * @see Yerlesim
 */
public class Koordinat
{
    private int x[], y[], s, sec;

    public static final int BATI = 1;
    public static final int DOGU = 2;
    public static final int KUZEY = 3;
    public static final int GUNEY = 4;

    public Koordinat (int a, int b, int s)
    {
        x = new int[5];
        y = new int[5];
        this.s = s;
        sec = 0;
        setVal(a, b);
    }

    /**
     * Koordinatları tekrar hesaplıyor. Koordinatla beraber diğer
     * köşeleri de hesaplıyor. Harita dışına çıkmasını engelliyor (10x10).
     * @param a x eksenindeki konum
     * @param b y eksenindeki konum
     */
    public void setVal(int a, int b)
    {
        if ((a<0) || (a>9) || (b<0) || (b>9) )
            return;
        x[0] = a; y[0] = b;
        x[BATI] = a-s+1; y[BATI] = b;
        x[DOGU] = a+s-1; y[DOGU] = b;
        x[KUZEY] = a; y[KUZEY] = b-s+1;
        x[GUNEY] = a; y[GUNEY] = b+s-1;
    }

    /**
     * Koordinatları a ve b değeri kadar ilerletmeye yarıyor. Bu
     * değerler eksi olabilir.
     * @param a x ekseninde ilerleme değeri
     * @param b y ekseninde ilerleme değeri
     * @see setVal(int a, int b)
     */
    public void updateVal(int a, int b)
    {
        setVal(x[0]+a, y[0]+b);
    }

    public int getX() { return x[0]; }
    public int getY() { return y[0]; }

    public int getXCorner(int c) { return x[c]; }
    public int getYCorner(int c) { return y[c]; }

    public void koseSec(int a)
    {
        if (x[a] < 0 || x[a] > 9 || y[a] < 0 || y[a] > 9) // nasıl da gözden kaçmış
            sec = 0;
        else sec = a;
    }
    public int  getSec() { return sec; }
    public int  getXSec() { return x[sec]; }
    public int  getYSec() { return y[sec]; }
    public boolean secXesitMi() { return x[0] == x[sec]; }
    public boolean secYesitMi() { return y[0] == y[sec]; }

    public boolean yerlestir(int[][] alan)
    {
        if(getSec() == 0)
            return false;

        if (secXesitMi())
        {
            // x'ler eşitse yukarıdan aşağıya
            int xx = getX();
            int sy, ey;
            if (getYSec() < getY())
            {
                sy = getYSec();
                ey = getY();
            }
            else
            {
                sy = getY();
                ey = getYSec();
            }
            for(int i=sy;i<=ey;i++)
            {
                if (alan[xx][i] != 0)
                {
                    sec = 0;
                    return false;
                }
            }
            for(int i=sy;i<=ey;i++)
                alan[xx][i] = Sabit.KARE_GEMI;
        }
        else if (secYesitMi())
        {
            // y'ler eşitse soldan sağa
            int yy = getY();
            int sx, ex;
            if (getXSec() < getX())
            {
                sx = getXSec();
                ex = getX();
            }
            else
            {
                sx = getX();
                ex = getXSec();
            }
            for(int i=sx;i<=ex;i++)
            {
                if (alan[i][yy] != 0)
                {
                    sec = 0;
                    return false;
                }
            }
            for(int i=sx;i<=ex;i++)
                alan[i][yy] = Sabit.KARE_GEMI;
        }
        return true;
    }
}

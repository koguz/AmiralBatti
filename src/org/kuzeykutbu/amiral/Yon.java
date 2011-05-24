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
 * Yapay zekaya yardımcı Yön sınıfı
 * @author kaya
 */
public class Yon
{
    private int val;
    private int yon;
    public Yon(int v, int y)
    {
        setVals(v, y);
    }

    public void setVals(int v, int y)
    {
        yon = y; val = v;
    }

    public int getYon()
    {
        return yon;
    }

    public int getVal()
    {
        return val;
    }
}

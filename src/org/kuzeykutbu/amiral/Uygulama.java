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

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.midlet.MIDlet;

/**
 * 
 * @author kaya
 */
public class Uygulama implements CommandListener
{
    MIDlet midlet;
    private boolean ses;
    private ChoiceGroup cgSes;
    public Uygulama(MIDlet m)
    {
        midlet = m;
        ses = false;
    }

    public void baslat()
    {
        Ekran e = new Ekran(this);
        e.start();
        setScreen(e);
    }

    private void setScreen(Displayable d)
    {
        Display.getDisplay(midlet).setCurrent(d);
    }

    public void menu()
    {
        List l = new List("Amiral Battı", List.IMPLICIT);
        l.append("Yeni Oyun", null);
        l.append("Ayarlar", null);
        l.append("Yardım", null);
        l.append("Hakkında", null);
        l.append("Çıkış", null);
        l.setCommandListener(this);
        setScreen(l);
    }

    private void yeniOyun()
    {
        Yerlesim y = new Yerlesim(this);
        y.start();
        setScreen(y);
    }

    public void oyunBasla(int[][] a)
    {
        Oyun o = new Oyun(this, a, ses);
        o.start();
        setScreen(o);
    }

    public void sonuc(boolean kazan)
    {
        SonEkran e = new SonEkran(this, kazan);
        e.start();
        setScreen(e);
    }

    public void commandAction(Command c, Displayable d)
    {
        if(d.getTitle().compareTo("Amiral Battı") ==  0)
        {
            List l = (List)d;
            switch(l.getSelectedIndex())
            {
                case 0:
                    yeniOyun();
                    break;
                case 1:
                    ayarlar();
                    break;
                case 2:
                    yardim();
                    break;
                case 3:
                    hakkinda();
                    break;
                case 4:
                default:
                    ((Amiral)midlet).quitApp();
                    break;
            }
        }
        else if (d.getTitle().compareTo("Hakkında") == 0)
        {
            menu();
        }
        else if (d.getTitle().compareTo("Yardım") == 0)
        {
            menu();
        }
        else if (d.getTitle().compareTo("Ayarlar") == 0)
        {
            ses = (cgSes.getSelectedIndex() == 0);
            menu();
        }
    }

    private void ayarlar()
    {
        Form f = new Form("Ayarlar");
        cgSes = new ChoiceGroup("Ses Ayarı", ChoiceGroup.EXCLUSIVE);
        cgSes.append("Ses Açık", null);
        cgSes.append("Ses Kapalı", null);
        cgSes.setSelectedIndex(0, ses);
        cgSes.setSelectedIndex(1, !ses);
        Command tamam = new Command("Tamam", Command.OK, 1);
        f.addCommand(tamam);
        f.append(cgSes);
        f.setCommandListener(this);
        setScreen(f);
    }

    private void hakkinda()
    {
        Form f = new Form("Hakkında");
        StringItem h = new StringItem("", "Kuzey Kutbu, 2009. Biricik bitanem aşkım, Damla'ya :*");
        f.append(h);
        Command geri = new Command("Geri", Command.BACK, 1);
        f.addCommand(geri);
        f.setCommandListener(this);
        setScreen(f);
    }

    private void yardim()
    {
        Form f = new Form("Yardım");
        StringItem y = new StringItem("",
                "Oyunda ilk olarak gemilerinizi yerleştirmelisiniz. " +
                "Daha sonra karşı tarafın gemilerini, kendi gemileriniz " +
                "bitmeden yok etmelisiniz."
                );
        f.append(y);
        Command geri = new Command("Geri", Command.BACK, 1);
        f.addCommand(geri);
        f.setCommandListener(this);
        setScreen(f);
    }
}
